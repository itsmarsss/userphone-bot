package com.marsss.callerphone.tccallerphone.commands;

import com.marsss.ICommand;
import com.marsss.callerphone.Callerphone;
import com.marsss.callerphone.tccallerphone.ChatResponse;
import com.marsss.callerphone.tccallerphone.TCCallerphone;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.MessageReceivedEvent;

public class EndChat implements ICommand {
    @Override
    public void runCommand(MessageReceivedEvent e) {
        e.getMessage().reply(endChat(e.getChannel())).queue();
    }

    @Override
    public void runSlash(SlashCommandEvent e) {
        e.reply(endChat(e.getTextChannel())).queue();
    }

    private String endChat(TextChannel channel) {
        if(!TCCallerphone.hasCall(channel.getId())) {
            return ChatResponse.NO_CALL.toString();
        }
        return TCCallerphone.onEndCallCommand(channel);
    }

    @Override
    public String getHelp() {
        return "`" + Callerphone.config.getPrefix() + "endchat` - End chatting with people from another server.";
    }

    @Override
    public String[] getTriggers() {
        return "end,hangup,endcall,endchat,enduserphone,endcallerphone,endphone,endphonecall".split(",");
    }
}
