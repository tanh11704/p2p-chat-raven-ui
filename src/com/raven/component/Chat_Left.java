package com.raven.component;

import java.awt.Color;
import java.time.LocalDateTime;
import javax.swing.*;

public class Chat_Left extends javax.swing.JLayeredPane {

    public Chat_Left() {
        initComponents();
        txt.setBackground(new Color(242, 242, 242));

        setDefaultImageProfile();
    }

    private void setDefaultImageProfile() {
        IaImage.setImage(new ImageIcon(getClass().getResource("/com/raven/icon/profile.png")));
    }

    public void setText(String text) {
        if (text.equals("")) {
            txt.hideText();
        } else {
            txt.setText(text);
        }

    }

    public void setImage(String imagePath) {
        txt.setImage(false, imagePath);
    }

    public void setVideo(String videoFilePath) {
        txt.setVideoContent(videoFilePath);
    }

    public void setFile(String filePath) {
        txt.setFile(filePath);
    }

    public void setEmoji(Icon icon) {
        txt.hideText();
        txt.setEmoji(false, icon);
    }

    public void setVoice(String voiceFilePath) {
        txt.setVoice(voiceFilePath);
    }

    public void setTime() {
        LocalDateTime now = LocalDateTime.now();
        txt.setTime(now.getHour() + ":" + now.getMinute());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        IaImage = new com.raven.swing.ImageAvatar();
        txt = new com.raven.component.Chat_Item();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        IaImage.setBorderSize(0);
        IaImage.setMaximumSize(new java.awt.Dimension(31, 31));
        IaImage.setMinimumSize(new java.awt.Dimension(31, 31));
        IaImage.setPreferredSize(new java.awt.Dimension(31, 31));

        jLayeredPane1.setLayer(IaImage, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
                jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jLayeredPane1Layout.createSequentialGroup()
                                .addComponent(IaImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        jLayeredPane1Layout.setVerticalGroup(
                jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(IaImage, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        add(jLayeredPane1);
        add(txt);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.raven.swing.ImageAvatar IaImage;
    private javax.swing.JLayeredPane jLayeredPane1;
    private com.raven.component.Chat_Item txt;
    // End of variables declaration//GEN-END:variables
}
