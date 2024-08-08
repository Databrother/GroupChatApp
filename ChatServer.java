import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private ServerSocket serverSocket;

    public ChatServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void startServer(){
        try {

            while(!serverSocket.isClosed()){
               Socket socket = serverSocket.accept();
               System.out.println("a new client has connected");
               ClientHandler clientHandler = new ClientHandler(socket);
               Thread thread = new Thread(clientHandler);
               thread.start();
            }
        } catch(Exception e ){
            e.getStackTrace();
        }
    }
    public void closeServerSocket(){
     try{
         if(serverSocket != null){
             serverSocket.close();
         }
     } catch(IOException e){
         e.getStackTrace();
     }
    }
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2000);
        ChatServer chatServer = new ChatServer(serverSocket);
        chatServer.startServer();
    }

}