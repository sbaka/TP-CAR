import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void firstCase() throws UnknownHostException, IOException {
        Socket serverSocket = new Socket("localhost", 2121);
        System.out.println("Premier cas: Connecté au serveur");

        OutputStream out = serverSocket.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

        String serverResponse = in.readLine();
        System.out.println("Réponse du serveur: " + serverResponse);

        if (serverResponse.contains("service ready")) {
            out.write("USER miage\r\n".getBytes());
            out.flush();
            serverResponse = in.readLine();
            System.out.println("Réponse du serveur: " + serverResponse);
        }

        if (serverResponse.contains("User name received")) {
            out.write("PASS car\r\n".getBytes());
            out.flush();
            serverResponse = in.readLine();
            System.out.println("Réponse du serveur: " + serverResponse);
        }

        if (serverResponse.contains("User logged in")) {
            out.write("QUIT\r\n".getBytes());
            out.flush();
            System.out.println("Réponse du serveur: " + serverResponse);
        } else {
            System.out.println("Réponse du serveur: QUIT");

        }

        in.close();
        out.close();
        serverSocket.close();
    }

    public static void secondCase() throws UnknownHostException, IOException {
        Socket serverSocket = new Socket("localhost", 2121);
        System.out.println("Deuxième cas: Connecté au serveur");

        OutputStream out = serverSocket.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

        String serverResponse = in.readLine();
        System.out.println("Réponse du serveur: " + serverResponse);

        if (serverResponse.contains("service ready")) {
            out.write("PING\r\n".getBytes());
            out.flush();
            serverResponse = in.readLine();
            System.out.println("Réponse du serveur: " + serverResponse);
        }

        if (serverResponse.contains("PING command ok")) {
            serverResponse = in.readLine();
            System.out.println("Réponse du serveur: " + serverResponse);
            if (serverResponse.contains("PONG")) {
                out.write("200 PONG command ok\r\n".getBytes());
                out.flush();
            } else {
                out.write("502 Unknown command\r\n".getBytes());
                System.out.println("502 Unknown command");
            }

        }

        in.close();
        out.close();
        serverSocket.close();
    }

    public static void thirdCase() throws UnknownHostException, IOException {
        Socket serverSocket = new Socket("localhost", 2121);
        System.out.println("Deuxième cas: Connecté au serveur");

        OutputStream out = serverSocket.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
    }
}
