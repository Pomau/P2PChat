package p2pchat.module;


import org.json.simple.JSONObject;

public class ConnectionData {
    private String ip;
    private int port;

    public ConnectionData(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public ConnectionData(ConnectionData data) {
        this.ip = data.getIp();
        this.port =  data.getPort();
    }

    public ConnectionData(String data) {
        String[] dataSplit = data.split(":");
        this.ip = dataSplit[0];
        this.port = Integer.parseInt(dataSplit[1]);
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        JSONObject result = new JSONObject();
        result.put("ip", ip);
        result.put("port", port);
        return result.toJSONString();
    }
}