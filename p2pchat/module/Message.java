package p2pchat.module;

import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.Timestamp;

public class Message {
    private String id;
    private String text;
    private String toUserId;
    private String fromUserId;
    private Timestamp timeCreate;
    private String signature;

    public Message() {}

    public Message(Message mess) {
        this.id = mess.getId();
        this.text = mess.getText();
        this.toUserId = mess.getToUserId();
        this.fromUserId = mess.getFromUserId();
        this.timeCreate = mess.getTimeCreate();
        this.signature = mess.getSignature();
    }

    public Message(JSONObject mess) {
        this.id = (String) mess.get("id");
        this.text =  (String) mess.get("text");
        this.toUserId =  (String) mess.get("toUserId");
        this.fromUserId =  (String) mess.get("fromUserId");
        this.timeCreate = Timestamp.valueOf((String) mess.get("timeCreate"));
        this.signature =  (String) mess.get("signature");
    }

    public Message(ResultSet rs) {
        try {
            this.id = rs.getString(1);
            this.text = rs.getString(2);
            this.toUserId = rs.getString(3);
            this.fromUserId = rs.getString(4);
            this.timeCreate = rs.getTimestamp(5);
            this.signature = rs.getString(6);
        } catch (Exception e) {
            System.out.println("Error create model");
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Timestamp getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(Timestamp timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String textForSignature() {
        return id + text + toUserId + fromUserId;
    }

    @Override
    public String toString() {
        JSONObject result = new JSONObject();
        result.put("id", id);
        result.put("text", text);
        result.put("toUserId", toUserId);
        result.put("fromUserId", fromUserId);
        result.put("timeCreate", timeCreate.toString());
        result.put("signature", signature);

        return result.toJSONString();
    }
}
