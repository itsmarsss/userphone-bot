package com.marsss.callerphone.msginbottle;

import com.marsss.callerphone.Callerphone;
import com.marsss.callerphone.ToolSet;
import com.marsss.callerphone.msginbottle.entities.Bottle;
import com.marsss.callerphone.msginbottle.entities.Page;
import com.marsss.database.categories.MIB;
import com.marsss.database.categories.Users;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class MessageInBottle {
    public static final Logger logger = LoggerFactory.getLogger(MessageInBottle.class);

    public static MIBStatus sendBottle(String id, String message, boolean anon, String mibId) {
        Bottle bottle = mibId == null ? MIB.createMIB(id, message, anon) : MIB.addMIBPage(id, message, anon, mibId);

        if (bottle == null) {
            return MIBStatus.ERROR;
        }

        log(bottle);
        return MIBStatus.SENT;
    }

    public static Bottle findBottle() {
        return MIB.findBottle();
    }

    public static MessageCreateData createMessage(Bottle bottle, int pageNum) {
        if (pageNum == Integer.MAX_VALUE) {
            pageNum = Math.min(Math.max(bottle.getPages().size() - 1, 0), bottle.getPages().size() - 1);
        }

        Page page = bottle.getPages().get(pageNum);

        CompletableFuture<MessageCreateData> future = new CompletableFuture<>();

        ToolSet.getUser(page.getAuthor()).queue(lastUser -> {
                    String sign = "anonymous";
                    String prefix = Users.getPrefix(lastUser.getId());

                    if (page.isSigned()) {
                        sign = (prefix.isEmpty() ? "" : "*[" + prefix + "]* ") + lastUser.getName();
                    }

                    EmbedBuilder bottleEmbed = new EmbedBuilder()
                            .setTitle("<:MessageInBottle:1089648266284638339> **A message in bottle has arrived!**")
                            .setDescription(page.getMessage())
                            .appendDescription("\n\n\u3000**\\- " + sign + "** from  <t:" + page.getReleased() + ":R>")
                            .setFooter("Pages " + (page.getPageNum() + 1) + "/" + bottle.getPages().size())
                            .setTimestamp(Instant.now())
                            .setColor(ToolSet.COLOR);

                    Button addPage = Button.success("adp-" + bottle.getId(), "Add Page");
                    Button previousPage = Button.secondary("pvp-" + bottle.getId() + "-" + Math.max(page.getPageNum() - 1, 0), "\u25C0\uFE0F");
                    Button nextPage = Button.secondary("nxp-" + bottle.getId() + "-" + Math.min(page.getPageNum() + 1, bottle.getPages().size() - 1), "\u25B6\uFE0F");
                    Button reportButton = Button.danger("rpt-" + bottle.getId() + "-" + page.getPageNum(), "Report");
                    Button saveACopy = Button.secondary("sve", "Save");

                    MessageCreateData message = new MessageCreateBuilder()
                            .setEmbeds(bottleEmbed.build())
                            .setComponents(ActionRow.of(addPage, previousPage, nextPage, reportButton, saveACopy)).build();

                    future.complete(message);
                },
                future::completeExceptionally
        );

        try {
            return future.get();
        } catch (Exception e) {
        }

        return null;
    }

    private static void log(Bottle bottle) {
        MessageCreateData message = createMessage(bottle, bottle.getPages().size() - 1);
        final TextChannel TEMP_CHANNEL = ToolSet.getTextChannel(Callerphone.config.getTempChatChannel());

        if (TEMP_CHANNEL != null) {
            TEMP_CHANNEL.sendMessage("**ID:** " + bottle.getId()).addEmbeds(message.getEmbeds()).queue();
        }
    }
}