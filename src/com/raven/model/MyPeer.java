package com.raven.model;

public class MyPeer {
    private static MyPeer instance;
    private String peerName;
    private String peerAddress;
    private int peerPort;

    public static MyPeer getInstance() {
        if (instance == null) {
            instance = new MyPeer();
        }
        return instance;
    }

    public MyPeer() {
    }

    public String getPeerName() {
        return peerName;
    }

    public void setPeerName(String peerName) {
        this.peerName = peerName;
    }

    public int getPeerPort() {
        return peerPort;
    }

    public void setPeerPort(int peerPort) {
        this.peerPort = peerPort;
    }

    public String getPeerAddress() {
        return peerAddress;
    }

    public void setPeerAddress(String peerAddress) {
        this.peerAddress = peerAddress;
    }
}
