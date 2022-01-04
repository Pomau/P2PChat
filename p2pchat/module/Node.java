package p2pchat.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import p2pchat.module.ConnectionData;

public class Node {
    private List<ConnectionData> nodes = new ArrayList<>();
    private ConnectionData myNode;
    private int maxNodesConnection;

    public Node(int maxNodesConnection, ConnectionData data) {

        this.maxNodesConnection = maxNodesConnection;
        this.myNode = data;
    }

    public List<ConnectionData> getNodes() {
        return nodes;
    }

    public void setNodes(List<ConnectionData> nodes) {
        this.nodes = nodes;
    }

    public ConnectionData getMyNode() {
        return myNode;
    }

    public void setMyNode(ConnectionData myNode) {
        this.myNode = new ConnectionData(myNode);
    }

    public int getMaxNodesConnection() {
        return maxNodesConnection;
    }

    public void setMaxNodesConnection(int maxNodesConnection) {
        this.maxNodesConnection = maxNodesConnection;
    }

    public boolean connectionNode() {
        return (maxNodesConnection <= nodes.size());
    }

    public void addConnection(ConnectionData connect) {
        nodes.add(connect);
    }

    public void deleteConnection(ConnectionData connect) {
        nodes.remove(connect);
    }

    public void addConnection(String data) {
        addConnection(new p2pchat.module.ConnectionData(data));
    }

}