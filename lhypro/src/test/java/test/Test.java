package test;

/**
 * 项目名字：lhy
 *
 * @author 作者: lihuiyang 功能描述：1. 创建时间: 2020/3/21 所属公司：
 */
public class Test {

    public static void main(String[] args) {
        Queue queue = new Queue(4);
        queue.addQueue(1);
        queue.addQueue(2);
        queue.addQueue(3);
        queue.getQueue();
        queue.getQueue();
        queue.addQueue(4);
        queue.addQueue(5);
        queue.addQueue(6);
        System.out.printf("头元素为：%d",queue.heedQueue());
        System.out.println();
        queue.showQueue();

    }
}

class Queue{
    //容器最大容量
    private int maxSize;
    //容器的第一个元素，初始值为0
    private int front;
    //容器的最后一个元素的后一位，初始值为0
    private int rear;
    private int[] arr;

    public Queue(int arrMaxSize){
        maxSize = arrMaxSize;
        arr = new int[maxSize];
    }

    public boolean isFull(){
        return (rear + 1) % maxSize == front;
    }

    public boolean isEmpty(){
        return rear == front;
    }

    public void addQueue(int n){
        if(isFull()){
            System.out.println("队列满，不能加入数据～");
            return;
        }
        arr[rear] = n;
        rear = (rear +1) % maxSize;
    }

    public int getQueue(){
        if(isEmpty()){
            throw new RuntimeException("队列空，不能获取数据～");
        }
        int value = arr[front];
        front = (front + 1) % maxSize;
        return value;
    }

    public void showQueue(){
        if(isEmpty()){
            System.out.println("队列空，没有数据～");
            return;
        }
        int size = size();
        for (int i=front; i<front + size; i++){
            System.out.printf("arr[%d]=%d\n",i % maxSize, arr[i % maxSize]);
        }
    }

    public int size(){
        return (rear + maxSize - front) % maxSize;
    }

    public int heedQueue(){
        if(isEmpty()){
            throw new RuntimeException("队列空，不能获取数据～");
        }
        return arr[front];
    }
}
