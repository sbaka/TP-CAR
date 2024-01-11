import java.net.ServerSocket;
import java.net.Socket;

class Server  {
    public static void main(String[] args){
        try{
            int port = 2121;
            ServerSocket server = new ServerSocket(port);

            System.out.println("Démarrage du serveur sur 127.0.0.1:"+port+".\r\nAttente d’une connexion...");

            Socket client = server.accept();

            System.out.println("Un client s’est connecté.");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
}