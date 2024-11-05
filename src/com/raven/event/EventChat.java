package com.raven.event;

import com.raven.model.Model_Send_Message;
import com.raven.model.PeerInfo;

public interface EventChat {
    void sendTextMessage(PeerInfo peerInfo, Model_Send_Message message);
}
