package dungeon;

import org.json.JSONObject;

import cartago.Artifact;
import cartago.OPERATION;
import cartago.OpFeedbackParam;
import jason.asSyntax.Literal;
import websocket.WsClient;

import java.util.List;
import java.util.ArrayList;

public class Foe extends Artifact{

	@OPERATION
	public void hit(int port){
		JSONObject action_json = new JSONObject();
		action_json.put("type", "hit");
		WsClient client = new WsClient();
		try{
			client.startConnection("127.0.0.1", port);
			System.out.println("[MONSTER] Get Answer: " + client.sendMessage(action_json.toString()));
			client.stopConnection();
		}catch(Exception e){
			System.out.println(e);
		}
	}

	@OPERATION
	public void move(int port, String direction){
		JSONObject action_json = new JSONObject();
		action_json.put("type", "move");
		action_json.put("direction", direction);
		WsClient client = new WsClient();
		try{
			client.startConnection("127.0.0.1", port);
			System.out.println("[MONSTER] Get Answer: " + client.sendMessage(action_json.toString()));
			client.stopConnection();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	@OPERATION
	public void die(int port){
		JSONObject action_json = new JSONObject();
		action_json.put("type", "die");
		WsClient client = new WsClient();
		try{
			client.startConnection("127.0.0.1", port);
			System.out.println("[MONSTER] Dead successfully : " + client.sendMessage(action_json.toString()));
			client.stopConnection();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	@OPERATION
	public void look(int port, OpFeedbackParam<Literal[]> result){
		JSONObject request_json = new JSONObject();
		request_json.put("type", "look");

		// We send it to the monster body and get the result.
		WsClient client = new WsClient();
		try{
			client.startConnection("127.0.0.1", port);
			String[] a = client.sendMessage(request_json.toString()).replaceAll("\\P{Print}", "").split(" ");
			System.out.print("[MONSTER] Get Answer: ");
			Literal[] b = new Literal[a.length];
			for (int i = 0; i < a.length; i++) {
				if(a[i].length()>2) {
					b[i] = Literal.parseLiteral(a[i]);
					System.out.print(a[i]+" ");
				}
			}
			System.out.println();
			result.set(b);
			client.stopConnection();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
}
