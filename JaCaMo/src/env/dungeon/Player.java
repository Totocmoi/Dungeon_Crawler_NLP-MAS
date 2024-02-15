package dungeon;

import com.fasterxml.jackson.databind.JsonSerializer;
import org.json.JSONObject;

// JaCaMo
import jason.asSyntax.Literal;
import cartago.Artifact;
import cartago.OPERATION;
import cartago.OpFeedbackParam;
import websocket.WsClient;

// Java Sets
import java.util.Arrays; 
import java.util.Set;

import java.util.HashSet;

public class Player extends Artifact{

    // Global default variables
    private final String movePlayerIntentName = "move_player";
    private final String moveCameraIntentName = "move_camera";
    private final String takeObjectIntentName = "take_object";
    private final String openChestIntentName = "open_chest";
    private final String hitIntentName = "hit";
    private final String avoidIntentName = "avoid";
    private final String lootIntentName = "loot";
    private final String restIntentName = "rest";
    private final String seeIntentName = "see";
    private final String dieIntentName = "die";
    private final String address = "127.0.0.1";
    private final int port = 9080;
    Set<String> relatives = new HashSet<>(Arrays.asList("left of", "right of", "front of", "behind of", "on"));
    Infos infos = new Infos();

    public String sendRequest(Infos newInfos) throws Exception {
        String payload = newInfos.buildPayload();
        System.out.println("[DUNGEON] Connection on 127.0.0.1:9080");
        WsClient client = new WsClient();
        client.startConnection(address, port);
        StringBuilder answer = new StringBuilder(client.sendMessage(payload));
        System.out.println("[DUNGEON] Get Answer: " + answer.toString());
        client.stopConnection();
        return answer.toString();
    }

    Infos organizeInfo(JSONObject objInfo){
        System.out.println("[DUNGEON] organizeInfo");
        Infos newInfos = new Infos();
        if (objInfo.has("intent")){
            switch (objInfo.getString("intent")){
                case movePlayerIntentName:
                    newInfos.movPlay = true;
                    break;
                case moveCameraIntentName:
                    newInfos.movCam = true;
                    break;
                case takeObjectIntentName:
                    newInfos.take = true;
                    break;
                case openChestIntentName:
                    newInfos.open = true;
                    break;
                case hitIntentName:
                    newInfos.hit = true;
                    break;
                case avoidIntentName:
                    newInfos.avoid = true;
                    break;
                case lootIntentName:
                    newInfos.loot = true;
                    break;
                case restIntentName:
                    newInfos.rest = true;
                    break;
                case seeIntentName:
                    newInfos.see = true;
                    break;
                case dieIntentName:
                    newInfos.die = true;
                    break;
                default:
                    System.out.println("[DUNGEON] ERROR: Intent name not available: " +objInfo.getString("intent") );
            }
        } else{
            System.out.println("[DUNGEON] ERROR: Intent name not provided");
        }
        if (objInfo.has("obj") && !objInfo.isNull("obj"))
            newInfos.objName = objInfo.getString("obj");
        if (objInfo.has("dir") && !objInfo.isNull("dir"))
            newInfos.direction = objInfo.getString("dir");
        if (objInfo.has("time") && !objInfo.isNull("time"))
            newInfos.time = objInfo.getString("time");
        return newInfos;
    }
        
    @OPERATION
    void move_player(String dir){
        JSONObject JsonInfo = new JSONObject();
        JsonInfo.put("intent", movePlayerIntentName);
        JsonInfo.put("dir", dir);
        Infos newInfos = organizeInfo(JsonInfo);
        try{
            sendRequest(newInfos);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @OPERATION
    void move_camera(String dir){
        JSONObject JsonInfo = new JSONObject();
        JsonInfo.put("intent", moveCameraIntentName);
        JsonInfo.put("dir", dir);
        Infos newInfos = organizeInfo(JsonInfo);
        try{
            sendRequest(newInfos);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @OPERATION
    void take_object(String objName){
        JSONObject JsonInfo = new JSONObject();
        JsonInfo.put("intent", takeObjectIntentName);
        JsonInfo.put("obj", objName);
        Infos newInfos = organizeInfo(JsonInfo);
        System.out.println("[DUNGEON] newInfos: " + newInfos.buildPayload());
        try{
            sendRequest(newInfos);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @OPERATION
    void open_chest(){
        JSONObject JsonInfo = new JSONObject();
        JsonInfo.put("intent", openChestIntentName);
        Infos newInfos = organizeInfo(JsonInfo);
        System.out.println("[DUNGEON] info organized");
        try{
            System.out.println("[DUNGEON] sending request");
            sendRequest(newInfos);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @OPERATION
    void hit(){
        JSONObject JsonInfo = new JSONObject();
        JsonInfo.put("intent", hitIntentName);
        Infos newInfos = organizeInfo(JsonInfo);
        System.out.println("[DUNGEON] info organized");
        try{
            System.out.println("[DUNGEON] sending request");
            sendRequest(newInfos);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @OPERATION
    void avoid(){
        JSONObject JsonInfo = new JSONObject();
        JsonInfo.put("intent", avoidIntentName);
        Infos newInfos = organizeInfo(JsonInfo);
        System.out.println("[DUNGEON] info organized");
        try{
            System.out.println("[DUNGEON] sending request");
            sendRequest(newInfos);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @OPERATION
    void loot(OpFeedbackParam<Literal> result){
        JSONObject JsonInfo = new JSONObject();
        JsonInfo.put("intent", lootIntentName);
        Infos newInfos = organizeInfo(JsonInfo);
        System.out.println("[DUNGEON] info organized");
        try{
            System.out.println("[DUNGEON] sending request");
            result.set(Literal.parseLiteral(sendRequest(newInfos).replaceAll("\\P{Print}", "")));
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @OPERATION
    void rest(String time, Byte N, OpFeedbackParam<Byte> result){
        JSONObject JsonInfo = new JSONObject();
        JsonInfo.put("intent", restIntentName);
        JsonInfo.put("time", time);
        Infos newInfos = organizeInfo(JsonInfo);
        System.out.println("[DUNGEON] info organized");
        byte NN = N;
        if (time.equals("a_little")){ NN= (byte) (NN+5);}
        if (time.equals("more")){ NN= (byte) (NN+10);}
        if (time.equals("until_full")){ NN= (byte) (NN+20);}
        try{
            System.out.println("[DUNGEON] sending request");
            sendRequest(newInfos);
            result.set((byte) Integer.min(NN,20));
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @OPERATION
    void see(OpFeedbackParam<Literal[]> result){
        JSONObject JsonInfo = new JSONObject();
        JsonInfo.put("intent", seeIntentName);
        Infos newInfos = organizeInfo(JsonInfo);
        System.out.println("[DUNGEON] info organized");
        try{
            System.out.println("[DUNGEON] sending request");
            String[] a = sendRequest(newInfos).replaceAll("\\P{Print}", "").split(" ");
            Literal[] b = new Literal[a.length];
            for (int i = 0; i < a.length; i++) {
                b[i] = Literal.parseLiteral(a[i]);
            }
            System.out.println("[DUNGEON] "+ Arrays.toString(a));
            result.set(b);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    void die(){
        JSONObject JsonInfo = new JSONObject();
        JsonInfo.put("intent", dieIntentName);
        Infos newInfos = organizeInfo(JsonInfo);
        System.out.println("[DUNGEON] info organized");
        try{
            System.out.println("[DUNGEON] sending request");
            sendRequest(newInfos);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    
}
