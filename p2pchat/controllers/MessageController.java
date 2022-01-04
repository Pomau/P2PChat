package p2pchat.controllers;

import p2pchat.module.ConnectionData;
import p2pchat.module.Message;
import p2pchat.module.People;
import p2pchat.server.Client;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class MessageController {
    public DB db = new DB();
    private String alfabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private Client client;

    public MessageController(Client client) {
        this.client = client;
    }

    public void createMessage(String text, People user, String toUser) {
        try {
            Message message = new Message();
            message.setText(text.toString());
            message.setToUserId(toUser);
            message.setFromUserId(user.getPublicKey());
            message.setTimeCreate(new Timestamp(new Date().getTime()));
            message.setId(generateId(90));
            message.setSignature(byteToString(createSignature(message, getPrivateKey(user.getPrivateKey()))));
            addMessage(message);

        } catch (Exception e) {
            System.out.println("Error create");
        }

    }

    public void checkMessages(List<Message> messsages) {
        for (Message message : messsages) {
            if (checkSignature(message)) {
                db.add(message);
            }
        }
    }

    public boolean checkNode(ConnectionData data) {
        return client.connect(data);
    }

    public List<Message> getMessagesAfterTimestamp(Timestamp time) {
        return db.getAfterDate(time);
    }

    public List<Message> getMessagesById(List<String> ids) {
        List<Message> messages = new ArrayList<>();
        for (String id : ids) {
            messages.add(db.getMessageById(id));
        }
        return messages;
    }

    public Integer getCountMessagesAfterTimestamp(Timestamp time) {
        return db.getAfterDateCount(time);
    }

    public List<String> getChats(String id) {
        return db.getChats(id);
    }

    public List<Message> getMessagesByChat(String chat, String me) {
        return db.getMessagesByChat(chat, me);
    }


    public void addMessage(Message message) {
        if (checkSignature(message) && db.add(message)) {
            client.sendMessage(message);
        }
    }

    public boolean checkSignature(Message message) {
        try {
            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(message.getFromUserId()));
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PublicKey publicKeypublicKey = keyFactory.generatePublic(publicKeySpec);
            ecdsaVerify.initVerify(publicKeypublicKey);
            ecdsaVerify.update(message.textForSignature().getBytes("UTF-8"));
            return ecdsaVerify.verify(Base64.getDecoder().decode(message.getSignature()));
        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }

    public PrivateKey getPrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encoded = Base64.getDecoder().decode(key);
        KeyFactory kf = KeyFactory.getInstance("EC");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return kf.generatePrivate(keySpec);
    }

    public KeyPair createNewKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        g.initialize(ecSpec, new SecureRandom());
        return g.generateKeyPair();
    }

    public byte[] createSignature(Message message, PrivateKey privateKey) throws NoSuchAlgorithmException,
            InvalidKeyException, UnsupportedEncodingException, SignatureException {
        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(message.textForSignature().getBytes("UTF-8"));
        return ecdsaSign.sign();

    }

    public String byteToString(byte[] arr) {
        return Base64.getEncoder().encodeToString(arr);
    }

    private String generateId(int len) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(alfabet.charAt(rnd.nextInt(alfabet.length())));
        return sb.toString();
    }
}