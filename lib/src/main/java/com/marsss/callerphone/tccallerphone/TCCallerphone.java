package com.marsss.callerphone.tccallerphone;

import com.marsss.callerphone.Callerphone;
import com.marsss.callerphone.ToolSet;
import com.marsss.callerphone.tccallerphone.entities.ConversationStorage;
import com.marsss.callerphone.tccallerphone.entities.MessageStorage;
import com.marsss.database.categories.Chats;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.utils.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TCCallerphone {

    public static final List<ConversationStorage> queue = new ArrayList<>();
    public static final Map<String, ConversationStorage> conversationMap = new HashMap<>();

    public static ChatStatus onCallCommand(MessageChannelUnion tcchannel, boolean anon) {
        final Logger logger = LoggerFactory.getLogger(TCCallerphone.class);
        final String CHANNELID = tcchannel.getId();

        if (queue.isEmpty()) {
            ConversationStorage convo = new ConversationStorage();

            convo.setCallerAnonymous(anon);
            convo.setCallerTCId(CHANNELID);
            queue.add(convo);

            return ChatStatus.SUCCESS_CALLER;
        }

        ConversationStorage convo = queue.get(0);
        queue.remove(0);

        convo.setReceiverAnonymous(anon);
        convo.setReceiverTCId(CHANNELID);

        convo.setStarted(Instant.now().getEpochSecond());
        convo.setCallerLastMessage(System.currentTimeMillis());
        convo.setReceiverLastMessage(System.currentTimeMillis());

        final TextChannel CALLER_CHANNEL = ToolSet.getTextChannel(convo.getCallerTCId());
        final TextChannel RECEIVER_CHANNEL = ToolSet.getTextChannel(convo.getReceiverTCId());

        if (CALLER_CHANNEL == null || RECEIVER_CHANNEL == null) {
            convo.resetMessage();
            return ChatStatus.NON_EXISTENT;
        }

        conversationMap.put(convo.getCallerTCId(), convo);
        conversationMap.put(convo.getReceiverTCId(), convo);

        CALLER_CHANNEL.sendMessage(ChatResponse.PICKED_UP.toString()).queue();

        logger.info("From Channel: {} - To Channel: {}", convo.getCallerTCId(), convo.getReceiverTCId());
        logger.info("From Guild: {} - To Guild: {}", CALLER_CHANNEL.getGuild().getId(), RECEIVER_CHANNEL.getGuild().getId());

        return ChatStatus.SUCCESS_RECEIVER;
    }

    public static String onEndCallCommand(MessageChannelUnion channel) {
        if (!hasCall(channel.getId())) {
            return ChatResponse.NO_CALL.toString();
        }

        ConversationStorage convo = getCall(channel.getId());

        if (convo == null) {
            return ChatResponse.NO_CALL.toString();
        }

        final String CALLER_ID = convo.getCallerTCId();
        final String RECEIVER_ID = convo.getReceiverTCId();

        final TextChannel CALLER_CHANNEL = ToolSet.getTextChannel(CALLER_ID);
        final TextChannel RECEIVER_CHANNEL = ToolSet.getTextChannel(RECEIVER_ID);

        if (RECEIVER_ID.equals(channel.getId())) {
            if (!convo.getCallerTCId().equals("empty")) {
                if (CALLER_CHANNEL != null) {
                    CALLER_CHANNEL.sendMessage(ChatResponse.OTHER_PARTY_HUNG_UP.toString()).queue();
                }
            }
        } else {
            if (!convo.getReceiverTCId().isEmpty()) {
                if (RECEIVER_CHANNEL != null) {
                    RECEIVER_CHANNEL.sendMessage(ChatResponse.OTHER_PARTY_HUNG_UP.toString()).queue();
                }
            }
        }

        convo.setEnded(Instant.now().getEpochSecond());

        final boolean report = convo.getReport();

        log(convo, CALLER_ID, RECEIVER_ID);
        Chats.createChat(convo);

        if (report) {
            report(convo, CALLER_ID, RECEIVER_ID);
        }

        conversationMap.remove(convo.getCallerTCId());
        conversationMap.remove(convo.getReceiverTCId());

        return ChatResponse.HUNG_UP.toString();
    }

    private static void log(ConversationStorage convo, String callerID, String receiverID) {
        List<MessageStorage> data = convo.getMessages();

        StringBuilder dataString = new StringBuilder();
        for (MessageStorage m : data)
            dataString.append(m).append("\n");


        final TextChannel TEMP_CHANNEL = ToolSet.getTextChannel(Callerphone.config.getTempChatChannel());
        if (TEMP_CHANNEL != null) {
            TEMP_CHANNEL.sendMessage("**ID:** " + convo.getId())
                    .addFiles(FileUpload.fromData(dataString.toString().getBytes(), convo.getId() + ".txt")).queue();
        }
    }

    private static void report(ConversationStorage convo, String callerID, String receiverID) {
        List<MessageStorage> data = convo.getMessages();

        StringBuilder dataString = new StringBuilder();
        for (MessageStorage m : data)
            dataString.append(m).append("\n");


        final TextChannel REPORT_CHANNEL = ToolSet.getTextChannel(Callerphone.config.getReportChatChannel());
        if (REPORT_CHANNEL != null) {
            REPORT_CHANNEL.sendMessage("**ID:** " + convo.getId())
                    .addFiles(FileUpload.fromData(dataString.toString().getBytes(), convo.getId() + ".txt")).queue();
        }
    }

    public static ConversationStorage getCall(String tc) {
        return conversationMap.getOrDefault(tc, null);
    }

    public static boolean hasCall(String tc) {
        return conversationMap.containsKey(tc);
    }

}
