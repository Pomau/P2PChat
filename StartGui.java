import p2pchat.controllers.MessageController;
import p2pchat.module.ConnectionData;
import p2pchat.module.People;
import p2pchat.server.Client;
import p2pchat.server.Server;
import p2pchat.views.ChatConsole;

import java.io.IOException;


public class StartGui {
    public static void main(String[] args) throws IOException {

        String myIp = "127.0.0.1:12345";
        People user = new People(20, new ConnectionData(myIp));
        Client client = new Client(user);


        MessageController controller = new MessageController(client);
        new Thread(new Server(myIp, user, controller, client)).start();
        new ChatConsole(user, controller).run();
        return;
    }
}
