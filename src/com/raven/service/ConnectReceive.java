package com.raven.service;

import com.raven.app.MessageType;
import com.raven.form.Chat;
import com.raven.form.Menu_Left;
import com.raven.model.Model_Connect_Message;
import com.raven.model.Model_Send_Message;
import com.raven.model.PeerInfo;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectReceive implements Runnable {
    private Map<String, PeerInfo> peerList;
    private DatagramSocket socket;
    private String currentPeerName;
    private String currentPeerAddress;
    private int currentPeerPort;
    private Menu_Left menuLeft;
    private Chat chat;

    public ConnectReceive(Menu_Left menuLeft, Chat chat, Map<String, PeerInfo> peerList,
                          String currentPeerName, String currentPeerAddress, int currentPeerPort) {
        this.menuLeft = menuLeft;
        this.chat = chat;
        this.peerList = peerList;
        this.currentPeerName = currentPeerName;
        this.currentPeerAddress = currentPeerAddress;
        this.currentPeerPort = currentPeerPort;

        socket = null;
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(null);
            socket.setReuseAddress(true);
            socket.bind(new java.net.InetSocketAddress(9999));

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                processMessage(message);
            }
        } catch (Exception e) {

        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    private void processMessage(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            MessageType type = MessageType.valueOf(jsonObject.getString("type"));
            String name = jsonObject.getString("name");
            String address = jsonObject.getString("address");
            int port = jsonObject.getInt("port");

            switch (type) {
                case CONNECT -> handleConnect(address, name, port);
                case DISCONNECT -> handleDisconnect(address, name, port);
            }
        } catch (JSONException e) {
        } catch (IllegalArgumentException e) {
        }
    }

    private void handleConnect(String address, String name, int port) {
        if (peerList.containsKey(name) || (name.equals(currentPeerName) && address.equals(currentPeerAddress) && port == currentPeerPort)) {
            return;
        }
        PeerInfo peerInfo = new PeerInfo(address, name, port);
        peerList.put(name, peerInfo);
        SwingUtilities.invokeLater(() -> {
            menuLeft.addPeer(peerInfo);
            chat.addChatPanelForUser(peerInfo);
        });
        connectReserve(peerInfo);
    }

    private void handleDisconnect(String address, String name, int port) {
        if (peerList.containsKey(name)) {
            PeerInfo peerInfo = peerList.get(name);
            peerList.remove(name);
            SwingUtilities.invokeLater(() -> {
                menuLeft.removePeer(peerInfo);
                chat.removeChatPanelForUser(peerInfo);
            });
        }
    }

    private void connectReserve(PeerInfo peerInfo) {

        Model_Connect_Message connectMessage = new Model_Connect_Message(MessageType.CONNECT.toString(),
                currentPeerName, currentPeerAddress, currentPeerPort);

        new Thread(new PeerConnectSend(peerInfo, connectMessage)).start();
    }
}