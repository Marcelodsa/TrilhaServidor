import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Server {
    private ArrayList<MessageManager> messageManagers;
    private ArrayList<Socket> clientsSocket;
    private ServerSocket server;
    private int numberOfConnections;
    boolean running;

    private void initServer() {
        try {
            server = new ServerSocket(1234);
        } catch (IOException e) {
            System.out.println("Falha ao iniciar socket");
            e.printStackTrace();
        }
    }

    public void waitConnections() {
        while (numberOfConnections != 2 && numberOfConnections < 2) {
            System.out.println("Aguardando conexao!");
            try {
                Socket clientSocket = server.accept();
                System.out.println("Conexao recebida em: " + clientSocket.getInetAddress().getHostAddress());
                numberOfConnections++;
                clientsSocket.add(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            running = false;
        }
    }

    public void run(){
        numberOfConnections = 0;
        initServer();
        clientsSocket = new ArrayList<>();
        messageManagers = new ArrayList<>();
        try {
            while(true){
                if(!running){
                    waitConnections();
                    running = true;
                    MessageManager messageManagerClient1= new MessageManager(clientsSocket.get(0), clientsSocket.get(1), this);
                    MessageManager messageManagerClient2= new MessageManager(clientsSocket.get(1), clientsSocket.get(0), this);
                    messageManagers.add(messageManagerClient1);
                    messageManagers.add(messageManagerClient2);
                    randomColor();
                }
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void randomColor(){
        Random random = new Random();
        boolean var= random.nextBoolean();
        if(var){
            messageManagers.get(0).setClientColor("RED");
            messageManagers.get(1).setClientColor("GREEN");
            messageManagers.get(0).setOpponentColor("GREEN");
            messageManagers.get(1).setOpponentColor("RED");
        } else{
            messageManagers.get(1).setClientColor("RED");
            messageManagers.get(0).setClientColor("GREEN");
            messageManagers.get(1).setOpponentColor("GREEN");
            messageManagers.get(0).setOpponentColor("RED");
        }
    }

    public void setPlayerToStart(){
        Random random = new Random();
        boolean var= random.nextBoolean();
        if(var){
            messageManagers.get(0).notifyPlayerToStart();
        } else{
            messageManagers.get(1).notifyPlayerToStart();
        }
    }
}
