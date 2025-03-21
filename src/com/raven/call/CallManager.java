package com.raven.call;
import com.raven.model.PeerInfo;
import org.json.JSONObject;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class CallManager {
    private static CallManager instance;
    private Map<String, Call_Panel> activeCalls;

    private CallManager() {
        activeCalls = new HashMap<>();
    }

    public static synchronized CallManager getInstance() {
        if (instance == null) {
            instance = new CallManager();
        }
        return instance;
    }

    public void handleIncomingCall(PeerInfo peerInfo, JSONObject callData) {
        String callerId = callData.getString("sender");
        String callerIp = callData.getString("senderAddress");
        int receiverPort = callData.getInt("receiverPort");
        int listenPort = callData.getInt("listenPort");

        // Create call panel for incoming call
        SwingUtilities.invokeLater(() -> {
            Call_Panel callPanel = new Call_Panel(peerInfo, true, receiverPort, listenPort, callerIp);
            activeCalls.put(callerId, callPanel);
            callPanel.showCallFrame();
        });
    }

    public void startOutgoingCall(PeerInfo peerInfo, JSONObject callData) {
        String receiverId = peerInfo.getAddress();
        int receiverPort = callData.getInt("receiverPort");
        int listenPort = callData.getInt("listenPort");

        // Create call panel for outgoing call
        SwingUtilities.invokeLater(() -> {
            Call_Panel callPanel = new Call_Panel(peerInfo, false, listenPort, receiverPort, receiverId);
            activeCalls.put(receiverId, callPanel);
            callPanel.showCallFrame();
        });
    }

    public void handleCallAccepted(String callerId, int remotePort) {
        Call_Panel callPanel = activeCalls.get(callerId);

        if (callPanel != null) {
            SwingUtilities.invokeLater(() -> {
                callPanel.startCall();
            });
        }
    }

    public void handleCallRejected(String callerId) {
        Call_Panel callPanel = activeCalls.get(callerId);

        if (callPanel != null) {
            SwingUtilities.invokeLater(() -> {
                callPanel.stopCall();
                activeCalls.remove(callerId);
            });
        }
    }

    public void handleCallEnded(String callerId) {
        Call_Panel callPanel = activeCalls.get(callerId);

        if (callPanel != null) {
            SwingUtilities.invokeLater(() -> {
                callPanel.stopCall();
                activeCalls.remove(callerId);
            });
        }
    }
}
