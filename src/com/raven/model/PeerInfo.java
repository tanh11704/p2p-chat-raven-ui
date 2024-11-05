package com.raven.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PeerInfo {
    private String name;
    private String address;
    private int port;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public PeerInfo(String address, String name, int port) {
        this.address = address;
        this.name = name;
        this.port = port;
    }

    public PeerInfo(Object json) {
        JSONObject obj = (JSONObject) json;
        try {
            name = obj.getString("name");
            address = obj.getString("address");
            port = obj.getInt("port");
        } catch (JSONException e) {
            System.err.println(e);
        }
    }

    @Override
    public String toString() {
        return "PeerInfo{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", port=" + port +
                '}';
    }
}