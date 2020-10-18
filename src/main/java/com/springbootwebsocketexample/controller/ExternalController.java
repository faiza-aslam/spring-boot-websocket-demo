package com.springbootwebsocketexample.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.springbootwebsocketexample.service.SocketIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api")
public class ExternalController {

    @Autowired
    private SocketIOService socketIOService;

    @GetMapping("data")
    public ResponseEntity<String> sendData(@RequestParam("companyId") String companyId) {

        if (socketIOService.getClientMap().containsKey(companyId)) {
            SocketIOClient client = socketIOService.getClientMap().get(companyId);
            client.sendEvent("data_event", "Data: " + "External data received at: " + new Date());
        }

        return ResponseEntity.ok("success");

    }
}
