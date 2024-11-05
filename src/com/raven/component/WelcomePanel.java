package com.raven.component;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class WelcomePanel extends JPanel {
    public WelcomePanel() {
        setLayout(new MigLayout("fill, insets 0", "[grow]", "[grow]"));
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/com/raven/icon/welcome.png"));
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(imageLabel, "grow");

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelWidth = getWidth();
                int panelHeight = getHeight();

                Image scaledImage = originalIcon.getImage().getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            }
        });
    }
}
