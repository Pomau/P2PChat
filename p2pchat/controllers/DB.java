package p2pchat.controllers;

import p2pchat.module.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DB {
    private static final String url = "jdbc:mysql://localhost:3306/p2pChat";
    private static final String user = "root";
    private static final String password = "2003";
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    private Set<Message> messages = new HashSet<>();


    public DB() {
        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();
        } catch (Exception e) {
            System.out.println("Error connected");
        }

    }

    public boolean add(Message message) {
        try {
            if (!haveMessage(message)) {
                stmt.executeUpdate(String.format("INSERT INTO p2pChat.Messages (id,`text`,toUserId,fromUserId,signature, timeCreate) VALUES ('%s','%s', '%s','%s','%s', '%s');",
                        message.getId(), message.getText(), message.getToUserId(), message.getFromUserId(), message.getSignature(), message.getTimeCreate()));
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error add " + message + e);
        }
        return false;
    }

    public boolean haveMessage(Message message) {
        int count = 0;
        try {
            rs = stmt.executeQuery(String.format("SELECT count(*) FROM p2pChat.Messages WHERE `id` = '%s'", message.getId()));
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error check have messages");
        }
        return (count == 1);
    }

    public List<Message> getAfterDate(Timestamp time) {
        List<Message> messages = new ArrayList<>();
        try {
            rs = stmt.executeQuery(String.format("SELECT * FROM p2pChat.Messages WHERE `timeCreate` >= '%s'", time));
            while (rs.next()) {
                messages.add(new Message(rs));
            }
        } catch (Exception e) {
            System.out.println("Error get messages");
        }
        return messages;
    }

    public Integer getAfterDateCount(Timestamp time) {
        int count = 0;
        try {
            rs = stmt.executeQuery(String.format("SELECT count(*) FROM p2pChat.Messages WHERE `timeCreate` >= '%s'", time));
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error get messages");
        }
        return count;
    }

    public Message getMessageById(String id) {
        try {
            rs = stmt.executeQuery(String.format("SELECT * FROM p2pChat.Messages WHERE `id` = '%s'", id));
            while (rs.next()) {
                return new Message(rs);
            }
        } catch (Exception e) {
            System.out.println("Error get message by id");
        }
        return null;
    }

    public List<String> getChats(String id) {
        Set<String> chats = new HashSet<>();
        try {
            rs = stmt.executeQuery(String.format("SELECT * FROM p2pChat.Messages WHERE `toUserId` = '%s' OR `FromUserId` = '%s'", id, id));
            while (rs.next()) {
                chats.add(rs.getString(3));
                chats.add(rs.getString(4));
            }
            chats.remove(id);
        } catch (Exception e) {
            System.out.println("Error get chats");
        }
        return chats.stream().collect(Collectors.toList());
    }

    public List<Message> getMessagesByChat(String chat, String me) {
        List<Message> chatMessage = new ArrayList<>();
        try {
            rs = stmt.executeQuery(String.format("SELECT * FROM p2pChat.Messages WHERE  (`toUserId` = '%s' OR `toUserId` = '%s') AND (`fromUserId` = '%s' OR `fromUserId` = '%s')", chat, me, me, chat));
            while (rs.next()) {
                chatMessage.add(new Message(rs));
            }
        } catch (Exception e) {
            System.out.println("Error get messages by chat");
        }
        return chatMessage;
    }
}
