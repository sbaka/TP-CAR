import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

class Server {
    int port = 2121;
    boolean isLoggedIn = false;
    ServerSocket server;
    Socket dataTransferSocket = null;
    Socket client;
    OutputStream clientOut;
    InputStream clientInput;
    String username = "";
    String pwd = "";
    String input;
    Stack<String> dirHistory = new Stack<String>();
    String cwd = "/TP1-Intellij/src/filesToSend/";
    String startingWd = "/TP1-Intellij/src/filesToSend/";

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start_server();
    }

    void start_server() throws IOException {
        server = new ServerSocket(port);
        System.out.println("Démarrage du serveur sur 127.0.0.1:" +
                port +
                ".\nAttente d’une connexion...");
        run();
    }

    public void run() {
        try {
            client = server.accept();
            clientOut = client.getOutputStream();
            clientInput = client.getInputStream();
            Scanner scanner = new Scanner(clientInput);

            // implementing multithreading support soon
            // ExecutorService executor = Executors.newFixedThreadPool(5);

            // send to client that it is ready
            clientOut.write("220 service ready\n".getBytes());
            while (true) {
                input = scanner.nextLine();
                methods(input);
            }
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }

    }

    private void sendMessage(String msg) throws IOException {
        clientOut.write((msg + "\r\n").getBytes(StandardCharsets.UTF_8));
        clientOut.flush();
    }

    private void methods(String input) throws Exception {
        // TODO: implement isLoggedIn in all functions, allow anonymous ?
        // TODO: implement dir
        System.out.println(input);
        switch (input.split(" ")[0]) {
            case "USER":
                username = input.split(" ")[1];
                sendMessage("331 User name received");
                break;
            case "PASS":
                pwd = input.split(" ")[1];
                if (Utilisateur.isValidUser(new Utilisateur(username, pwd))) {
                    this.sendMessage("230 User logged in.");
                } else {
                    this.sendMessage("430 Invalid username & password");
                }
                break;
            case "SIZE":
                final File f = new File(Path.of("./filesToSend").toString());
                sendMessage(("226 " + f.length()));
                break;
            case "PORT":
                String addressString = input.split(" ")[1];
                if (setDataPort(addressString))
                    return;
                break;
            case "CWD":
                this.cd(input.split(" ")[1]);
                break;
            case "RETR":
                String fileName = input.split(" ")[1];
                sendFile(fileName);
                break;
            case "LIST":
                if (input.split(" ").length == 2) {
                    listFileContent(input.split(" ")[1]);
                } else {
                    listFileContent();
                }
                break;
            case "QUIT":
                System.out.println(input);
                client.close();
                server.close();
                System.exit(0);
            default:
                sendMessage("500 Command not implemented.");
                System.out.println("[" + input + "]: not implemented");
                break;
        }
    }

    private String getCurrentPath() {
        /* get the absolute path of the current working directory */
        final Path currentDirectorPath = FileSystems.getDefault().getPath("");
        String currentDirectoryName = currentDirectorPath.toAbsolutePath().toString();
        return currentDirectoryName;
    }

    private void cd(String string) throws Exception {
        /*
         * handles the cd commande:
         * -cd <filename>: navigates to <filename if it's a directory
         * -cd ..: goes back one step
         * -cd /: goes straight to root
         */
        switch (string) {
            case "..":
                // go back one step
                if (!dirHistory.empty()) {
                    dirHistory.pop();
                    cwd = startingWd + printCDHistory();
                    this.sendMessage("226 Changed working directory, current path ." + printCDHistory());
                } else {
                    this.sendMessage("510 already in root directory, current path ." + printCDHistory());
                }
                break;
            case "/":
                dirHistory.clear();
                cwd = startingWd;
                this.sendMessage("226 Changed working directory, current path ." + printCDHistory());
                // go to root
                break;
            default:
                final File folder = new File(getCurrentPath() + cwd + string);
                // test if path is a directory
                if (folder.exists() && folder.isDirectory()) {
                    cwd += string + "/";
                    dirHistory.addAll(Arrays.asList(string.split("/")));
                    this.sendMessage("226 Changed working directory, current path ." + printCDHistory());
                } else {
                    this.sendMessage("510 " + string + " is not a directory or it doesn't exist");
                }
                break;
        }
        // printing current navigation stack
        System.out.println("navigation stack: " + dirHistory);

    }

    private String printCDHistory() {
        String temp = "/";
        if (!dirHistory.isEmpty()) {
            for (String elString : dirHistory) {
                temp += elString + "/";
            }
        }
        return temp;
    }

    private boolean setDataPort(String addressString) throws Exception {
        final String[] addressArray = addressString.split(",");
        if (addressArray.length != 6) {
            sendMessage("425 Can't open data connection.");
            return true;
        }
        final String address = addressArray[0] + "." + addressArray[1]
                + "." + addressArray[2] + "." + addressArray[3];
        final int dataPort = Integer.parseInt(addressArray[4]) * 256
                + Integer.parseInt(addressArray[5]);
        try {
            dataTransferSocket = new Socket(address, dataPort);
            sendMessage("227 Entering Active Mode");
        } catch (final IOException e) {
            throw new Exception(
                    "Unable to create TransferServer active mode", e);
        }
        return false;
    }

    // void listFileContent() throws Exception {
    // if (dataTransferSocket == null) {
    // this.sendMessage("443 No data connection");
    // } else {
    // this.sendMessage("150 Accepted data connection");
    // // récup la current path
    // final File filesToSendFolder = new File(
    // getCurrentPath() + cwd);
    // final OutputStream dataOutputStream = dataTransferSocket.getOutputStream();
    // if (filesToSendFolder.listFiles().length > 0) {
    // dataOutputStream.write("files at /filesToSend/: \n".getBytes());
    // for (File file : filesToSendFolder.listFiles()) {
    // dataOutputStream.write(("\t-" + file.getName() + "\n").getBytes());
    // }
    // } else {
    // this.sendMessage("510 No files in " + cwd);
    // }
    // // clear remaining data if there is any
    // dataOutputStream.flush();
    // // close output
    // dataOutputStream.close();
    // // close data socket
    // dataTransferSocket.close();
    // // success message
    // this.sendMessage("226 Current directory content sent");
    // }
    // }

    void listFileContent(String... folderName) throws Exception {
        if (dataTransferSocket == null) {
            this.sendMessage("443 No data connection");
        } else {
            this.sendMessage("150 Accepted data connection");
            final OutputStream dataOutputStream = dataTransferSocket.getOutputStream();
            // checks if there is foldername specified
            if (folderName.length == 0) {
                final File currentFolder = new File(getCurrentPath() + cwd);
                if (currentFolder.exists() && currentFolder.isDirectory()) {
                    if (currentFolder.listFiles().length > 0) {
                        dataOutputStream.write(("files at " + printCDHistory() + " \n").getBytes());
                        for (File file : currentFolder.listFiles()) {
                            dataOutputStream.write(("\t-" + file.getName() + "\n").getBytes());
                        }
                    } else {
                        this.sendMessage("510 No files in " + cwd);
                    }
                } else {
                    this.sendMessage("510 Not a dir " + getCurrentPath() + cwd);
                }
            } else {
                final File folder = new File(getCurrentPath() + cwd + folderName[0]);
                if (folder.exists()) {
                    if (folder.isDirectory()) {
                        if (folder.listFiles().length > 0) {
                            dataOutputStream.write("files at /filesToSend/: \n".getBytes());
                            for (File file : folder.listFiles()) {
                                dataOutputStream.write(("\t-" + file.getName() + "\n").getBytes());
                            }
                            // clear remaining data if there is any
                            dataOutputStream.flush();
                            dataOutputStream.close();
                        } else {
                            this.sendMessage("510 No files in the specified directory");
                        }
                    } else {
                        this.sendMessage("510 " + folderName[0] + "is not a dir");
                    }
                } else {
                    this.sendMessage("510" + folderName[0] + " doesn't exist");
                }
            }
            // close data socket
            dataTransferSocket.close();
            // success message
            this.sendMessage("226 Current directory content sent");
        }
    }

    void sendFile(String fileName) throws Exception {
        if (dataTransferSocket == null) {
            this.sendMessage("443 No data connection");
        } else {
            this.sendMessage("150 Accepted data connection");
            // récup la current path
            final Path currentDirectorPath = FileSystems.getDefault().getPath("");
            String currentDirectoryName = currentDirectorPath.toAbsolutePath().toString();
            try (final FileInputStream fileInputStream = new FileInputStream(
                    currentDirectoryName + "/TP1-Intellij/src/filesToSend/" + fileName);) {

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
                this.sendMessage("226 File successfully transferred.");
            } catch (final IOException e) {
                this.sendMessage("426 Unable to send file.");
                throw new Exception(e.getMessage());
            }
        }
    }
}