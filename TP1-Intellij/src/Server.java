import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

class Server  {
    public static void main(String[] args){
        Utilisateur user1 = new Utilisateur("USER latif","PASS 123");
        try{
            int port = 2121;
            ServerSocket server = new ServerSocket(port);

            System.out.println("Démarrage du serveur sur 127.0.0.1:"+port+".\r\nAttente d’une connexion...");
            Socket client = server.accept();
            OutputStream clientOut = client.getOutputStream();
            InputStream clientInput = client.getInputStream();
            Scanner scanner = new Scanner(clientInput);
            //send to client that it is ready
            clientOut.write("220 service ready\n".getBytes());
            //faire la trace de ce qu'on a reçu
            String username = scanner.nextLine();
            System.out.println(username);
            clientOut.write("331 User name received\n".getBytes());

            String pwd = scanner.nextLine();
            System.out.println(pwd);

            Utilisateur user2 = new Utilisateur(username,pwd);
            //si le user match
            if(user1.isSame(user2)){
                clientOut.write("230 User Logged IN\n".getBytes());
            }else {
                clientOut.write("230 User Not Logged \n".getBytes());
            }
            scanner.nextLine();
            client.close();

        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
}