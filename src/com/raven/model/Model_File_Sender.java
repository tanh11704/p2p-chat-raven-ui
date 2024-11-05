package com.raven.model;

import java.io.File;
import java.io.RandomAccessFile;

public class Model_File_Sender {
    private RandomAccessFile fileAccess;
    private File file;
    private String fileExtension;
    private long fileSize;
    private Model_Send_Message message;

    public Model_File_Sender(RandomAccessFile fileAccess, File file, String fileExtension, long fileSize, Model_Send_Message message) {
        this.fileAccess = fileAccess;
        this.file = file;
        this.fileExtension = fileExtension;
        this.fileSize = fileSize;
        this.message = message;
    }

    public RandomAccessFile getFileAccess() {
        return fileAccess;
    }

    public void setFileAccess(RandomAccessFile fileAccess) {
        this.fileAccess = fileAccess;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Model_Send_Message getMessage() {
        return message;
    }

    public void setMessage(Model_Send_Message message) {
        this.message = message;
    }
}
