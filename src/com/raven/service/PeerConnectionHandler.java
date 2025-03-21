package com.raven.service;

import com.raven.app.MessageType;
import com.raven.model.Model_File_Sender;
import com.raven.model.Model_Send_Message;
import com.raven.model.PeerInfo;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class PeerConnectionHandler implements Runnable {
    private PeerInfo peer;
    private Object message;

    public PeerConnectionHandler(PeerInfo peer, Model_Send_Message message) {
        this.peer = peer;
        this.message = message;
    }

    public PeerConnectionHandler(PeerInfo peer, JSONObject message) {
        this.peer = peer;
        this.message = message;
    }

    @Override
    public void run() {
        try (Socket peerSocket = new Socket(peer.getAddress(), peer.getPort());
            DataOutputStream out = new DataOutputStream(peerSocket.getOutputStream())) {
            System.out.println("Send Message: " + message);

            if (message instanceof Model_Send_Message) {

                Model_Send_Message msg = (Model_Send_Message) message;
                if (msg.getFile() == null) {
                    out.writeUTF(msg.toJsonObject().toString());
                    out.flush();
                }

                if (msg.getFile() != null) {
                    Model_File_Sender fileSender = msg.getFile();
                    sendFile(fileSender.getFile().getAbsolutePath(), out, msg.getSender(), ((Model_Send_Message) message).getType());
                }
            } else if (message instanceof JSONObject) {
                // Handle JSONObject directly
                JSONObject jsonMsg = (JSONObject) message;
                out.writeUTF(jsonMsg.toString());
                out.flush();
                System.out.println("Sent JSON message: " + jsonMsg.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(String filePath, DataOutputStream out, String sender, MessageType type) {
        try (FileInputStream fileInput = new FileInputStream(new File(filePath))) {
            File file = new File(filePath);
            System.out.println(filePath);
            JSONObject metadata = new JSONObject();
            metadata.put("type", type);
            metadata.put("sender", sender);
            metadata.put("fileSize", file.length());
            metadata.put("content", file.getName());
            System.out.println(metadata.toString());
            out.writeUTF(metadata.toString());
            out.flush();

            byte[] buffer = new byte[1024 * 64];
            int bytesRead;
            while ((bytesRead = fileInput.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
//                System.out.println(bytesRead);
            }
            out.flush();
            System.out.println("File sent successfully: " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}