package com.raven.component;

import com.raven.app.MessageType;
import com.raven.emoji.Emogi;
import com.raven.emoji.Model_Emoji;
import com.raven.main.Main;
import com.raven.model.Model_File_Sender;
import com.raven.model.Model_Send_Message;
import com.raven.model.MyPeer;
import com.raven.model.PeerInfo;
import com.raven.service.PeerConnectionHandler;
import com.raven.swing.ScrollBar;
import com.raven.swing.WrapLayout;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import javax.sound.sampled.*;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.swing.MigLayout;

public class Panel_More extends javax.swing.JPanel {
    private PeerInfo peerInfo;
    private TargetDataLine targetDataLine;
    private File audioFile;
    private Chat_Body chatBody;

    public Panel_More(PeerInfo peerInfo, Chat_Body chatBody) {
        this.peerInfo = peerInfo;
        this.chatBody = chatBody;
        initComponents();
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx"));
        panelHeader = new JPanel();
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.LINE_AXIS));
        panelHeader.add(getButtonImage());
        panelHeader.add(getButtonVideo());
        panelHeader.add(getButtonFile());
        panelHeader.add(getButtonVoice());
        panelHeader.add(getEmojiStyle1());
        panelHeader.add(getEmojiStyle2());
        add(panelHeader, "w 100%, h 30!, wrap");
        panelDetail = new JPanel();
        panelDetail.setLayout(new WrapLayout(WrapLayout.LEFT));    //  use warp layout
        JScrollPane ch = new JScrollPane(panelDetail);
        ch.setBorder(null);
        ch.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        ch.setVerticalScrollBar(new ScrollBar());
        //  test color
        add(ch, "w 100%, h 100%");
    }

    private JButton getButtonImage() {
        OptionButton cmd = new OptionButton();
        cmd.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/image.png")));
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JFileChooser ch = new JFileChooser();
                ch.setMultiSelectionEnabled(true);
                ch.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        return file.isDirectory() || isImageFile(file);
                    }

                    @Override
                    public String getDescription() {
                        return "Image File";
                    }
                });
                int option = ch.showOpenDialog(Main.getFrames()[0]);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File files[] = ch.getSelectedFiles();
                    for (File file: files) {
                        try {
                            File directory = new File("file/images/");
                            if (!directory.exists()) {
                                directory.mkdirs();
                            }

                            String newFilePath = "file/images/" + UUID.randomUUID().toString() + "_" + file.getName();
                            File destinationFile = new File(newFilePath);
                            Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                            sendImageMessage(newFilePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        return cmd;
    }

    private JButton getButtonVideo() {
        OptionButton cmd = new OptionButton();
        cmd.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/video.png"))); // Icon cho nút video
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JFileChooser ch = new JFileChooser();
                ch.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        String name = file.getName().toLowerCase();
                        return file.isDirectory() || name.endsWith(".mp4") || name.endsWith(".avi") || name.endsWith(".mov");
                    }

                    @Override
                    public String getDescription() {
                        return "Video Files (.mp4, .avi, .mov)";
                    }
                });

                int option = ch.showOpenDialog(Main.getFrames()[0]);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = ch.getSelectedFile();

                    File directory = new File("file/medias/");
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }

                    String newFilePath = "file/medias/" + UUID.randomUUID().toString() + "_" + selectedFile.getName();
                    File destinationFile = new File(newFilePath);

                    try {
                        Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        sendVideoMessage(newFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return cmd;
    }

    private JButton getButtonFile() {
        OptionButton cmd = new OptionButton();
        cmd.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/link.png")));
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JFileChooser ch = new JFileChooser();
                int option = ch.showOpenDialog(Main.getFrames()[0]);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = ch.getSelectedFile();

                    File directory = new File("file/docs/");
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }

                    String newFilePath = "file/docs/" + UUID.randomUUID().toString() + "_" + selectedFile.getName();
                    File destinationFile = new File(newFilePath);

                    try {
                        Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        sendFileMessage(newFilePath);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        return cmd;
    }

    private JButton getButtonVoice() {
        OptionButton cmd = new OptionButton();
        cmd.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/micro.png")));
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (targetDataLine == null) {
                    String filePath = startRecording();
                    cmd.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/micro_stop.png")));
                } else {
                    stopRecording();
                    cmd.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/micro.png")));
                    sendVoiceMessage(audioFile.getAbsolutePath());
                }
            }
        });
        return cmd;
    }

    private JButton getEmojiStyle1() {
        OptionButton cmd = new OptionButton();
        cmd.setIcon(Emogi.getInstance().getImoji(1).toSize(25, 25).getIcon());
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                clearSelected();
                cmd.setSelected(true);
                panelDetail.removeAll();
                for (Model_Emoji d : Emogi.getInstance().getStyle1()) {
                    panelDetail.add(getButton(d));
                }
                panelDetail.repaint();
                panelDetail.revalidate();
            }
        });
        return cmd;
    }

    private JButton getEmojiStyle2() {
        OptionButton cmd = new OptionButton();
        cmd.setIcon(Emogi.getInstance().getImoji(21).toSize(25, 25).getIcon());
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                clearSelected();
                cmd.setSelected(true);
                panelDetail.removeAll();
                for (Model_Emoji d : Emogi.getInstance().getStyle2()) {
                    panelDetail.add(getButton(d));
                }
                panelDetail.repaint();
                panelDetail.revalidate();
            }
        });
        return cmd;
    }

    private JButton getButton(Model_Emoji data) {
        JButton cmd = new JButton(data.getIcon());
        cmd.setName(data.getId() + "");
        cmd.setBorder(new EmptyBorder(3, 3, 3, 3));
        cmd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmd.setContentAreaFilled(false);
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Model_Send_Message message = new Model_Send_Message();
                message.setSender(MyPeer.getInstance().getPeerName());
                message.setType(MessageType.EMOJI);
                message.setContent(data.getId() + "");
                chatBody.addItemRight(message);
                new Thread(new PeerConnectionHandler(peerInfo, message)).start();
            }
        });
        return cmd;
    }

    private String startRecording() {
        try {
            AudioFormat audioFormat = new AudioFormat(44100, 16, 1, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(audioFormat);
            targetDataLine.start();

            String fileName = UUID.randomUUID().toString();
            String filePath = "file/voices/" + fileName + ".wav";

            File directory = new File("file/voices/");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            audioFile = new File(filePath);

            Thread recordingThread = new Thread(() -> {
                try (AudioInputStream audioStream = new AudioInputStream(targetDataLine)) {
                    AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            recordingThread.start();

            return filePath;

        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();
        targetDataLine = null;
    }

    private void sendVoiceMessage(String pathName) {
        try {
            Model_Send_Message message = new Model_Send_Message();
            message.setSender(MyPeer.getInstance().getPeerName());
            message.setType(MessageType.VOICE);
            message.setContent(pathName);

            Model_File_Sender fileSender = new Model_File_Sender(
                    new RandomAccessFile(new File(pathName), "r"),
                    new File(pathName),
                    ".wav",
                    new File(pathName).length(),
                    message
            );

            message.setFile(fileSender);

            new Thread(new PeerConnectionHandler(this.peerInfo, message)).start();
            chatBody.addItemRight(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendVideoMessage(String pathName) {
        try {
            Model_Send_Message message = new Model_Send_Message();
            message.setSender(MyPeer.getInstance().getPeerName());
            message.setType(MessageType.VIDEO);
            message.setContent(pathName);

            File file = new File(pathName);

            Model_File_Sender fileSender = new Model_File_Sender(
                    new RandomAccessFile(file, "r"),
                    file,
                    file.getName().substring(file.getName().lastIndexOf(".")),
                    file.length(),
                    message
            );

            message.setFile(fileSender);

            new Thread(new PeerConnectionHandler(this.peerInfo, message)).start();

            chatBody.addItemRight(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendImageMessage(String pathName) {
        try {
            Model_Send_Message message = new Model_Send_Message();
            message.setSender(MyPeer.getInstance().getPeerName());
            message.setType(MessageType.IMAGE);
            message.setContent(pathName);

            File file = new File(pathName);

            Model_File_Sender fileSender = new Model_File_Sender(
                    new RandomAccessFile(file, "r"),
                    file,
                    file.getName().substring(file.getName().lastIndexOf(".")),
                    file.length(),
                    message
            );

            message.setFile(fileSender);

            new Thread(new PeerConnectionHandler(this.peerInfo, message)).start();

            chatBody.addItemRight(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFileMessage(String pathName) {
        try {
            Model_Send_Message message = new Model_Send_Message();
            message.setSender(MyPeer.getInstance().getPeerName());
            message.setType(MessageType.FILE);
            message.setContent(pathName);

            File file = new File(pathName);

            Model_File_Sender fileSender = new Model_File_Sender(
                    new RandomAccessFile(file, "r"),
                    file,
                    file.getName().substring(file.getName().lastIndexOf(".")),
                    file.length(),
                    message
            );

            message.setFile(fileSender);

            new Thread(new PeerConnectionHandler(this.peerInfo, message)).start();

            chatBody.addItemRight(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 515, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 84, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void clearSelected() {
        for (Component c : panelHeader.getComponents()) {
            if (c instanceof OptionButton) {
                ((OptionButton) c).setSelected(false);
            }
        }
    }

    private boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg") || name.endsWith(".gif");
    }

    private JPanel panelHeader;
    private JPanel panelDetail;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
