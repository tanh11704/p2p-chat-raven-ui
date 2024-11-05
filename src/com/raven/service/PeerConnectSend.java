package com.raven.service;

import com.raven.model.Model_Connect_Message;
import com.raven.model.PeerInfo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PeerConnectSend implements Runnable{
    private PeerInfo peer;
    private Model_Connect_Message message;

    public PeerConnectSend(PeerInfo peer, Model_Connect_Message message) {
        this.peer = peer;
        this.message = message;
    }

    @Override
    public void run() {
        try (Socket peerSocket = new Socket(peer.getAddress(), peer.getPort());
             DataOutputStream out = new DataOutputStream(peerSocket.getOutputStream())) {
            System.out.println(message);
            if (message != null) {
                out.writeUTF(message.toJsonObject().toString());
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
