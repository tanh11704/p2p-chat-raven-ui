package com.raven.service;

import com.raven.app.MessageType;
import com.raven.call.CallManager;
import com.raven.component.Chat_Body;
import com.raven.form.Chat;
import com.raven.form.Menu_Left;
import com.raven.model.Model_Receive_Message;
import com.raven.model.MyPeer;
import com.raven.model.PeerInfo;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
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

                        if (!message.trim().startsWith("{")) {
                            System.err.println("Received invalid JSON: " + message);
                            continue;
                        }

                        JSONObject jsonObject = new JSONObject(message);
                        System.out.println(jsonObject);
                        MessageType type = MessageType.valueOf(jsonObject.getString("type"));
                        switch (type) {
                            case TEXT, EMOJI -> {
                                Model_Receive_Message model = new Model_Receive_Message().fromJsonObject(jsonObject);
                                String sender = model.getSender();
                                SwingUtilities.invokeLater(() -> {
                                    Chat_Body chatBody = chat.getChatBodyByUser(sender);
                                    if (chatBody != null) {
                                        chatBody.addItemLeft(model);
                                    }
                                });
                            }
                            case VIDEO, VOICE, IMAGE, FILE -> {
                                handleFileMessage(jsonObject, in, "received_files/");
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
                            case CALL_START -> {
                                handleIncomingCall(jsonObject);
                            }
                            case CALL_END -> {
                                handleCallEnded(jsonObject);
                            }
                            case CALL_ACCEPT -> {
                                handleCallAccepted(jsonObject);
                            }
                            case CALL_REJECT -> {
                                handleCallRejected(jsonObject);
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

    private void handleIncomingCall(JSONObject message) {
        String sender = message.getString("sender");
        String senderAddress = message.getString("senderAddress");
        int receiverPort = message.getInt("receiverPort");
        int listenPort = message.getInt("listenPort");

        // Create a PeerInfo for the caller
        PeerInfo senderInfo = new PeerInfo(senderAddress, sender, 0); // Port not needed for display

        // Handle the incoming call
        CallManager.getInstance().handleIncomingCall(senderInfo, message);
    }

    private void handleCallAccepted(JSONObject message) {
        String sender = message.getString("sender");
        int listenPort = message.getInt("listenPort");

        // Inform CallManager that the call was accepted
        CallManager.getInstance().handleCallAccepted(sender, listenPort);
    }

    private void handleCallRejected(JSONObject message) {
        String sender = message.getString("sender");

        // Kết thúc cuộc gọi vì bị từ chối
        CallManager.getInstance().handleCallRejected(sender);
    }

    private void handleCallEnded(JSONObject message) {
        String sender = message.getString("sender");

        // Kết thúc cuộc gọi
        CallManager.getInstance().handleCallEnded(sender);
    }

    private void handleFileMessage(JSONObject jsonObject, DataInputStream in, String saveDir) {
        try {
            String sender = jsonObject.getString("sender");
            String filePath = jsonObject.getString("content");
            Integer fileSize = jsonObject.getInt("fileSize");

            String fileName = new File(filePath).getName();
            System.out.println("Receiving file: " + fileName);
            File file = new File(saveDir, fileName);
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                throw new IOException("Failed to create directory: " + file.getParentFile().getAbsolutePath());
            }

            try (FileOutputStream fileOutput = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024 * 64];

                int bytesRead;

                while (fileSize > 0 && (bytesRead = in.read(buffer)) >0) {
                    fileOutput.write(buffer, 0, bytesRead);
                    fileSize -= bytesRead;
//                    System.out.println(bytesRead);
                }
            }

            System.out.println("File received: " + file.getAbsolutePath());
            Model_Receive_Message model = new Model_Receive_Message().fromJsonObject(jsonObject);
            model.setContent(file.getAbsolutePath());

            // Cập nhật giao diện
            SwingUtilities.invokeLater(() -> {
                Chat_Body chatBody = chat.getChatBodyByUser(sender);
                if (chatBody != null) {
                    chatBody.addItemLeft(model);
                }
            });

        } catch (IOException e) {
            System.err.println("Error receiving file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
