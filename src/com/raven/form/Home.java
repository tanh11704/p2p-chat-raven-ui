package com.raven.form;

import com.raven.app.MessageType;
import com.raven.event.EventMenuLeft;
import com.raven.event.PublicEvent;
import com.raven.model.MyPeer;
import com.raven.model.PeerInfo;
import com.raven.service.ConnectReceive;
import com.raven.service.PeerListener;
import com.raven.utils.Utilities;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Home extends javax.swing.JLayeredPane implements EventMenuLeft {

    private Menu_Left menuLeft;
    private Chat chatPanel;
    private DatagramSocket socket;
    private Map<String, PeerInfo> peerList = new HashMap<>();
    private Utilities utilities = new Utilities();

    public Home() throws SocketException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);
        menuLeft = new Menu_Left(peerList);
        chatPanel = new Chat();

        initComponents();
        init();
        start();
        PublicEvent.getInstance().addEventMenuLeft(this);
    }

    public void start() {
        String peerName = MyPeer.getInstance().getPeerName();
        String peerAddress = MyPeer.getInstance().getPeerAddress();
        int peerPort = MyPeer.getInstance().getPeerPort();

        new Thread(new PeerListener(MyPeer.getInstance().getPeerPort(), peerList, menuLeft, chatPanel)).start();
        new Thread(new ConnectReceive(menuLeft, chatPanel, peerList, peerName, peerAddress, peerPort)).start();
        utilities.sendMulticastMessage(new PeerInfo(peerAddress, peerName, peerPort), socket, MessageType.CONNECT);
    }

    private void init() {
        setLayout(new MigLayout("fillx, filly", "0[200!]5[fill, 100%]5[200!]0", "0[fill]0"));
        this.add(menuLeft);
        this.add(chatPanel);

        JPanel menuRight = new JPanel();
        menuRight.setBackground(new java.awt.Color(242, 242, 242));
        menuRight.setLayout(new BorderLayout());
        JLabel username = new JLabel(MyPeer.getInstance().getPeerName());
        username.setFont(new java.awt.Font("SansSerif", 1, 14));
        username.setHorizontalAlignment(SwingConstants.CENTER);
        menuRight.add(username, BorderLayout.CENTER);
        this.add(menuRight, "w 200!");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1007, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 551, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void selectedUser(PeerInfo peerInfo) {
        System.out.println("Selected user: " + peerInfo.getName());
        chatPanel.showChatPanelForUser(peerInfo);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
