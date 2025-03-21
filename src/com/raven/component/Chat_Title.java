package com.raven.component;

import com.raven.app.MessageType;
import com.raven.call.CallManager;
import com.raven.model.MyPeer;
import com.raven.model.PeerInfo;
import com.raven.service.PeerConnectionHandler;
import com.raven.utils.Utilities;
import org.json.JSONObject;

import javax.swing.*;

public class Chat_Title extends javax.swing.JPanel {
    private PeerInfo peerInfo;
    private Utilities utilities;

    public Chat_Title(PeerInfo peerInfo) {
        initComponents();
        this.peerInfo = peerInfo;
        this.utilities = new Utilities();
        lbName.setText(peerInfo.getName());

        btnCall.addActionListener(e -> {
            String receiverIP = peerInfo.getAddress();
            int receiverPort = utilities.findAvailablePort();
            int listenPort = utilities.findAvailablePort();

            JSONObject message = new JSONObject();
            message.put("type", MessageType.CALL_START);
            message.put("sender", MyPeer.getInstance().getPeerName());
            message.put("senderAddress", MyPeer.getInstance().getPeerAddress());
            message.put("receiver", receiverIP);
            message.put("receiverPort", receiverPort);
            message.put("listenPort", listenPort);

            new Thread(new PeerConnectionHandler(peerInfo, message)).start();

            CallManager.getInstance().startOutgoingCall(peerInfo, message);
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        layer = new javax.swing.JLayeredPane();
        lbName = new javax.swing.JLabel();
        lbStatus = new javax.swing.JLabel();
        btnCall = new javax.swing.JButton();
        btnVideoCall = new javax.swing.JButton();

        setBackground(new java.awt.Color(229, 229, 229));

        layer.setLayout(new java.awt.GridLayout(0, 1));

        lbName.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbName.setForeground(new java.awt.Color(66, 66, 66));
        lbName.setText("Name");
        layer.add(lbName);

        lbStatus.setForeground(new java.awt.Color(40, 147, 59));
        lbStatus.setText("Active now");
        layer.add(lbStatus);

        btnCall.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/call.png"))); // Đường dẫn đến icon
        btnCall.setContentAreaFilled(false);
        btnCall.setBorderPainted(false);

        btnVideoCall.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/video_camera.png"))); // Đường dẫn đến icon
        btnVideoCall.setContentAreaFilled(false);
        btnVideoCall.setBorderPainted(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(layer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE) // Điều chỉnh khoảng cách
                                .addComponent(btnCall, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnVideoCall, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(layer, javax.swing.GroupLayout.PREFERRED_SIZE, 34, Short.MAX_VALUE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(btnCall, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                                                .addComponent(btnVideoCall, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)))
                                .addGap(3, 3, 3))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane layer;
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JButton btnCall;
    private javax.swing.JButton btnVideoCall;
    // End of variables declaration//GEN-END:variables
}
