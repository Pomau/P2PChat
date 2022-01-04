package p2pchat.server;

import org.json.simple.JSONObject;
import p2pchat.module.ConnectionData;
import p2pchat.module.Message;
import p2pchat.module.People;

import java.net.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class Client {
    private People user;

    public Client(People user) {
        this.user = user;
    }

    public boolean run(ConnectionData data, String body) {
        try {
            System.out.println("Подключение к " + data.getIp() + " на порт " + data.getPort());
            Socket client = new Socket(data.getIp(), data.getPort());

            System.out.println("Просто подключается к " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF(body);

            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            System.out.println("Сервер ответил " + in.readUTF());
            client.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public String proccess(String action, List<Object> additionallyJson) {
        JSONObject json = new JSONObject();
        json.put("action", action);
        switch (action) {
            case "connect":
                break;
            case "status":
                break;
            case "getNodes":
                break;
            case "getMessagesByTimestamp":
                json.put("time", new Timestamp(new Date().getTime()- 24 * 3600 * 1000 ).toString());
                break;
            case "getCountMessagesTimestamp":
                json.put("time", new Timestamp(new Date().getTime()- 24 * 3600 * 1000 ).toString());
                break;
            case "getMessages":
                json.put("ids", additionallyJson.get(0));
                break;
            case "sendMessage":
                json.put("message", additionallyJson);
                break;
        }
        return  json.toJSONString();
    }

    public void sendMessage(Message message) {
        for (int i = 0; i < user.getNodes().size(); i++) {
            run( user.getNodes().get(i), proccess("sendMessage", new ArrayList<Object>(Collections.singleton(message.toString()))));
        }
    }

    public void getMessages(List<String> ids, ConnectionData data) {
        run( data, proccess("getMessages", Collections.singletonList(ids)));
    }

    public void CheckStatus() {
        int i = 0;
        while (i != user.getNodes().size()) {
            if (!run(user.getNodes().get(i), proccess("status", new ArrayList<>()))) {
                user.getNodes().remove(i);
            }
            i++;
        }
    }

    public boolean connect(ConnectionData data) {
        return run(data, proccess("connect", new ArrayList<>()));
    }

}