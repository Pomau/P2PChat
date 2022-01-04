package p2pchat.views;

import p2pchat.controllers.MessageController;
import p2pchat.module.ConnectionData;
import p2pchat.module.Message;
import p2pchat.module.People;

import java.security.*;
import java.util.Scanner;

public class ChatConsole {
    Scanner in = new Scanner(System.in);
    String separation = "---------------------------------------------------------";
    People user;
    MessageController controller;

    public ChatConsole(People user, MessageController controller) {
        this.user = user;
        this.controller = controller;
    }

    public void run() {
        try {
            start();
            int action = 0;
            while (true) {
                System.out.println(separation);
                System.out.println("Действия:");
                System.out.println("1) Посмотреть чаты");
                System.out.println("2) Посмотреть сообщения по id");
                System.out.println("3) Написать сообщение чату по id");
                action = in.nextInt();
                System.out.println(separation);
                switch (action) {
                    case 1:
                        for (String user: controller.getChats(user.getPublicKey())) {
                            System.out.println(user);
                        }
                        break;
                    case 2:
                        System.out.println("Введите id пользователя:");
                        String id = in.next();
                        for (Message message: controller.getMessagesByChat(id, user.getPublicKey())) {
                            System.out.println(message.toString());
                        }
                        break;
                    case 3:
                        System.out.println("Введите id пользователя:");
                        String pk = in.next();
                        System.out.println("Введите сообщения, когда закончите введите в отдельной строке '!!' для завершения написания ообщения");
                        String mes = in.nextLine();
                        StringBuilder text = new StringBuilder("");
                        while (!mes.equals("!!")) {
                            text.append(String.format("%s %n", mes));
                            mes = in.nextLine();
                        }
                        controller.createMessage(text.toString(), user, pk);
                }
            }
        } catch (Exception e) {
            System.out.println("Error " + e);
        }
    }

    public void start() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        int action = 0;
        connect();
        while (action != 1 && action != 2) {
            System.out.println(separation);
            System.out.println("Вход:");
            System.out.println("1) зарегистрироваться");
            System.out.println("2) Вход по приватному и публичному ключу");
            action = in.nextInt();
            if (action == 1) {
                KeyPair keys = controller.createNewKey();
                user.setPrivateKey(controller.byteToString(keys.getPrivate().getEncoded()));
                user.setPublicKey(controller.byteToString(keys.getPublic().getEncoded()));
                System.out.println("Ваш публичный ключ - " + user.getPublicKey());
                System.out.println("Ваш приватный ключ - " + user.getPrivateKey());
            } else if (action == 2) {
                System.out.println("Введите публичный ключ:");
                user.setPublicKey(in.next());
                System.out.println("Введите приватный ключ:");
                user.setPrivateKey(in.next());
            }

        }

    }
    public void connect() {
        System.out.println(separation);
        System.out.println("Подключение к сети");
        System.out.println("Введите ip:port");
        String node = in.next();
        while (!controller.checkNode(new ConnectionData(node))) {
            node = in.next();
        }
        user.addConnection(node);

    }
}
