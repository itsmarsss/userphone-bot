package com.marsss.callerphone;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolSet {
    public static String CP_EMJ = Callerphone.config.getCallerphoneNormal();
    public static String CP_ERR = Callerphone.config.getCallerphoneError();
    public static String CP_CALL = Callerphone.config.getCallerphoneCall();
    public static long MESSAGE_COOLDOWN = 500;
    public static long CREDIT_COOLDOWN = 15000;

    public static void updateToolSet() {
        CP_EMJ = Callerphone.config.getCallerphoneNormal();
        CP_ERR = Callerphone.config.getCallerphoneError();
        CP_CALL = Callerphone.config.getCallerphoneCall();
        MESSAGE_COOLDOWN = 500;
        CREDIT_COOLDOWN = 15000;
    }


    // https://programming.guide/java/formatting-byte-size-to-human-readable-format.html {

    public static String convert(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        final CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

    // }

    public static TextChannel getTextChannel(String id) {
        if (id.isEmpty()) {
            return null;
        }

        long idL;
        try {
            idL = Long.parseLong(id);
        } catch (Exception e) {
            return null;
        }

        final TextChannel CHANNEL = Callerphone.jda.getTextChannelById(idL);

        if (CHANNEL == null)
            return null;

        if (!CHANNEL.getGuild().getSelfMember().hasPermission(CHANNEL, Permission.MESSAGE_WRITE)) {
            return null;
        }

        return CHANNEL;
    }

    public static String messageCheck(String messageRaw) {
        if (messageRaw.contains("@here") || messageRaw.contains("@everyone"))
            return Response.ATTEMPTED_PING.toString();

        if (hasLink(messageRaw))
            return Response.ATTEMPTED_LINK.toString();

        if (messageRaw.length() > 1500)
            return Response.MESSAGE_TOO_LONG.toString();

        return messageRaw;
    }

    public static boolean hasLink(String msg) {
        LinkedList<String> links = new LinkedList<>();
        String regexString = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
        Pattern pattern = Pattern.compile(regexString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(msg);
        while (matcher.find()) {
            links.add(msg.substring(matcher.start(0), matcher.end(0)));
        }

        return links.size() != 0;
    }

    public static String filter(String messageraw) {
        for (String ftr : Storage.filter) {
            StringBuilder rep = new StringBuilder();
            for (int i = 0; i < ftr.length(); i++) {
                rep.append("#");
            }
            messageraw = messageraw.replaceAll("(?i)" + ftr, rep.toString());
        }
        return messageraw;
    }

    public static void printWelcome() {
        System.out.println("  _____          _      _      ______ _____  _____  _    _  ____  _   _ ______ ");
        System.out.println(" / ____|   /\\   | |    | |    |  ____|  __ \\|  __ \\| |  | |/ __ \\| \\ | |  ____|");
        System.out.println("| |       /  \\  | |    | |    | |__  | |__) | |__) | |__| | |  | |  \\| | |__");
        System.out.println("| |      / /\\ \\ | |    | |    |  __| |  _  /|  ___/|  __  | |  | | . ` |  __|  ");
        System.out.println("| |____ / ____ \\| |____| |____| |____| | \\ \\| |    | |  | | |__| | |\\  | |____ ");
        System.out.println(" \\_____/_/    \\_\\______|______|______|_|  \\_\\_|    |_|  |_|\\____/|_| \\_|______|");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("               ============== PROGRAM SOURCE CODE =============");
        System.out.println("               = https://github.com/itsmarsss/Callerphone-Bot =");
        System.out.println("               ================================================");
        System.out.println("                    Welcome to Callerphone's Control Prompt");
    }
}
