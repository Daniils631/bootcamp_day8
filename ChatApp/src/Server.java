import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{

    private static ArrayList<ConnectionHandler> connections;
    private static ServerSocket server;
    public static boolean done;
    private static ExecutorService pool;

    public  Server(){
        connections = new ArrayList<>();
        done = false;
    }


    @Override
    public static void run(){
        try {

            server = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public static void broadcast(String message){
        for (ConnectionHandler ch: connections){
            if (ch!=null){
                ch.sendMessage(message);
            }
        }
    }

    public static void shutdown(){
        try {
            done = true;
            if (!server.isClosed()){
                server.close();
            }
            for (ConnectionHandler ch : connections){
                ch.shutdown();
            }
        } catch (IOException e){
            // ignore
        }


    }
    static class ConnectionHandler implements Runnable{

        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname;

        public ConnectionHandler(Socket client){
            this.client = client;
        }

        @Override
        public void run() {
            try {

                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out.println("Please enter a nickname: ");
                nickname = in.readLine();
                System.out.println(nickname + " connected!");
                broadcast(nickname + " joined the chat!");
                String message;
                while ((message = in.readLine()) !=null){
                    if (message.startsWith("/nick ")){
                        String[] messageSplit = message.split(" ", 2);
                        if (messageSplit.length ==2 ){
                            broadcast(nickname + " renamed themselves to " + messageSplit[1]);
                            System.out.println(nickname + " renamed themselves to " + messageSplit[1]);
                            nickname = messageSplit[1];
                            out.println("Successfuly changed nickname to " + nickname);

                        }else{
                            out.println("No nickname provided!");
                        }

                    }else if (message.startsWith("/quit")){
                        broadcast(nickname + " left the chat!" );
                        shutdown();
                    }else {
                        broadcast(nickname + ": " + message);
                    }
                }

            } catch (Exception e) {
                shutdown();
            }

        }
        public void sendMessage(String message){
            System.out.println(message);
        }
        public void shutdown(){
            try {
                in.close();
                out.close();
                if (client.isClosed()){
                    client.close();
                }
            }catch (IOException e){
            // ignore
            }

        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        Server.run();
    }
}
