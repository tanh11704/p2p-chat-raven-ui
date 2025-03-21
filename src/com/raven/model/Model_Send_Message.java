package com.raven.model;

import com.raven.app.MessageType;
import org.json.JSONException;
import org.json.JSONObject;

public class Model_Send_Message {
    private MessageType type;
    private String sender;
    private String content;
    private Model_File_Sender file;

    public Model_Send_Message() {
    }

    public Model_Send_Message(String content, Model_File_Sender file, String sender, MessageType type) {
        this.content = content;
        this.file = file;
        this.sender = sender;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Model_File_Sender getFile() {
        return file;
    }

    public void setFile(Model_File_Sender file) {
        this.file = file;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public JSONObject toJsonObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("type", type.toString());
            json.put("sender", sender);
            json.put("content", content);

            if (file != null) {
                json.put("fileSize", file.getFileSize());
            }

            return json;
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        JSONObject json = toJsonObject();
        return json != null ? json.toString() : "{}";
    }
}
