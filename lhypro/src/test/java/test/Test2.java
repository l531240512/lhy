package test;

/**
 * 项目名字：lhy
 *
 * @author 作者: lihuiyang 功能描述：1. 创建时间: 2020/4/5 所属公司：lhy
 */
public class Test2 {
    public static void main(String[] args) {
        Node node = new Node(0);
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        node.setNext(node1);
        node1.setNext(node2);
        node2.setNext(node3);
        System.out.println(node);
        ss(node);
        System.out.println(node);
    }

    public static void ss(Node node){
        Node curr = node.next;
        Node next = null;
        Node newN = new Node(10);
        while (curr != null){
            next = curr.next;
            curr.next = newN.next;
            newN.next = curr;
            curr = next;
        }
        node.next = newN.next;
    }
}
class Node {
    public int id;
    public Node next;
    public Node(int id){
        this.id = id;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                "}";
    }
}
