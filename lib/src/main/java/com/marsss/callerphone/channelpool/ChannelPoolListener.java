package com.marsss.callerphone.channelpool;

import com.marsss.callerphone.Callerphone;
import com.marsss.callerphone.Response;
import com.marsss.callerphone.ToolSet;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ChannelPoolListener extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        final Message MESSAGE = event.getMessage();
        if (MESSAGE.isWebhookMessage())
            return;

        if (!event.getChannel().canTalk())
            return;

        final Member MEMBER = event.getMember();

        String content = MESSAGE.getContentRaw();

        if (MEMBER.getUser().isBot() || MEMBER.getUser().isSystem())
            return;

        if (content.startsWith("\\\\") || content.startsWith(Callerphone.Prefix)) {
            return;
        }
        if (!(ChannelPool.isHost(event.getChannel().getId()) || ChannelPool.isChild(event.getChannel().getId()))) {
            return;
        }

        content = ToolSet.messageCheck(content);

        String sendCont = String.format("**%s**#%s `%s` | <t:%d:f>\n%s",
                MESSAGE.getAuthor().getName(),
                MESSAGE.getAuthor().getDiscriminator(),
                MEMBER.getEffectiveName(),
                MESSAGE.getTimeCreated().toEpochSecond(),
                content
        );

        ChannelPool.broadCast(event.getChannel().getId(),
                event.getChannel().getId(),
                sendCont
        );

        if ((System.currentTimeMillis() - Callerphone.getUserCooldown(event.getAuthor())) > Callerphone.cooldown) {
            Callerphone.updateUserCooldown(event.getAuthor());

            Callerphone.reward(event.getAuthor(), 3);
            Callerphone.addTransmit(event.getAuthor(), 1);
        }
    }
}
