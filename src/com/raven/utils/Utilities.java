package com.raven.utils;

import com.raven.app.MessageType;
import com.raven.model.PeerInfo;
import org.json.JSONObject;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utilities {

    public String getIpAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (isInvalidInterface(networkInterface)) {
                    continue;
                }

                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (isValidInetAddress(inetAddress)) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
        }
        return null;
    }

    private boolean isInvalidInterface(NetworkInterface networkInterface) throws SocketException {
        String interfaceName = networkInterface.getDisplayName().toLowerCase();
        return networkInterface.isLoopback() || !networkInterface.isUp() || networkInterface.isVirtual()
                || interfaceName.contains("vmware") || interfaceName.contains("virtual") || interfaceName.contains("loopback");
    }

    private boolean isValidInetAddress(InetAddress inetAddress) {
        return inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress();
    }

    public void sendBroadcastMessage(PeerInfo peerInfo, DatagramSocket socket, MessageType type) {
        JSONObject message = new JSONObject();
        message.put("type", type);
        message.put("name", peerInfo.getName());
        message.put("address", peerInfo.getAddress());
        message.put("port", peerInfo.getPort());

        try {
            byte[] buffer = message.toString().getBytes();
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, 9999);
            socket.send(packet);
        } catch (IOException e) {
        }
    }

    public int findAvailablePort() {
        for (int port = 1; port <= 65000; port++) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                return port;
            } catch (IOException e) {
                // Port is in use, continue to the next one
            }
        }
        throw new RuntimeException("Không tìm thấy cổng trống");
    }
}