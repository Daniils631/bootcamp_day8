package Threads;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello world! " + Thread.currentThread().getName());

        MyThreads myThreads = new MyThreads();
        myThreads.start();

        //Thread.sleep(1000);// potok main ostanavlivaetsja na 1 sekundu poesle nacalo 1 potoka i poka on zdet 1 potok uze sdelaetsja
            myThreads.join();// garantija cto poka 1 potok ne sdelaet vtoroij nenacnetsja

        MyThreadsRunnable r1 = new MyThreadsRunnable();
        Thread t2 = new Thread(r1);
        t2.start();

        System.out.println("Main Thread is dead");

    }
}

class MyThreads extends Thread{
    @Override
    public void run(){
        for (int i =0; i<100; i++)
        System.out.println(i + " "  + Thread.currentThread().getName());
        //Thread.yield(); // ustupaet svoe mesto drugomu potoku no nevsegda
        }
    }

    class MyThreadsRunnable implements Runnable{

        @Override
        public void run() {
            for (int i =0; i<100; i++)
            System.out.println(i + " "  + Thread.currentThread().getName());
        }
    }