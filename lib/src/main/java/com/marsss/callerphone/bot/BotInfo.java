package com.marsss.callerphone.bot;

import com.marsss.callerphone.ToolSet;
import com.marsss.commandType.ISlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;


public class BotInfo implements ISlashCommand {
    @Override
    public void runSlash(SlashCommandInteractionEvent e) {
        e.replyEmbeds(botInfo(e.getJDA())).setEphemeral(true).queue();
    }

    private MessageEmbed botInfo(JDA jda) {
        StringBuilder description = new StringBuilder()
                .append("**Tag of the bot:** ").append(jda.getSelfUser().getAsTag())
                .append("\n**Avatar url:** [link](").append(jda.getSelfUser().getAvatarUrl()).append(")")
                .append("\n**Time created:** ").append(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(jda.getSelfUser().getTimeCreated()))
                .append("\n**Id:** ").append(jda.getSelfUser().getId())
                .append("\n**Shard info:** [").append(jda.getShardInfo().getShardId() + 1).append("/").append(jda.getShardInfo().getShardTotal()).append("]")
                .append("\n**Servers:** ").append(jda.getGuilds().size());

        EmbedBuilder botInfo = new EmbedBuilder()
                .setColor(ToolSet.COLOR)
                .setTitle("**Bot Info**");

        CompletableFuture<Void> future = new CompletableFuture<>();

        jda.getRestPing().queue(
                (ping) -> {
                    botInfo.setDescription(description
                            .append("\n**Reset ping:** ").append(ping).append("ms")
                            .append("\n**WS ping:** ").append(jda.getGatewayPing()).append("ms"));

                    future.complete(null);
                },
                future::completeExceptionally
        );

        try {
            future.get();
        } catch (Exception e) {
            botInfo.setDescription(description
                    .append("\n**Reset ping:** ").append("*Unable to obtain*")
                    .append("\n**WS ping:** ").append(jda.getGatewayPing()));
        }

        return botInfo.build();
    }


    @Override
    public String getHelp() {
        return "`/botinfo` - Get information about the bot.";
    }

    @Override
    public String[] getTriggers() {
        return "botinfo,info".split(",");
    }

    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash(getTriggers()[0], getHelp().split(" - ")[1])
                .setGuildOnly(true);
    }

}
