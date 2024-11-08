package com.raven.service;

import com.raven.app.MessageType;
import com.raven.form.Chat;
import com.raven.form.Menu_Left;
import com.raven.model.Model_Connect_Message;
import com.raven.model.Model_Send_Message;
import com.raven.model.MyPeer;
import com.raven.model.PeerInfo;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectReceive implements Runnable {
    private Map<String, PeerInfo> peerList;
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
    }

    @Override
    public void run() {
        MulticastSocket multicastSocket = null;
        try {
            multicastSocket = new MulticastSocket(9999);
            InetAddress multicastGroup = InetAddress.getByName("230.0.0.1");
            multicastSocket.joinGroup(multicastGroup);

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                multicastSocket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                processMessage(message, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (multicastSocket != null && !multicastSocket.isClosed()) {
                try {
                    InetAddress multicastGroup = InetAddress.getByName("230.0.0.1");
                    multicastSocket.leaveGroup(multicastGroup); // Rời khỏi nhóm
                    multicastSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processMessage(String message, DatagramPacket packet) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            MessageType type = MessageType.valueOf(jsonObject.getString("type"));
            String name = jsonObject.getString("name");
            String address = packet.getAddress().getHostAddress();
            System.out.println(address);
            int port = jsonObject.getInt("port");

            switch (type) {
                case CONNECT -> handleConnect(address, name, port);
                case DISCONNECT -> handleDisconnect(address, name, port);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void handleConnect(String address, String name, int port) {
        if (
                peerList.containsKey(name)
                        || (name.equals(currentPeerName) && address.equals(currentPeerAddress) && port == currentPeerPort
                        || MyPeer.getInstance().getPeerName().equals(name)
                )) {
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