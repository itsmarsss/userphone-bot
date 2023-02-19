package com.marsss.callerphone;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolSet {
    public static final JDA jda = Callerphone.jda;
    public static final String CP_EMJ = Callerphone.Callerphone;



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

        final TextChannel CHANNEL = jda.getTextChannelById(idL);

        if (CHANNEL == null)
            return null;

        if (!CHANNEL.getGuild().getSelfMember().hasPermission(CHANNEL, Permission.MESSAGE_WRITE)) {
            return null;
        }

        return CHANNEL;
    }

    public static String messageCheck(String messageRaw) {
        if(messageRaw.contains("@here") || messageRaw.contains("@everyone"))
            return Response.ATTEMPTED_PING.toString();

        if(hasLink(messageRaw))
            return Response.ATTEMPTED_LINK.toString();

        if (messageRaw.length() > 1500)
            return Response.MESSAGE_TOO_LONG.toString();

        return messageRaw;
    }

    public static boolean hasLink(String msg) {
        LinkedList<String> links = new LinkedList<>();
        String regexString = "\\b(https://|www[.])[A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        Pattern pattern = Pattern.compile(regexString,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(msg);
        while (matcher.find()) {
            links.add(msg.substring(matcher.start(0),matcher.end(0)));
        }

        return links.size()!=0;
    }
}
