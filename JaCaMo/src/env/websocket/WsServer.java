package websocket;

import cartago.Artifact;
import cartago.OpFeedbackParam;
import cartago.OPERATION;
import jason.asSyntax.Literal;

import org.glassfish.tyrus.server.Server;
import org.json.JSONObject;

import javax.swing.plaf.synth.SynthTextAreaUI;

public class WsServer extends Artifact{
	private int PORT = 5002;

	// The method listen is called by the agent and sets the literals depending on the user input.
	@OPERATION
	public void listen(OpFeedbackParam<Literal> intent, OpFeedbackParam<Literal> obj, OpFeedbackParam<Literal> dir, OpFeedbackParam<Literal> time)  throws Exception{
		
		// The server creates an endpoint that will manage the connections
		Endpoint endpoint = new Endpoint();
		Server server = new Server("localhost", PORT, "/websockets", null, endpoint.getClass());

		try {
			// The server is started and waits for a message from the user
			server.start();
			System.out.println("[SOCKET SERVER] Server is running");
			while(null == endpoint.resultString){
				Thread.sleep(10);
			}
			// Create a json object from the string received in input
			JSONObject info = new JSONObject(endpoint.resultString);
			// Set all the literal values
			if (info.has("intent"))
				intent.set(Literal.parseLiteral(info.getString("intent")));
			if (info.has("obj") && !info.isNull("obj"))
				obj.set(Literal.parseLiteral(info.getString("obj")));
			if (info.has("dir") && !info.isNull("dir"))
				dir.set(Literal.parseLiteral(info.getString("dir")));
			if (info.has("time") && !info.isNull("time"))
				time.set(Literal.parseLiteral(info.getString("time").replaceAll(" ", "_")));
			// reset the user string
			endpoint.resultString = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.stop();
		}
	}

}