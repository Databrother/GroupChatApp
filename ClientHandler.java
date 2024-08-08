import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String userId;

    public ClientHandler(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            this.userId = bufferedReader.readLine();
            clientHandlers.add(this);
            broadCastMessage("SERVER: " + userId + " entered the chat");
        }catch (IOException e){
         closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while(socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                broadCastMessage(messageFromClient);

            } catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
    public void broadCastMessage(String messageToSend){
      for (ClientHandler clientHandler: clientHandlers){
          try{
              if(!clientHandler.userId.equals(userId)){
                  clientHandler.bufferedWriter.write(messageToSend);
                  clientHandler.bufferedWriter.newLine();
                  clientHandler.bufferedWriter.flush();
              }
          } catch(IOException e){
           closeEverything(socket, bufferedReader, bufferedWriter);
          }
      }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadCastMessage("Server: "+ userId + " has left the chat");
    }
    public void closeEverything(Socket socket,
                                BufferedReader bufferedReader,
                                BufferedWriter bufferedWriter){
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
}
