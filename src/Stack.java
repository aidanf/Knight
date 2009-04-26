import waba.ui.*;
import waba.fx.*;
import waba.sys.*;
import waba.util.*;

class Node{
    Object contents;
    Object next;

    public Node(){
	contents=null;
	next=null;
    }
    public Node(Object c){
	contents=c;
	next=null;
    }
    public Node(Object c,Object n){
	contents=c;
	next = n;
    }
    public Object contents(){
	return contents;
    }
    public Object next(){
	return next;
    }
}

public class Stack{
    int length;
    Node top;

    public Stack(){
	top = new Node();
	length = 0;
    }
    public void push(Object o){
	Node n = new Node(o,top);
	top = n;
	length++;
    }
    public Object pop(){
	Node n;
	n = top;
	top = (Node)n.next();
	length--;
	return n.contents();
    }
    public Object top(){
	return top.contents();
    }
    public int length(){
	return length;
    }
}
