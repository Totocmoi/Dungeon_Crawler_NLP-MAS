package dungeon;

import org.json.JSONObject;

public class Infos{

    // Fields
    public boolean movPlay = false;
    public boolean movCam = false;
    public boolean take = false;
    public boolean open = false;
    public boolean hit = false;
    public boolean avoid = false;
    public boolean loot = false;
    public boolean rest = false;
    public boolean see = false;
    public boolean die = false;
    public String direction = "";         // direction of the motion
    public String time= "";         // time (only for rest)
    public String objName= "";      // name of the object

    // Reset all the values to empty string
    public void reset(){
        this.movPlay = false;
        this.movCam = false;
        this.hit = false;
        this.take = false;
        this.avoid = false;
        this.open = false;
        this.loot = false;
        this.rest = false;
        this.see = false;
        this.die = false;
        this.objName = "";
        this.time = "";
        this.direction = "";
    }

    public String buildPayload(){
        JSONObject payload_json = new JSONObject();
        payload_json.put("movPlay", this.movPlay);
        payload_json.put("movCam", this.movCam);
        payload_json.put("take", this.take);
        payload_json.put("open", this.open);
        payload_json.put("hit", this.hit);
        payload_json.put("avoid", this.avoid);
        payload_json.put("loot", this.loot);
        payload_json.put("rest", this.rest);
        payload_json.put("see", this.see);
        payload_json.put("die", this.die);
        payload_json.put("objName", this.objName);
        payload_json.put("time", this.time);
        payload_json.put("direction", this.direction);
        return payload_json.toString();
    }
}