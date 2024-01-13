import java.io.*;
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

            Socket dataTransferSocket = null;
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
                        System.out.println("GET message requested");
                        break;
                    case "SIZE":
                        System.out.println(input);
                        final File f = new File("./filesToSend");
                        clientOut.write(("226 "+f.length()+".\r\n").getBytes());
                        break;
                    case "PORT":
                        String addressString = input.split(" ")[1];
                        final String[] addressArray = addressString.split(",");
                        if (addressArray.length != 6) {
                            clientOut.write("425 Can't open data connection.\r\n".getBytes(StandardCharsets.UTF_8));
                            return;
                        }
                        final String address = addressArray[0] + "." + addressArray[1]
                                + "." + addressArray[2] + "." + addressArray[3];
                        final int dataPort = Integer.parseInt(addressArray[4]) * 256
                                + Integer.parseInt(addressArray[5]);

                        try {
                            dataTransferSocket =
                                    new Socket(address, dataPort);
                            clientOut.write("227 Entering Active Mode\r\n".getBytes(StandardCharsets.UTF_8));
                        } catch (final IOException e) {
                            throw new Exception(
                                    "Unable to create TransferServer active mode", e);
                        }
                        break;
                    case "RETR":
                        String fileName = input.split(" ")[1];
                        if(dataTransferSocket==null){
                            clientOut.write("443 No data connection\r\n".getBytes(StandardCharsets.UTF_8));
                        }else{
                            clientOut.write("150 Accepted data connection\r\n".getBytes(StandardCharsets.UTF_8));
                            try(final FileInputStream fileInputStream =
                                        new FileInputStream("/home/latif/Desktop/TP-CAR/TP1-Intellij/src/filesToSend/"+fileName);) {

                                final BufferedOutputStream outBuffer = new BufferedOutputStream(
                                        dataTransferSocket.getOutputStream());

                                final byte[] buffer = new byte[512];
                                int l;
                                while ((l = fileInputStream.read(buffer)) > 0) {
                                    outBuffer.write(buffer, 0, l);
                                }
                                // Flush data
                                outBuffer.flush();

                                // Close the connection
                                dataTransferSocket.close();
                                clientOut.write("226 File successfully transferred\r\n".getBytes(
                                        StandardCharsets.UTF_8));
                            } catch (final IOException e) {
                                clientOut.write("426 Unable to send file\r\n".getBytes(
                                        StandardCharsets.UTF_8));
                                throw new Exception(e.getMessage());
                            }
                        }
                        break;
                    case "QUIT":
                        System.out.println(input);
                        client.close();
                        server.close();
                        System.exit(0);
                    default:
                        clientOut.write("500 Command not implemented.\r\n".getBytes());
                        System.out.println(input+"----");
                        break;
                }
            }
        }catch(Throwable e){
            System.out.println(e.getMessage());
        }

    }
}