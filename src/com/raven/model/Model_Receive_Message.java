package com.raven.model;

import com.raven.app.MessageType;
import org.json.JSONException;
import org.json.JSONObject;

public class Model_Receive_Message {
    private String sender;
    private String content;
    private MessageType messageType;
    private Model_File_Receiver file;

    public Model_Receive_Message() {
    }

    public Model_Receive_Message(String sender, String content, MessageType messageType) {
        this.sender = sender;
        this.content = content;
        this.messageType = messageType;
    }

    public Model_Receive_Message(String sender, Model_File_Receiver file, MessageType messageType) {
        this.sender = sender;
        this.file = file;
        this.messageType = messageType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Model_File_Receiver getFile() {
        return file;
    }

    public void setFile(Model_File_Receiver file) {
        this.file = file;
    }

    public JSONObject toJsonObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("type", messageType);
            json.put("sender", sender);
            json.put("content", content);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Model_Receive_Message fromJsonObject(JSONObject json) {
        try {
            String sender = json.getString("sender");
            String content = json.getString("content");
            MessageType messageType = MessageType.valueOf(json.getString("type"));
            return new Model_Receive_Message(sender, content, messageType);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
