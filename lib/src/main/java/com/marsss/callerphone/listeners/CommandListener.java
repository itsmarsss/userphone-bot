package com.marsss.callerphone.listeners;

import com.marsss.callerphone.Callerphone;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        final Message MESSAGE = event.getMessage();
        if (MESSAGE.isWebhookMessage())
            return;

        if (!event.getChannel().canTalk())
            return;

        final Member MEMBER = event.getMember();

        final String CONTENT = MESSAGE.getContentRaw();

        final String[] ARGS = CONTENT.split("\\s+");


        if (MEMBER.getUser().isBot() || MEMBER.getUser().isSystem())
            return;

        //        if(Callerphone.blacklist.contains(event.getAuthor().getId())) {
//            MESSAGE.reply("Sorry you are blacklisted, submit an appeal in our support server").queue();
//            break;
//        }

        if (CONTENT.contains(Callerphone.jda.getSelfUser().getId())) {
            MESSAGE.reply("My prefix is `" + Callerphone.Prefix + "`, do `" + Callerphone.Prefix + "help` for a list of commands!").queue();
            return;
        }

        if (!ARGS[0].toLowerCase().startsWith(Callerphone.Prefix))
            return;

        String trigger = ARGS[0].toLowerCase().replace(Callerphone.Prefix, "");

        try {
            if (Callerphone.cmdMap.containsKey(trigger)) {
                Callerphone.cmdMap.get(trigger).runCommand(event);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendError(event.getMessage(), ex);
        }

    }

    public static void sendError(Message message, Exception error) {
        message.reply(String.format(Callerphone.ERROR_MSG, error.toString())).queue();
    }

    public static String adminHelp() {
        return "`" + Callerphone.Prefix + "mod <id>` - Adds id to mod list.\n" +
                "`" + Callerphone.Prefix + "rmod <id>` - Removes id from mod list.";
    }

    public static String blacklistHelp() {
        return "`" + Callerphone.Prefix + "blacklist <id>` - Adds id to blacklist.\n" +
                "`" + Callerphone.Prefix + "rblacklist <id>` - Removes id from blacklist.";
    }

    public static String supportHelp() {
        return "`" + Callerphone.Prefix + "prefix <id> <prefix>` - Give user a prefix.\n" +
                "`" + Callerphone.Prefix + "rprefix <id>` - Removes user prefix.";
    }

    public static String showItemsHelp() {
        return "`" + Callerphone.Prefix + "blackedlist` - Shows all black listed users.\n" +
                "`" + Callerphone.Prefix + "prefixlist` - Shows all prefixes for users.\n" +
                "`" + Callerphone.Prefix + "infolist` - Shows all info for startup.\n" +
                "`" + Callerphone.Prefix + "modlist` - Shows all moderators.\n" +
                "`" + Callerphone.Prefix + "filterlist` - Shows all chat filters.";
    }

}
