package com.raven.service;

import com.raven.app.MessageType;
import com.raven.component.Chat_Body;
import com.raven.form.Chat;
import com.raven.form.Menu_Left;
import com.raven.model.Model_Receive_Message;
import com.raven.model.PeerInfo;
import org.json.JSONObject;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class PeerMessageHandler implements Runnable {
    private Socket peerSocket;
    private Map<String, PeerInfo> peerList;
    private Menu_Left menuLeft;
    private Chat chat;

    public PeerMessageHandler(Map<String, PeerInfo> peerList, Socket peerSocket, Menu_Left menuLeft, Chat chat) {
        this.peerList = peerList;
        this.peerSocket = peerSocket;
        this.menuLeft = menuLeft;
        this.chat = chat;
    }

    @Override
    public void run() {
        try (DataInputStream in = new DataInputStream(peerSocket.getInputStream())) {
            String message;
            while (true) {
                try {
                    if (in.available() > 0) {
                        message = in.readUTF();
                        System.out.println(message);
                        JSONObject jsonObject = new JSONObject(message);
                        MessageType type = MessageType.valueOf(jsonObject.getString("type"));
                        switch (type) {
                            case TEXT, VIDEO, VOICE, IMAGE, FILE, EMOJI -> {
                                Model_Receive_Message model = new Model_Receive_Message().fromJsonObject(jsonObject);
                                String sender = model.getSender();
                                SwingUtilities.invokeLater(() -> {
                                    Chat_Body chatBody = chat.getChatBodyByUser(sender);
                                    if (chatBody != null) {
                                        chatBody.addItemLeft(model);
                                    }
                                });
                            }
                            case CONNECT -> {
                                String name = jsonObject.getString("name");
                                String address = jsonObject.getString("address");
                                int port = jsonObject.getInt("port");
                                PeerInfo peer = new PeerInfo(address, name, port);
                                peerList.put(name, peer);
                                SwingUtilities.invokeLater(() -> {
                                    menuLeft.addPeer(peer);
                                    chat.addChatPanelForUser(peer);
                                });
                            }
                        }
                    }
                } catch (EOFException eof) {
                    System.out.println("Connection closed by peer.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                peerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
