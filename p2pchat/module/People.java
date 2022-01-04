package p2pchat.module;

public class People extends Node{
    private String username;
    private String publicKey;
    private String privateKey;

    public People(int maxNodesConnection, ConnectionData data) {
        super(maxNodesConnection, data);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
