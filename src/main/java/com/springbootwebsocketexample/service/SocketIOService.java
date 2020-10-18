package com.springbootwebsocketexample.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SocketIOService {

    private static final Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();
    //private final String DISCONNECT_EVENT = "disconnect_event";
    private final String DATA_EVENT = "data_event";

    @Autowired
    private SocketIOServer socketIOServer;

    @PostConstruct
    public void init() {
        start();
    }

    @PreDestroy
    public void close() {
        stop();
    }

    private void start() {
        // Listening Clients Connections
        socketIOServer.addConnectListener(this::connectClient);

        // Listening Client Disconnect
        socketIOServer.addDisconnectListener(this::disconnectClient);
        //socketIOServer.addEventListener(DISCONNECT_EVENT, String.class, this::disconnectClient);

        // Listening to incoming events
        servicesEventListeners();

        // Start Services
        socketIOServer.start();
    }

    private void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    private void connectClient(SocketIOClient client) {
        String companyId = getParamsByClient(client);
        if (companyId == null || companyId.isEmpty()) throw new RuntimeException("Company Id not found");

        log.info("************ Client: " + companyId + " - " + getIpAddress(client) + " Connected ************");

        clientMap.put(companyId, client);
    }

    private void disconnectClient(SocketIOClient client) {
        String clientIp = getIpAddress(client);
        String companyId = getParamsByClient(client);

        log.info(clientIp + " - " + companyId + " ************* " + "Client disconnected" + "*************");
        client.sendEvent("disconnect", "You're disconnected!");

        if (companyId != null) {
            clientMap.remove(companyId);
            client.disconnect();
        }
    }

    private String getParamsByClient(SocketIOClient client) {
        // Get the client url parameter (where userId is the unique identity)
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> companyIdList = params.get("companyId");

        if (!CollectionUtils.isEmpty(companyIdList)) {
            return companyIdList.get(0);
        }
        return null;
    }

    private String getIpAddress(SocketIOClient client) {
        String sa = client.getRemoteAddress().toString();
        return sa.substring(1, sa.indexOf(":"));
    }

    private void servicesEventListeners() {

        socketIOServer.addEventListener(DATA_EVENT, String.class, (client, data, ackSender) -> {
            String clientIp = getIpAddress(client);
            String companyId = getParamsByClient(client);
            log.info(clientIp + " - " + companyId + " ************ Data Event Received -> Data: " + data + "*************");

            //Return same data to client
            client.sendEvent(DATA_EVENT, "Data " + data);
        });
    }

    public Map<String, SocketIOClient> getClientMap() {
        return clientMap;
    }
}
