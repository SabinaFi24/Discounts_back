package com.dev.utils;

import com.dev.Persist;
import com.dev.objects.*;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MessagesHandler extends TextWebSocketHandler {

    private static List<WebSocketSession> sessionList = new CopyOnWriteArrayList<>();
    private static int totalSessions;
    public static Map<String, WebSocketSession> sessionMap = new HashMap<>();

    @Autowired
    private Persist persist;
    private List<UserObject> userObjectList;
    private List<SaleObject> startSales;
    private List<SaleObject> endSales;

    @PostConstruct

    public void init () {
        new Thread(() -> {
            while (true) {
                try {
                    sendStartSale();
                    sendEndDate();
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        Map<String, String> map = Utils.splitQuery(session.getUri().getQuery());
        sessionMap.put(map.get("token"),session);
        sessionList.add(session);
        totalSessions = sessionList.size();
        System.out.println("afterConnectionEstablished");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        System.out.println("handleTextMessage");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionList.remove(session);
        System.out.println("afterConnectionClosed");
    }

    /*public void sendToEveryone () {
        for (WebSocketSession session : sessionList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("test", String.valueOf(System.currentTimeMillis()));
            try {
                session.sendMessage(new TextMessage(jsonObject.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    public void sendStartSale() {
        List<SaleObject> startDates = persist.getStartDate();
        List<UserObject> userObjects = null;
        List<OrganizationObject> organizations = persist.getAllOrganizations();
        if (startDates != null) {
            for (SaleObject start : startSales) {
                if (start.isForAll() != 1) {
                    for (OrganizationObject organization : organizations) {
                        if (persist.doseStoreBelongToOrganization(start.getStore().getId(), organization.getOrganizationId())) {
                            userObjects = persist.getUserByOrganizationId(organization.getOrganizationId());
                            sender(userObjects,start);}}
                }else {
                    userObjects= persist.getAllUsers();
                    sender(userObjects,start);}
            }
        } else {
            System.out.println("no sale is starting now");
        }
    }


    public void sendEndDate() {
        List<SaleObject> endDates = persist.getEndDate();
        List<UserObject> userObjects = null;
        List<OrganizationObject> organizations = persist.getAllOrganizations();
        if (endDates != null) {
            for (SaleObject end : endDates) {
                if (end.isForAll() != 1) {
                    for (OrganizationObject organization : organizations) {
                        if (persist.doseStoreBelongToOrganization(end.getStore().getId(), organization.getOrganizationId())) {
                            userObjects = persist.getUserByOrganizationId(organization.getOrganizationId());
                            sender(userObjects,end);
                        }
                    }
                }else { userObjects= persist.getAllUsers();
                    sender(userObjects,end);}
            }
        } else {
            System.out.println("There is no sales ending today");
        }
    }

    public void sender(List<UserObject> userObjects,SaleObject sale){
        List<UserObject> userObjectList=persist.removeIfMultipleUsers(userObjects);
        try {
            if (userObjectList != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content", sale.getContent());
                for (UserObject userObject : userObjectList) {
                    sessionList.add(sessionMap.get(userObject.getToken()));
                    if (sessionMap.get(userObject.getToken()) != null)
                        sessionMap.get(userObject.getToken()).sendMessage(new TextMessage(jsonObject.toString()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}