package com.raven.service;

import com.raven.component.Chat_Body;
import com.raven.form.Chat;
import com.raven.form.Menu_Left;
import com.raven.model.PeerInfo;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class PeerListener implements Runnable {
    private int port;
    private Map<String, PeerInfo> peerList;
    private Menu_Left menuLeft;
    private Chat chat;

    public PeerListener(int port, Map<String, PeerInfo> peerList, Menu_Left menuLeft, Chat chat) {
        this.port = port;
        this.peerList = peerList;
        this.menuLeft = menuLeft;
        this.chat = chat;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket peerSocket = serverSocket.accept();
                new Thread(new PeerMessageHandler(peerList, peerSocket, menuLeft, chat)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
