package com.raven.component;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class Chat_Item extends javax.swing.JLayeredPane {

    private JLabel label;
    private JFXPanel videoPanel;

    public Chat_Item() {
        initComponents();
        txt.setEditable(false);
        txt.setBackground(new Color(0, 0, 0, 0));
        txt.setOpaque(false);
    }

    public void setText(String text) {
        txt.setText(text);
    }

    public void setTime(String time) {
        JLayeredPane layer = new JLayeredPane();
        layer.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        layer.setBorder(new EmptyBorder(0, 5, 10, 5));
        label = new JLabel(time);
        label.setForeground(new Color(110, 110, 110));
        label.setHorizontalTextPosition(JLabel.LEFT);
        layer.add(label);
        add(layer);
    }

    public void setImage(boolean right, String filePath) {
        JLayeredPane layer = new JLayeredPane();
        layer.setLayout(new FlowLayout(right ? FlowLayout.RIGHT : FlowLayout.LEFT));
        layer.setBorder(new EmptyBorder(0, 5, 0, 5));

        Chat_Image chatImage = new Chat_Image(right);
        chatImage.addImage(filePath);
        layer.add(chatImage);

        add(layer);
    }

    public void setVideoContent(String videoFilePath) {
        if (videoPanel == null) {
            videoPanel = new JFXPanel();
            videoPanel.setPreferredSize(new Dimension(356, 200));
            add(videoPanel, BorderLayout.CENTER);
        }

        Platform.runLater(() -> {
            Media media = new Media(new File(videoFilePath).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            mediaView.setFitHeight(200);
            mediaView.setPreserveRatio(true);

            mediaView.setOnMouseClicked(event -> {
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.pause();
                } else {
                    if (mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
                        mediaPlayer.seek(Duration.ZERO);
                    }
                    mediaPlayer.play();
                }
            });

            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.stop();
            });

            Scene scene = new Scene(new Group(mediaView), 356, 200);
            videoPanel.setScene(scene);
        });
    }

    public void setFile(String filePath) {
        JLayeredPane layer = new JLayeredPane();
        layer.setLayout(new FlowLayout(FlowLayout.LEFT));
        layer.setBorder(new EmptyBorder(0, 5, 0, 5));
        Chat_File chatFile = new Chat_File(filePath);
        layer.add(chatFile);
        add(layer);
    }

    public void setEmoji(boolean right, Icon icon) {
        JLayeredPane layer = new JLayeredPane();
        layer.setLayout(new FlowLayout(right ? FlowLayout.RIGHT : FlowLayout.LEFT));
        layer.setBorder(new EmptyBorder(0, 5, 0, 5));
        layer.add(new JLabel(icon));
        add(layer);
        setBackground(null);
    }

    public void setVoice(String voiceFilePath) {
        JLayeredPane layer = new JLayeredPane();
        layer.setLayout(new FlowLayout(FlowLayout.LEFT));
        layer.setBorder(new EmptyBorder(0, 5, 0, 5));

        Chat_Voice chatVoice = new Chat_Voice(voiceFilePath);
        layer.add(chatVoice);
        add(layer);
    }

    public void sendSuccess() {
        if (label != null) {
            label.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/tick.png")));
        }
    }

    public void seen() {
        if (label != null) {
            label.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/double_tick.png")));
        }
    }

    public void hideText() {
        txt.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txt = new com.raven.swing.JIMSendTextPane();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));

        txt.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 5, 10));
        txt.setSelectionColor(new java.awt.Color(92, 188, 255));
        add(txt);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        if (getBackground() != null) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        }
        super.paintComponent(grphcs);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.raven.swing.JIMSendTextPane txt;
    // End of variables declaration//GEN-END:variables
}
