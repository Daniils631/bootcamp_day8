package Threads;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MyThreads myThreads = new MyThreads();

        Thread t1 = new Thread(myThreads);
        t1.start();


        Thread t2  = new Thread(myThreads);
        t2.start();

        Thread.sleep(1000);
        System.out.println(myThreads.getX());
    }

}

class MyThreads extends Thread{
    int x =0;
    @Override
    public synchronized void run(){
        for (int i = 0; i<10000; i++){
            int temp = x; // temp = 0 // temp = 0
            temp++; // temp =1 // temp =1
            x=temp; // x=1
        }
    }
    public int getX(){
        return x;
    }
}