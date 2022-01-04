package p2pchat.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import p2pchat.controllers.MessageController;
import p2pchat.module.ConnectionData;
import p2pchat.module.Message;
import p2pchat.module.People;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;

public class Server extends Thread{
    private ServerSocket serverSocket;
    private People user;
    private MessageController controller;
    private Client client;
    public Server(String data, People user, MessageController controller, Client client) throws IOException {
        serverSocket = new ServerSocket(new ConnectionData(data).getPort());
        serverSocket.setSoTimeout(0);
        this.user = user;
        this.controller = controller;
        this.client = client;
    }

    public void run() {
        while(true) {
            try {
                System.out.println("Ожидание клиента на порт " + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();

                System.out.println("Просто подключается к " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());

                String input = in.readUTF();
                System.out.println(input);


                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF(process(input, server.getRemoteSocketAddress().toString()));
                server.close();
            } catch (SocketTimeoutException s) {
                System.out.println("Время сокета истекло!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public String process(String jsonString, String connect) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);
        String action = (String) json.get("action");

        JSONObject result = new JSONObject();
        result.put("action", action);
        switch (action) {
            case "connect":
                break;
            case "status":
                break;
            case "getNodes":
                result.put("users", user.getNodes());
                break;
            case "getMessagesByTimestamp":
                result.put("messages", controller.getMessagesAfterTimestamp(Timestamp.valueOf((String) json.get("time"))));
                break;
            case "getCountMessagesTimestamp":
                result.put("count", controller.getCountMessagesAfterTimestamp(Timestamp.valueOf((String) json.get("time"))));
                break;
            case "sendMessage":
                String Messages = (String) ((JSONArray) json.get("message")).get(0);
                JSONObject jsonMessage = (JSONObject) parser.parse(Messages);
                controller.addMessage(new Message(jsonMessage));
                break;
        }
        result.put("status", "ok");
        return result.toJSONString();
    }


}
