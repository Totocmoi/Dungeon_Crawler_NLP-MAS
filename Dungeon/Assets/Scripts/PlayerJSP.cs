using System.Collections;
using System.Collections.Generic;
using Unity.Mathematics;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.UIElements;

public class PlayerJSP : MonoBehaviour
{
    public Vector3 target;
    public float x, y;
    public float moveSpeed = 5f;
    private Animator animator;
    public int damage, degats;
    public Monster monster;
    private List<GameObject> loot;

    private int _animIDSpeed;
    private int _animIDGrounded;
    private int _animIDMotionSpeed;

    private void Start()
    {
        target = transform.position;
        animator = GetComponent<Animator>();
        AssignAnimationIDs();
        animator.SetBool(_animIDGrounded, true);
        animator.SetFloat(_animIDSpeed, 0f);
        animator.SetFloat(_animIDMotionSpeed, 1f);
        x=0; y=0;
        damage = 0;
        degats = 6;
        loot = new List<GameObject>();
    }


    private void Update()
    {
        if (Vector3.Distance(transform.position, target) > 0.1f)
        {
            transform.LookAt(target);
            transform.position += (target - transform.position).normalized * moveSpeed * Time.deltaTime;
            animator.SetFloat(_animIDSpeed, moveSpeed);
        }
        else { 
            animator.SetFloat(_animIDSpeed, 0f);
        }
        float x1 = 0,y1 = 0;
        if (x > 0.1) { x1 = Mathf.Min(moveSpeed * Time.deltaTime * 10, x); x -= x1; }
        if (y > 0.1) { y1 = Mathf.Min(moveSpeed * Time.deltaTime * 10, y); y -= y1; }
        if (x < -0.1) { x1 = Mathf.Max(-moveSpeed * Time.deltaTime * 10, x); x -= x1; }
        if (y < -0.1) { y1 = Mathf.Max(-moveSpeed * Time.deltaTime * 10, y); y -= y1; }
        transform.Find("FocalPoint").Rotate(0, y1, 0);
        transform.Find("FocalPoint").Find("FocalPoint2").Rotate(x1,0,0);
    }

    private void AssignAnimationIDs()
    {
        _animIDSpeed = Animator.StringToHash("Speed");
        _animIDGrounded = Animator.StringToHash("Grounded");
        _animIDMotionSpeed = Animator.StringToHash("MotionSpeed");
    }

    public void MoveCamera(float x1,float y1)
    {
        x = x1;
        y = y1;
    }

    public string lookAround()
    {
        string a = "";
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
            a+="wall(right) ";
        }
        RaycastHit hitLeft;
        if (Physics.Raycast(transform.position, -transform.right, out hitLeft, 8f, mask))
        {
            a += "wall(left) ";
        }
        if (monster != null) { a += "monster(here) "; }

        for (int i = 0; i<loot.Count; i++) { a += "object(" + loot[i].name + ") "; }
        if (damage != 0) { a += "damage(" + damage + ") "; damage = 0; }
        return a;
    }
    void OnTriggerEnter(Collider other)
    {
        if (other.CompareTag("Foe"))
        {
            monster = other.gameObject.GetComponent<Monster>();
        }
        if (other.CompareTag("Object"))
        {
            loot.Add(other.gameObject);
        }
    }
    private void OnTriggerExit(Collider other)
    {
        if (monster != null ) if (monster.gameObject == other.gameObject) { monster = null; }
        if (loot.Contains(other.gameObject)) { loot.Remove(other.gameObject);}
    }

    public void hit() {
        if (monster != null)
        {
            monster.damage += degats;
        }
    }

}
