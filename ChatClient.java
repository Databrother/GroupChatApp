import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
private Socket socket;
private BufferedWriter bufferedWriter;
private BufferedReader bufferedReader;
private String userId;

    public ChatClient(Socket socket, String userId) {
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter
                    (new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userId = userId;
        } catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    public void sendMessage(){
      try{
          bufferedWriter.write(userId);
          bufferedWriter.newLine();
          bufferedWriter.flush();
          Scanner scanner =new Scanner(System.in);
          while(socket.isConnected()){
              String messageToSend = scanner.nextLine();
              bufferedWriter.write(userId +": " + messageToSend);
              bufferedWriter.newLine();
              bufferedWriter.flush();
          }
      }catch (IOException e){
          closeEverything(socket,bufferedReader,bufferedWriter);
      }
    }
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
             String messageFromChat;
             while(socket.isConnected()){
                 try{
                     messageFromChat = bufferedReader.readLine();
                     System.out.println(messageFromChat);
                 }catch( IOException e){
                     closeEverything(socket, bufferedReader, bufferedWriter);
                 }
             }
            }
        }).start();
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader!= null){
                bufferedReader.close();
            }
            if(bufferedWriter!= null){
                bufferedWriter.close();
            }
            if(socket!= null){
                socket.close();
            }
        }catch(IOException e){
            e.getStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your UserId for the group chat");
        String userId = scanner.nextLine();
        Socket socket = new Socket("localHost",2000);
        ChatClient client = new ChatClient(socket, userId);
        client.listenForMessage();
        client.sendMessage();
    }
}