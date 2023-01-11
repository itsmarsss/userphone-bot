package com.marsss.callerphone.tccallerphone;

import java.util.Queue;

public class ConvoStorage {
    private final Queue<String> messages;
    private String callerTCID;

    private String receiverTCID;

    private long callerLastMessage;
    private long receiverLastMessage;

    private boolean callerFamilyFriendly;
    private boolean receiverFamilyFriendly;

    private boolean callerAnonymous;
    private boolean receiverAnonymous;

    private boolean report;

    public ConvoStorage(Queue<String> messages, String callerTCID, String receiverTCID, long callerLastMessage, long receiverLastMessage, boolean callerFamilyFriendly, boolean receiverFamilyFriendly, boolean callerAnonymous, boolean receiverAnonymous, boolean report) {
        this.messages = messages;
        this.callerTCID = callerTCID;
        this.receiverTCID = receiverTCID;
        this.callerLastMessage = callerLastMessage;
        this.receiverLastMessage = receiverLastMessage;
        this.callerFamilyFriendly = callerFamilyFriendly;
        this.receiverFamilyFriendly = receiverFamilyFriendly;
        this.callerAnonymous = callerAnonymous;
        this.receiverAnonymous = receiverAnonymous;
        this.report = report;
    }

    // Get

    public Queue<String> getMessages() {
        return messages;
    }

    public String getCallerTCID() {
        return callerTCID;
    }

    public String getReceiverTCID() {
        return receiverTCID;
    }

    public long getCallerLastMessage() {
        return callerLastMessage;
    }
    public long getReceiverLastMessage() {
        return receiverLastMessage;
    }

    public boolean getCallerFamilyFriendly() {
        return callerFamilyFriendly;
    }

    public boolean getReceiverFamilyFriendly() {
        return receiverFamilyFriendly;
    }

    public boolean getCallerAnonymous() {
        return callerAnonymous;
    }

    public boolean getReceiverAnonymous() {
        return receiverAnonymous;
    }

    public boolean getReport() {
        return report;
    }

    // Set

    public void addMessage(String message) {
        messages.add(message);
    }

    public void setCallerTCID(String ID) {
        callerTCID = ID;
    }

    public void setReceiverTCID(String ID) {
        receiverTCID = ID;
    }

    public void setCallerLastMessage(long time) {
        callerLastMessage = time;
    }
    public void setReceiverLastMessage(long time) {
        receiverLastMessage = time;
    }

    public void setCallerFamilyFriendly(boolean ccf) {
        callerFamilyFriendly = ccf;
    }

    public void setReceiverFamilyFriendly(boolean rff) {
        receiverFamilyFriendly = rff;
    }

    public void setCallerAnonymous(boolean canon) {
        callerAnonymous = canon;
    }

    public void setReceiverAnonymous(boolean ranon) {
        receiverAnonymous = ranon;
    }

    public void setReport(boolean rep) {
        report = rep;
    }

    public void resetMessage() {
        messages.clear();
        callerTCID = "empty";
        receiverTCID = "";
        callerLastMessage = 0;
        receiverLastMessage = 0;
        callerFamilyFriendly = true;
        receiverFamilyFriendly = true;
        callerAnonymous = false;
        receiverAnonymous = false;
        report = false;
    }

}
