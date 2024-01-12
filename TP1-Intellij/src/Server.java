import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

class Server  {
    public static void main(String[] args){
        Utilisateur user1 = new Utilisateur("latif","123");
        try{
            int port = 2121;
            ServerSocket server = new ServerSocket(port);
            System.out.println("Démarrage du serveur sur 127.0.0.1:"+port+".\r\nAttente d’une connexion...");

            Socket client = server.accept();
            OutputStream clientOut = client.getOutputStream();
            InputStream clientInput = client.getInputStream();
            Scanner scanner = new Scanner(clientInput);
            String username = "",pwd;

            //send to client that it is ready
            clientOut.write("220 service ready\n".getBytes());

            while(true){
                //ce qu'a ecrit le client
                String input = scanner.nextLine();

                switch (input.split(" ")[0]){

                    case "USER":
                        username = input.split(" ")[1];
                        System.out.println(input);
                        clientOut.write("331 User name received\r\n".getBytes());
                        break;
                    case "PASS":
                        pwd = input.split(" ")[1];
                        System.out.println(input);
                        Utilisateur user2 = new Utilisateur(username,pwd);
                        if(user1.isSame(user2)){
                            clientOut.write("230 User Logged IN\r\n".getBytes());
                        }else {
                            clientOut.write("430 Invalid username & password\r\n".getBytes());
                        }
                        break;
                    case "GET":
                        System.out.println(input);
                        break;
                    case "SIZE":
                        System.out.println("size returned");
                        clientOut.write("300 accepted.\r\n".getBytes());
                        break;
                    case "EPSV":
                        System.out.println("Entering Extended Passive Mode");
                        clientOut.write("229 Entering Extended Passive Mode (|||2122|).\r\n".getBytes());
                        break;
                    case "EPRT":
                        int portDonnee = 2122;
                        ServerSocket dataPort = new ServerSocket(portDonnee);
                        System.out.println("Entering Extended Passive Mode");
                        dataPort.accept();
                        dataPort.accept().getOutputStream().write("hhh".getBytes(StandardCharsets.UTF_8));
                        dataPort.close();
                        break;
                    case "QUIT":
                        System.out.println(input);
                        client.close();
                        server.close();
                        System.exit(0);
                    default:
                        clientOut.write("500 Command not implemented.\r\n".getBytes());
                        System.out.println(input+" 500 Command not implemented.");
                        break;
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
}