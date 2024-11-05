package com.raven.component;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Chat_Voice extends JPanel {
    private File audioFile;
    private Clip audioClip;

    public Chat_Voice(String filePath) {
        this.audioFile = new File(filePath);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton playButton = new JButton("Play");

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playAudio();
            }
        });

        add(playButton);
    }

    private void playAudio() {
        try {
            if (audioClip == null || !audioClip.isOpen()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                audioClip = AudioSystem.getClip();
                audioClip.open(audioStream);
            }

            if (audioClip.isRunning()) {
                audioClip.stop();
            }

            audioClip.setFramePosition(0);
            audioClip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    public void stopAudio() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
        }
    }
}
