package com.raven.component;

import com.raven.app.MessageType;
import com.raven.event.PublicEvent;
import com.raven.model.Model_Send_Message;
import com.raven.model.MyPeer;
import com.raven.model.PeerInfo;
import com.raven.service.PeerConnectionHandler;
import com.raven.swing.JIMSendTextPane;
import com.raven.swing.ScrollBar;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import org.json.JSONObject;

public class Chat_Bottom extends javax.swing.JPanel {

    private PeerInfo peerInfo;
    private JIMSendTextPane txtMessage;
    private Chat_Body chatBody;

    public Chat_Bottom(PeerInfo peerInfo, Chat_Body chatBody) {
        this.chatBody = chatBody;
        this.peerInfo = peerInfo;
        initComponents();
        init();
    }

    private void init() {
        mig = new MigLayout("fillx, filly", "0[fill]0[]0[]2", "2[fill]2[]0");
        setLayout(mig);
        JScrollPane scroll = new JScrollPane();
        scroll.setBorder(null);
        txtMessage = new JIMSendTextPane();

        txtMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
                    String message = txtMessage.getText().trim();
                    if (!message.isEmpty() && PublicEvent.getInstance().getEventChat() != null) {
                        Model_Send_Message model = new Model_Send_Message();
                        model.setSender(MyPeer.getInstance().getPeerName());
                        model.setContent(message);
                        model.setType(MessageType.TEXT);
                        model.setFile(null);
                        PublicEvent.getInstance().getEventChat().sendTextMessage(peerInfo, model);

                        txtMessage.setText("");
                        txtMessage.grabFocus();
                    }
                }
            }
        });

        txtMessage.setBorder(new EmptyBorder(5, 5, 5, 5));
        txtMessage.setHintText("Write Message Here ...");
        scroll.setViewportView(txtMessage);

        ScrollBar sb = new ScrollBar();
        sb.setBackground(new Color(229, 229, 229));
        sb.setPreferredSize(new Dimension(2, 10));
        scroll.setVerticalScrollBar(sb);
        add(sb);
        add(scroll, "w 100%");
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("filly", "0[]3[]0", "0[bottom]0"));
        panel.setPreferredSize(new Dimension(30, 28));
        panel.setBackground(Color.WHITE);

        JButton btnSend = new JButton();
        btnSend.setBorder(null);
        btnSend.setContentAreaFilled(false);
        btnSend.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSend.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/send.png")));
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String message = txtMessage.getText().trim();
                if (!message.isEmpty() && PublicEvent.getInstance().getEventChat() != null) {
                    Model_Send_Message model = new Model_Send_Message();
                    model.setSender(MyPeer.getInstance().getPeerName());
                    model.setContent(message);
                    model.setType(MessageType.TEXT);
                    model.setFile(null);
                    PublicEvent.getInstance().getEventChat().sendTextMessage(peerInfo, model);

                    txtMessage.setText("");
                    txtMessage.grabFocus();
                }
            }
        });

        JButton cmdMore = new JButton();
        cmdMore.setBorder(null);
        cmdMore.setContentAreaFilled(false);
        cmdMore.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdMore.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/more_disable.png")));
        cmdMore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (panelMore.isVisible()) {
                    cmdMore.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/more_disable.png")));
                    panelMore.setVisible(false);
                    mig.setComponentConstraints(panelMore, "dock south,h 0!");
                    revalidate();
                } else {
                    cmdMore.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/more.png")));
                    panelMore.setVisible(true);
                    mig.setComponentConstraints(panelMore, "dock south,h 170!");
                    revalidate();
                }
            }
        });
        panel.add(cmdMore);
        panel.add(btnSend);
        add(panel, "wrap");
        panelMore = new Panel_More(peerInfo, chatBody);
        panelMore.setVisible(false);
        add(panelMore, "dock south,h 0!");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(229, 229, 229));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 40, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private MigLayout mig;
    private Panel_More panelMore;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
