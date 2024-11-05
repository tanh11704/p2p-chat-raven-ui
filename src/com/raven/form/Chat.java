package com.raven.form;

import com.raven.component.Chat_Body;
import com.raven.component.Chat_Bottom;
import com.raven.component.Chat_Title;
import com.raven.component.WelcomePanel;
import com.raven.event.EventChat;
import com.raven.event.PublicEvent;
import com.raven.model.Model_Send_Message;
import com.raven.model.PeerInfo;
import com.raven.service.PeerConnectionHandler;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Chat extends JPanel {

    private CardLayout cardLayout;
    private Map<String, JPanel> userChatPanels;

    public Chat() {
        initComponents();
        init();
        add(new WelcomePanel(), "Welcome");
        cardLayout.show(this, "Welcome");
        registerEvent();
    }

    private void init() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        userChatPanels = new HashMap<>();
    }

    private void registerEvent() {
        PublicEvent.getInstance().addEventChat(new EventChat() {
            @Override
            public void sendTextMessage(PeerInfo peerInfo, Model_Send_Message message) {
                Chat_Body chatBody = (Chat_Body) userChatPanels.get(peerInfo.getName()).getComponent(1);
                chatBody.addItemRight(message);
                new Thread(new PeerConnectionHandler(peerInfo, message)).start();
            }
        });
    }

    public void addChatPanelForUser(PeerInfo peerInfo) {
        JPanel userPanel = createUserPanel(peerInfo);
        String peerName = peerInfo.getName();

        add(userPanel, peerName);
        userChatPanels.put(peerName, userPanel);
    }

    public void removeChatPanelForUser(PeerInfo peerInfo) {
        String peerName = peerInfo.getName();
        JPanel panel = userChatPanels.get(peerName);

        if (panel != null) {
            remove(panel);
            userChatPanels.remove(peerName);
            revalidate();
            repaint();
        }
    }

    public void showChatPanelForUser(PeerInfo peerInfo) {
        String peerName = peerInfo.getName();
        if (userChatPanels.containsKey(peerName)) {
            cardLayout.show(this, peerName);
        } else {
            System.err.println("Chat panel for user " + peerName + " does not exist.");
        }
    }

    private JPanel createUserPanel(PeerInfo peerInfo) {
        JPanel userPanel = new JPanel(new MigLayout("fillx", "0[fill]0", "0[]0[100%, fill]0[shrink 0]0"));

        Chat_Title chatTitle = new Chat_Title(peerInfo.getName());
        Chat_Body chatBody = new Chat_Body();
        Chat_Bottom chatBottom = new Chat_Bottom(peerInfo, chatBody);

        chatBody.clearChat();

        userPanel.add(chatTitle, "wrap");
        userPanel.add(chatBody, "wrap");
        userPanel.add(chatBottom, "h ::50%");

        return userPanel;
    }

    public Chat_Body getChatBodyByUser(String user) {
        return (Chat_Body) userChatPanels.get(user).getComponent(1);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 727, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 681, Short.MAX_VALUE)
        );
    }
}