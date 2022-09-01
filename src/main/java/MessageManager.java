import java.io.*;
import java.net.Socket;

public class MessageManager extends Thread{
    private Socket origin;
    private Socket destiny;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Server server;

    public MessageManager(Socket origin, Socket destiny, Server server) throws IOException {
        this.origin= origin;
        this.destiny= destiny;
        this.server= server;

        outputStream = new ObjectOutputStream(this.destiny.getOutputStream());
        inputStream = new ObjectInputStream(this.origin.getInputStream());

        this.start();
    }

    public void run() {
        while(true) {
            try {
                String messageReceived = (String) inputStream.readObject();
                if(messageReceived != null) {
                    System.out.println(messageReceived);

                    outputStream.writeObject(messageReceived);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void setClientColor(String color) {
        try {
            String clientColor= "MYCOL:"+color;
            outputStream.writeObject(clientColor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOpponentColor(String color) {
        try {
            String opponentColor= "OPCOL:"+color;
            outputStream.writeObject(opponentColor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notifyPlayerToStart(){
        try {
            String msg= "TURN:";
            outputStream.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
