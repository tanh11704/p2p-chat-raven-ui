package com.raven.call;

import com.raven.app.MessageType;
import com.raven.model.MyPeer;
import com.raven.model.PeerInfo;
import com.raven.service.PeerConnectionHandler;
import org.json.JSONObject;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class Call_Panel extends JPanel {
    private PeerInfo peerInfo;
    private JFrame callFrame;
    private DatagramSocket audioSocket;
    private Thread audioSendThread;
    private Thread audioReceiveThread;
    private AtomicBoolean isCalling = new AtomicBoolean(false);
    private int localPort;
    private int remotePort;
    private String remoteAddress;

    // UI Components
    private JLabel callerNameLabel;
    private JLabel callStatusLabel;
    private JButton endCallButton;
    private JButton muteButton;
    private boolean isMuted = false;

    public Call_Panel(PeerInfo peerInfo, boolean isIncoming, int localPort, int remotePort, String remoteAddress) {
        this.peerInfo = peerInfo;
        this.localPort = localPort;
        this.remotePort = remotePort;
        this.remoteAddress = remoteAddress;

        initComponents();
        setupCallFrame(isIncoming);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 250));
        setBackground(new Color(245, 245, 245));

        // Create top panel with caller info
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(229, 229, 229));

        callerNameLabel = new JLabel(peerInfo.getName());
        callerNameLabel.setFont(new Font("sansserif", Font.BOLD, 18));
        callerNameLabel.setForeground(new Color(66, 66, 66));

        callStatusLabel = new JLabel("Connecting...");
        callStatusLabel.setForeground(new Color(40, 147, 59));

        topPanel.add(callerNameLabel);
        topPanel.add(callStatusLabel);

        // Create center panel with call avatar
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(new Color(245, 245, 245));

        // You can add an avatar image here
        JLabel avatarLabel = new JLabel();
        avatarLabel.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/profile.png")));
        avatarLabel.setPreferredSize(new Dimension(100, 100));
        centerPanel.add(avatarLabel);

        // Create bottom panel with controls
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(new Color(229, 229, 229));

        endCallButton = new JButton("End Call");
        endCallButton.setBackground(new Color(220, 53, 69));
        endCallButton.setForeground(Color.WHITE);
        endCallButton.setFocusPainted(false);
        endCallButton.addActionListener(e -> endCall());

        muteButton = new JButton("Mute");
        muteButton.setBackground(new Color(52, 58, 64));
        muteButton.setForeground(Color.WHITE);
        muteButton.setFocusPainted(false);
        muteButton.addActionListener(e -> toggleMute());

        bottomPanel.add(muteButton);
        bottomPanel.add(endCallButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupCallFrame(boolean isIncoming) {
        callFrame = new JFrame(isIncoming ? "Incoming Call" : "Outgoing Call");
        callFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        callFrame.getContentPane().add(this);
        callFrame.pack();
        callFrame.setLocationRelativeTo(null);
        callFrame.setResizable(false);

        if (isIncoming) {
            // Add accept and reject buttons for incoming calls
            JPanel incomingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            incomingPanel.setBackground(new Color(245, 245, 245));

            JButton acceptButton = new JButton("Accept");
            acceptButton.setBackground(new Color(40, 167, 69));
            acceptButton.setForeground(Color.WHITE);
            acceptButton.addActionListener(e -> acceptCall());

            JButton rejectButton = new JButton("Reject");
            rejectButton.setBackground(new Color(220, 53, 69));
            rejectButton.setForeground(Color.WHITE);
            rejectButton.addActionListener(e -> rejectCall());

            incomingPanel.add(acceptButton);
            incomingPanel.add(rejectButton);
            add(incomingPanel, BorderLayout.CENTER);

            callStatusLabel.setText("Incoming call...");
        } else {
            callStatusLabel.setText("Calling...");
        }
    }

    public void showCallFrame() {
        callFrame.setVisible(true);
    }

    private void acceptCall() {
        callStatusLabel.setText("Connected");

        // Send call accept message
        JSONObject message = new JSONObject();
        message.put("type", MessageType.CALL_ACCEPT);
        message.put("sender", MyPeer.getInstance().getPeerName());
        message.put("receiver", peerInfo.getAddress());
        message.put("listenPort", localPort);

        new Thread(new PeerConnectionHandler(peerInfo, message)).start();

        // Start audio streaming
        startCall();

        // Remove accept/reject buttons
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JPanel && !(component instanceof JPanel)) {
                remove(component);
            }
        }
        revalidate();
        repaint();
    }

    private void rejectCall() {
        // Send call reject message
        JSONObject message = new JSONObject();
        message.put("type", MessageType.CALL_REJECT);
        message.put("sender", MyPeer.getInstance().getPeerName());
        message.put("receiver", peerInfo.getAddress());

        new Thread(new PeerConnectionHandler(peerInfo, message)).start();

        callFrame.dispose();
    }

    private void endCall() {
        // Send call end message
        JSONObject message = new JSONObject();
        message.put("type", MessageType.CALL_END);
        message.put("sender", MyPeer.getInstance().getPeerName());
        message.put("receiver", peerInfo.getAddress());

        new Thread(new PeerConnectionHandler(peerInfo, message)).start();

        stopCall();
        callFrame.dispose();
    }

    private void toggleMute() {
        isMuted = !isMuted;
        muteButton.setText(isMuted ? "Unmute" : "Mute");
    }

    public void startCall() {
        isCalling.set(true);

        try {
            audioSocket = new DatagramSocket(localPort);
            startAudioStreaming();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(callFrame,
                    "Failed to establish call: " + e.getMessage(),
                    "Call Error", JOptionPane.ERROR_MESSAGE);
            endCall();
        }
    }

    public void stopCall() {
        isCalling.set(false);

        if (audioSendThread != null) {
            audioSendThread.interrupt();
        }

        if (audioReceiveThread != null) {
            audioReceiveThread.interrupt();
        }

        if (audioSocket != null && !audioSocket.isClosed()) {
            audioSocket.close();
        }
    }

    private void startAudioStreaming() {
        // Start audio send thread
        audioSendThread = new Thread(() -> {
            try {
                AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

                if (!AudioSystem.isLineSupported(info)) {
                    JOptionPane.showMessageDialog(callFrame, "Audio line not supported",
                            "Audio Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
                microphone.open(format);
                microphone.start();

                InetAddress receiverAddress = InetAddress.getByName(remoteAddress);
                byte[] buffer = new byte[1024];

                while (isCalling.get()) {
                    if (!isMuted) {
                        int bytesRead = microphone.read(buffer, 0, buffer.length);

                        if (bytesRead > 0) {
                            DatagramPacket packet = new DatagramPacket(
                                    buffer, bytesRead, receiverAddress, remotePort);
                            audioSocket.send(packet);
                        }
                    }

                    Thread.sleep(10);
                }

                microphone.close();
            } catch (Exception e) {
                if (isCalling.get()) {
                    e.printStackTrace();
                }
            }
        });

        // Start audio receive thread
        audioReceiveThread = new Thread(() -> {
            try {
                AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

                if (!AudioSystem.isLineSupported(info)) {
                    JOptionPane.showMessageDialog(callFrame, "Audio line not supported",
                            "Audio Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                SourceDataLine speakers = (SourceDataLine) AudioSystem.getLine(info);
                speakers.open(format);
                speakers.start();

                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                while (isCalling.get()) {
                    audioSocket.receive(packet);
                    speakers.write(packet.getData(), 0, packet.getLength());
                }

                speakers.close();
            } catch (Exception e) {
                if (isCalling.get()) {
                    e.printStackTrace();
                }
            }
        });

        audioSendThread.start();
        audioReceiveThread.start();
    }
}
