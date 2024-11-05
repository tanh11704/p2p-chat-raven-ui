package com.raven.model;

import org.json.JSONObject;

public class Model_Connect_Message {
    private String type;
    private String name;
    private String address;
    private int port;

    public Model_Connect_Message() {
    }

    public Model_Connect_Message(String type, String name, String address, int port) {
        this.type = type;
        this.name = name;
        this.address = address;
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("name", name);
        jsonObject.put("address", address);
        jsonObject.put("port", port);
        return jsonObject;
    }

    @Override
    public String toString() {
        return toJsonObject().toString();
    }
}
