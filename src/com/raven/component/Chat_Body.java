package com.raven.component;

import com.raven.app.MessageType;
import com.raven.emoji.Emogi;
import com.raven.model.Model_Receive_Message;
import com.raven.model.Model_Send_Message;
import com.raven.swing.ScrollBar;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import javax.swing.JScrollBar;
import net.miginfocom.swing.MigLayout;

public class Chat_Body extends javax.swing.JPanel {

    public Chat_Body() {
        initComponents();
        init();
    }

    private void init() {
        body.setLayout(new MigLayout("fillx", "", "5[bottom]5"));
        sp.setVerticalScrollBar(new ScrollBar());
        sp.getVerticalScrollBar().setBackground(Color.WHITE);
    }

    public void addItemLeft(Model_Receive_Message data) {
        if (data.getMessageType() == MessageType.TEXT) {
            Chat_Left item = new Chat_Left();
            item.setText(data.getContent());
            item.setTime();
            body.add(item, "wrap, w 100::80%");
        } else if (data.getMessageType() == MessageType.EMOJI) {
            Chat_Left item = new Chat_Left();
            item.setEmoji(Emogi.getInstance().getImoji(Integer.valueOf(data.getContent())).getIcon());
            item.setTime();
            body.add(item, "wrap, w 100::80%");
        } else if (data.getMessageType() == MessageType.IMAGE) {
            Chat_Left item = new Chat_Left();
            item.setText("");
            item.setImage(data.getContent());
            item.setTime();
            body.add(item, "wrap, w 100::80%");
        } else if (data.getMessageType() == MessageType.VIDEO) {
            Chat_Left item = new Chat_Left();
            item.setText("");
            item.setVideo(data.getContent());
            item.setTime();
            body.add(item, "wrap, w 100::80%");
        } else if (data.getMessageType() == MessageType.VOICE) {
            Chat_Left item = new Chat_Left();
            item.setText("");
            item.setVoice(data.getContent());
            item.setTime();
            body.add(item, "wrap, w 100::80%");
        } else if (data.getMessageType() == MessageType.FILE) {
            Chat_Left item = new Chat_Left();
            item.setText("");
            item.setFile(data.getContent());
            item.setTime();
            body.add(item, "wrap, w 100::80%");
        }

        repaint();
        revalidate();
    }

    public void addItemRight(Model_Send_Message data) {
        if (data.getType() == MessageType.TEXT) {
            Chat_Right item = new Chat_Right();
            item.setText(data.getContent());
            item.setTime();
            body.add(item, "wrap, al right, w 100::80%");
        } else if (data.getType() == MessageType.EMOJI) {
            Chat_Right item = new Chat_Right();
            item.setEmoji(Emogi.getInstance().getImoji(Integer.valueOf(data.getContent())).getIcon());
            item.setTime();
            body.add(item, "wrap, al right, w 100::80%");
        } else if (data.getType() == MessageType.IMAGE) {
            Chat_Right item = new Chat_Right();
            item.setText("");
            item.setImage(data.getContent());
            item.setTime();
            body.add(item, "wrap, al right, w 100::80%");
        } else if (data.getType() == MessageType.VIDEO) {
            Chat_Right item = new Chat_Right();
            item.setVideo(data.getContent());
            item.setTime();
            body.add(item, "wrap, al right, w 100::80%");
        } else if (data.getType() == MessageType.VOICE) {
            Chat_Right item = new Chat_Right();
            item.setText("");
            item.setVoice(data.getContent());
            item.setTime();
            body.add(item, "wrap, al right, w 100::80%");
        } else if (data.getType() == MessageType.FILE) {
            Chat_Right item = new Chat_Right();
            item.setText("");
            item.setFile(data.getContent());
            item.setTime();
            body.add(item, "wrap, al right, w 100::80%");
        }

        repaint();
        revalidate();
        scrollToBottom();
    }

    public void clearChat() {
        body.removeAll();
        repaint();
        revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sp = new javax.swing.JScrollPane();
        body = new javax.swing.JPanel();

        sp.setBorder(null);
        sp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        body.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 826, Short.MAX_VALUE)
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 555, Short.MAX_VALUE)
        );

        sp.setViewportView(body);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sp)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sp)
        );
    }// </editor-fold>//GEN-END:initComponents

    public void scrollToBottom() {
        JScrollBar verticalBar = sp.getVerticalScrollBar();
        AdjustmentListener downScroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(downScroller);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    private javax.swing.JScrollPane sp;
    // End of variables declaration//GEN-END:variables
}
