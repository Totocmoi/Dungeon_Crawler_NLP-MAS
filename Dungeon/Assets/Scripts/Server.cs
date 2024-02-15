using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Net.Sockets;
using System.Net;
using UnityEngine;
using System;
using Unity.VisualScripting;

public class Server : MonoBehaviour
{
    public int PORT = 9080;
    public string ADDRESS = "127.0.0.1";

    private TcpListener server;
    public PlayerJSP player;
    
    
    // Called when the script instance is being loaded.
    void Start()
    {
        server = new TcpListener(IPAddress.Parse(ADDRESS), PORT);
        server.Start();
        Monster[] foes = GameObject.FindObjectsOfType<Monster>();
        int i = 1;
        foreach (var item in foes)
        {
            item.ADDRESS = ADDRESS;
            item.port = PORT + i;
            i++;
        }
    }

    // Update is called once per frame
    void Update()
    {
        if (server.Pending())
        {
            TcpClient client = server.AcceptTcpClient();
            if (client.Available > 0)
            {
                NetworkStream stream = client.GetStream(); 
                StreamReader reader = new StreamReader(stream);
                string instructionString = reader.ReadLine();
                Debug.Log(instructionString);
                Instruction instruction = JsonUtility.FromJson<Instruction>(instructionString);
                StreamWriter writer = new StreamWriter(stream);
                if (instruction.movPlay)
                {
                    if (instruction.direction == "forward") { player.target += player.transform.forward.normalized * 8; }
                    if (instruction.direction == "right") { player.target += player.transform.right.normalized * 8; }
                    if (instruction.direction == "left") { player.target += player.transform.right.normalized * -8; }
                    if (instruction.direction == "backward") { player.target += player.transform.forward.normalized * -8; }
                    writer.Write("Ok\n");
                }
                if (instruction.movCam)
                {
                    if (instruction.direction == "right") { player.MoveCamera(0, 90); }
                    if (instruction.direction == "left") { player.MoveCamera(0, -90); }
                    if (instruction.direction == "backward") { player.MoveCamera(0, 180); }
                    if (instruction.direction == "up") { player.MoveCamera(-25, 0); }
                    if (instruction.direction == "down") { player.MoveCamera(25, 0); }
                    if (instruction.direction == "around") { player.MoveCamera(0, 360); }
                    writer.Write("Ok\n");
                }
                if (instruction.take)
                {
                    writer.Write("Ok\n");
                }
                if (instruction.open)
                {
                    writer.Write("Ok\n");
                }
                if (instruction.hit)
                {
                    player.hit();
                    writer.Write("Ok\n");
                }
                if (instruction.avoid)
                {
                    writer.Write("Ok\n");
                }
                if (instruction.loot)
                {
                    writer.Write("Nothing lol let me dev pls\n");
                }
                if (instruction.rest)
                {
                    writer.Write("Ok\n");
                }
                if (instruction.see)
                {
                    writer.Write(player.lookAround()+"\n");
                }
                writer.Flush();
                server.Stop();
                server.Start();
            }
        }
    }

}
[Serializable]
public class Instruction
{
    public bool take;
    public bool rest;
    public bool hit;
    public bool movCam;
    public bool loot;
    public bool avoid;
    public string objName;
    public string time;
    public bool movPlay;
    public bool open;
    public string direction;
    public bool see;
}