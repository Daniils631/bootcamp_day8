package Threads;

//lock

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MyThreads2 myThreads2 = new MyThreads2();
        Thread t1 = new Thread(myThreads2);
        t1.start();
        Thread t2 = new Thread(myThreads2);
        t2.start();
        Thread.sleep(1000);

        System.out.println(myThreads2.getX());


    }
}

class MyThreads2 extends Thread{
    Lock lock = new ReentrantLock();
    int x = 0;
    public void run(){
        lock.lock();
        for (int i = 0; i<10000; i++)
            x++;
        lock.unlock();
    }
    public int getX(){
        return x;
    }
}