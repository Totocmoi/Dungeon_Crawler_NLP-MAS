using System.Collections;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Net;
using UnityEngine;
using System.IO;
using System.Threading;

public class Monster : MonoBehaviour
{
    // Start is called before the first frame update
    public int port;
    public string ADDRESS;
    private TcpListener server;
    public Vector3 target;
    private Animator animator;
    private float moveSpeed = 5f;
    public int damage = 0;
    private PlayerJSP player;

    void Start()
    {
        port = -1;
        animator = GetComponentInChildren<Animator>();
        StartCoroutine(WaitForPortInitialization());
        target = transform.position;
    }

    // Update is called once per frame
    void Update()
    {
        if (Vector3.Distance(transform.position, target) > 0.1f)
        {
            transform.LookAt(target);
            transform.position = transform.position + (target - transform.position).normalized * moveSpeed * Time.deltaTime;
     
        }
        if (server.Pending())
        {
            TcpClient client = server.AcceptTcpClient();
            if (client.Available > 0)
            {
                NetworkStream stream = client.GetStream();
                StreamReader reader = new StreamReader(stream);
                string instructionString = reader.ReadLine();
                Debug.Log(instructionString);
                InstructionMonster instruction = JsonUtility.FromJson<InstructionMonster>(instructionString);
                StreamWriter writer = new StreamWriter(stream);
                if (instruction.type == "move")
                {
                    if (instruction.direction == "forward") { target += transform.forward.normalized * 8; }
                    if (instruction.direction == "right") { target += transform.right.normalized * 8; }
                    if (instruction.direction == "left") { target += transform.right.normalized * -8; }
                    if (instruction.direction == "backward") { target += transform.forward.normalized * -8; }
                    writer.Write("Ok\n");
                    target.x = Mathf.Round(target.x / 8) * 8;
                    target.y = Mathf.Round(target.y / 8) * 8;
                    target.z = Mathf.Round(target.z / 8) * 8;
                }
                if (instruction.type== "hit")
                {
                    hit(3);
                    writer.Write("Ok\n");
                }
                if (instruction.type == "look")
                {
                    writer.Write(lookAround());
                }
                if (instruction.type == "die")
                {
                    writer.Write("Ok\n");
                    Destroy(this);
                }
                writer.Flush();
                server.Stop();
                server.Start();
            }
        }
    }

    IEnumerator WaitForPortInitialization()
    {
        while (port == -1)
        {
            yield return null;
        }
        server = new TcpListener(IPAddress.Parse(ADDRESS), port);
        server.Start();
        Debug.Log("La variable port est initialisée : " + port);
    }
    private string lookAround()
    {
        string a = "";
        if (player!=null) { a += "player(here) "; }
        else
        {
            int maskPlay = 1 << 9 | 1 << 6;
            RaycastHit hitForwardPlay;
            if (Physics.Raycast(transform.position, transform.forward, out hitForwardPlay, 24f, maskPlay))
            {
                if (hitForwardPlay.collider.CompareTag("Player")) a += "player(forward) ";
            }
            RaycastHit hitBackwardPlay;
            if (Physics.Raycast(transform.position, -transform.forward, out hitBackwardPlay, 24f, maskPlay))
            {
                if (hitBackwardPlay.collider.CompareTag("Player")) a += "player(backward) ";
            }
            RaycastHit hitRightPlay;
            if (Physics.Raycast(transform.position, transform.right, out hitRightPlay, 24f, maskPlay))
            {
                if (hitRightPlay.collider.CompareTag("Player")) a += "player(right) ";
            }
            RaycastHit hitLeftPlay;
            if (Physics.Raycast(transform.position, -transform.right, out hitLeftPlay, 24f, maskPlay))
            {
                if (hitLeftPlay.collider.CompareTag("Player")) a += "player(left) ";
            }
        }
        int mask = 1 << 6;
        RaycastHit hitForward;
        if (Physics.Raycast(transform.position, transform.forward, out hitForward, 8f, mask))
        {
            a += "wall(forward) ";
        }
        RaycastHit hitBackward;
        if (Physics.Raycast(transform.position, -transform.forward, out hitBackward, 8f, mask))
        {
            a += "wall(backward) ";
        }
        RaycastHit hitRight;
        if (Physics.Raycast(transform.position, transform.right, out hitRight, 8f, mask))
        {
            a += "wall(right) ";
        }
        RaycastHit hitLeft;
        if (Physics.Raycast(transform.position, -transform.right, out hitLeft, 8f, mask))
        {
            a += "wall(left) ";
        }
        if (damage > 0) a += "damage(" + damage + ") ";
        damage= 0;
        return a + "\n";
    }
    private void hit(int damage) {
        if (player!=null) { player.damage += damage; }
    }

    void OnTriggerEnter(Collider other)
    {
        if (other.CompareTag("Player"))
        {
            player = other.gameObject.GetComponent<PlayerJSP>();
        }
    }
    private void OnTriggerExit(Collider other)
    {
        if (player.gameObject == other.gameObject) { player = null; }
    }
}
public class InstructionMonster
{
    public string type;
    public string direction;
}