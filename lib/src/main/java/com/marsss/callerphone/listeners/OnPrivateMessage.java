//package com.marsss.callerphone.listeners;
//
//import java.awt.Color;
//import java.io.File;
//
//import com.marsss.callerphone.Callerphone;
//
//import net.dv8tion.jda.api.EmbedBuilder;
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.entities.MessageEmbed;
//import net.dv8tion.jda.api.entities.User;
//import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import net.dv8tion.jda.api.utils.AttachmentOption;
//
//// IF IT WORKS DON'T TOUCH IT
//public class OnPrivateMessage extends ListenerAdapter {
//    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
//        if (event.getAuthor().isBot() || event.getAuthor().isSystem())
//            return;
//
//        final User MEMBER = event.getAuthor();
//        final Message MESSAGE = event.getMessage();
//
//        String CONTENT = MESSAGE.getContentRaw();
//
//        final String[] args = CONTENT.split("\\s+");
//
//        boolean isAdmin = Callerphone.storage.isAdmin(event.getAuthor().getId());
//
//        if (CONTENT.startsWith(Callerphone.config.getPrefix() + "help mod")) {
//            String TITLE = "Mod";
//            String DESC = "You do not have permission to access this category.";
//
//            if (isAdmin) {
//                DESC = CommandListener.adminHelp() + "\n"
//                        + CommandListener.blacklistHelp() + "\n"
//                        + CommandListener.supportHelp() + "\n"
//                        + CommandListener.showItemsHelp();
//            }
//
//            EmbedBuilder HelpEmd = new EmbedBuilder()
//                    .setTitle(TITLE)
//                    .setDescription(DESC)
//                    .setFooter("Hope you found this useful!", Callerphone.jda.getSelfUser().getAvatarUrl())
//                    .setColor(new Color(114, 137, 218));
//
//            sendPrivateEmbed(MEMBER, HelpEmd.build());
//            return;
//        }
//
//        if (CONTENT.toLowerCase().startsWith(Callerphone.config.getPrefix())) {
//            try {
//
//                switch (args[0].toLowerCase().replace(Callerphone.config.getPrefix(), "")) {
//
//                    case "blackedlist":
//                        sendPrivateFile(MEMBER, new File(Callerphone.parent + "/blacklist.txt"), "Callerphone Blacklist:");
//                        return;
//
//                    case "prefixlist":
//                        sendPrivateFile(MEMBER, new File(Callerphone.parent + "/prefix.txt"), "Callerphone Prefix list:");
//                        return;
//
//                    case "infolist":
//                        sendPrivateFile(MEMBER, new File(Callerphone.parent + "/info.txt"), "Callerphone Info list:");
//                        return;
//
//                    case "modlist":
//                        sendPrivateFile(MEMBER, new File(Callerphone.parent + "/admin.txt"), "Callerphone Moderator list:");
//                        return;
//
//                    case "filterlist":
//                        sendPrivateFile(MEMBER, new File(Callerphone.parent + "/filter.txt"), "Callerphone Filter list:");
//                        return;
//
//                }
//
//                String id = args[1];
//                switch (args[0].toLowerCase().replace(Callerphone.config.getPrefix(), "")) {
//
//                    case "blacklist":
//                        if (Callerphone.storage.isBlacklisted(id)) {
//                            MESSAGE.reply("ID blacklisted already").queue();
//                        } else {
//                            Callerphone.storage.addBlacklist(id);
//                        }
//                        break;
//
//                    case "prefix":
//                        if (Callerphone.prefix.containsKey(id)) {
//                            MESSAGE.reply("ID has prefix already (" + Callerphone.prefix.get(id) + ")").queue();
//                        } else {
//                            String prefix = args[2];
//                            if (prefix.length() > 15) {
//                                MESSAGE.reply("Prefix too long (max. length is 15 chars)").queue();
//                                break;
//                            }
//                            Callerphone.prefix.put(id, prefix);
//                            MESSAGE.reply("ID: `" + id + "` now has prefix `" + prefix + "`").queue();
//                        }
//                        break;
//
//                    case "mod":
//                        if (Callerphone.admin.contains(id)) {
//                            MESSAGE.reply("ID is mod already").queue();
//                        } else {
//                            Callerphone.storage.addAdmin(id);
//                            MESSAGE.reply("ID: `" + id + "` added to mod list").queue();
//                        }
//                        break;
//
//
//                    case "rblacklist":
//                        if (!Callerphone.blacklist.contains(id)) {
//                            MESSAGE.reply("ID not blacklisted").queue();
//                        } else {
//                            Callerphone.blacklist.remove(id);
//                            MESSAGE.reply("ID: `" + id + "` removed from blacklist").queue();
//                        }
//                        break;
//
//                    case "rprefix":
//                        if (!Callerphone.prefix.containsKey(id)) {
//                            MESSAGE.reply("ID does not have a prefix").queue();
//                        } else {
//                            Callerphone.prefix.remove(id);
//                            MESSAGE.reply("ID: `" + id + "` no longer has a prefix").queue();
//                        }
//                        break;
//
//                    case "rmod":
//                        if (!Callerphone.admin.contains(id)) {
//                            MESSAGE.reply("ID is not a mod").queue();
//                        } else {
//                            if (id.equals(Callerphone.config.getOwnerID())) {
//                                MESSAGE.reply("You cannot remove this mod").queue();
//                                break;
//                            }
//                            Callerphone.admin.remove(id);
//                            MESSAGE.reply("ID: `" + id + "` removed from mod list").queue();
//                        }
//                        break;
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                MESSAGE.reply("Syntax Error").queue();
//            }
//        }
//    }
//
//    public void sendPrivateFile(User user, File file, String title) {
//        user.openPrivateChannel().queue((channel) ->
//                channel.sendFile(file, title + ".txt", AttachmentOption.SPOILER).queue());
//    }
//
//    public void sendPrivateEmbed(User user, MessageEmbed embed) {
//            user.openPrivateChannel().queue((channel) ->
//                    channel.sendMessageEmbeds(embed).queue()
//            );
//    }
//}
