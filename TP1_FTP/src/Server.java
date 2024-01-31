import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
    String cwd = "/TP1_FTP/src/filesToSend/";
    String startingWd = "/TP1_FTP/src/filesToSend/";

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

    private void sendMessage(OutputStream out, String msg) throws IOException {
        out.write((msg + "\r\n").getBytes(StandardCharsets.UTF_8));
        out.flush();
    }

    private void methods(String input) throws Exception {
        /*
         * Handles the commandes sent to the server, maps each commande to its function
         */
        // TODO: implement isLoggedIn in all functions, allow anonymous ?
        System.out.println(input);
        String command = input.split(" ")[0];
        String argument = input.split(" ").length > 1 ? input.split(" ")[1] : "";
        switch (command) {
            case "PWD":
                sendMessage(clientOut, "226 current path ." + printCDHistory());
                break;
            case "USER":
                username = argument;
                sendMessage(clientOut, "331 User name received");
                break;
            case "PASS":
                pwd = argument;
                if (Utilisateur.isValidUser(new Utilisateur(username, pwd))) {
                    this.sendMessage(clientOut, "230 User logged in.");
                } else {
                    this.sendMessage(clientOut, "430 Invalid username & password");
                }
                break;
            case "SIZE":
                final File f = new File(Path.of("./filesToSend").toString());
                sendMessage(clientOut, ("226 " + f.length()));
                break;
            // Code à exécuter pour 'PORT' ou 'EPRT'
            case "PORT":
            case "EPRT":
                if (setDataPort(command, argument))
                    return;
                break;
            case "CWD":
                this.cd(argument);
                break;
            case "RETR":
                String fileName = argument;
                sendFile(fileName);
                break;
            case "LIST":
                if (input.split(" ").length == 2) {
                    dir(argument);
                } else {
                    dir();
                }
                break;
            case "QUIT":
                System.out.println(input);
                client.close();
                server.close();
                System.exit(0);
            default:
                sendMessage(clientOut, "500 Command not implemented.");
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
                    this.sendMessage(clientOut, "226 Changed working directory, current path ." + printCDHistory());
                } else {
                    this.sendMessage(clientOut, "510 already in root directory, current path ." + printCDHistory());
                }
                break;
            case "/":
                dirHistory.clear();
                cwd = startingWd;
                this.sendMessage(clientOut, "226 Changed working directory, current path ." + printCDHistory());
                // go to root
                break;
            default:
                final File folder = new File(getCurrentPath() + cwd + string);
                // test if path is a directory
                if (folder.exists() && folder.isDirectory()) {
                    cwd += string + "/";
                    dirHistory.addAll(Arrays.asList(string.split("/")));
                    this.sendMessage(clientOut, "226 Changed working directory, current path ." + printCDHistory());
                } else {
                    this.sendMessage(clientOut, "510 " + string + " is not a directory or it doesn't exist");
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

    private boolean setDataPort(String command, String addressString) throws Exception {
        /*
         * Gère les overture du port de donnée, peu importe le mode
         */
        // Si la commande est PORT
        if (command.equals("PORT")) {
            // On divise la chaîne d'adresse en tableau
            final String[] addressArray = addressString.split(",");
            // Si la longueur du tableau n'est pas 6, on renvoie une erreur
            if (addressArray.length != 6) {
                sendMessage(clientOut, "425 Impossible d'ouvrir la connexion de données.");
                return true;
            }
            // On construit l'adresse et le port à partir de la chaîne de caractère
            final String address = addressArray[0] + "." + addressArray[1]
                    + "." + addressArray[2] + "." + addressArray[3];
            final int dataPort = Integer.parseInt(addressArray[4]) * 256
                    + Integer.parseInt(addressArray[5]);
            try {
                // On tente d'ouvrir un socket vers l'adresse et le port spécifiés
                dataTransferSocket = new Socket(address, dataPort);
                sendMessage(clientOut, "227 Entrée en mode actif");
            } catch (final IOException e) {
                // En cas d'erreur, on lance une exception
                throw new Exception(
                        "Impossible de créer le mode actif du serveur de transfert", e);
            }
            // Si la commande est EPRT
        } else if (command.equals("EPRT")) {
            // On divise la chaîne d'adresse en tableau
            String[] eprtArgs = addressString.split("\\|");
            // Si la longueur du tableau n'est pas 4, on renvoie une erreur
            if (eprtArgs.length != 4) {
                sendMessage(clientOut, "500 Erreur de syntaxe, commande non reconnue.");
                return true;
            }
            // On extrait le protocole, l'adresse IP et le port du tableau
            String protocol = eprtArgs[1];
            String ipAddress = eprtArgs[2];
            int dataPort = Integer.parseInt(eprtArgs[3]);
            // Si le protocole est 2 (IPv6) et l'adresse IP est ::1 (localhost)
            if (protocol.equals("2") && ipAddress.equals("::1")) {
                try {
                    // On tente d'ouvrir un socket vers l'adresse et le port spécifiés
                    dataTransferSocket = new Socket(ipAddress, dataPort);
                    sendMessage(clientOut, "200 Commande OK.");
                } catch (final IOException e) {
                    // En cas d'erreur, on lance une exception
                    throw new Exception("Impossible de créer le mode actif du serveur de transfert", e);
                }
            } else {
                // Si le protocole n'est pas supporté, on renvoie une erreur
                sendMessage(clientOut, "522 Protocole non supporté.");
            }
        }
        // Si tout se passe bien, on renvoie false
        return false;
    }

    private void dir(String... folderName) throws Exception {
        if (dataTransferSocket == null) {
            this.sendMessage(clientOut, "443 No data connection");
        } else {
            this.sendMessage(clientOut, "150 Accepted data connection");
            final OutputStream dataOutputStream = dataTransferSocket.getOutputStream();
            // checks if there is foldername specified
            if (folderName.length == 0) {
                final File currentFolder = new File(getCurrentPath() + cwd);
                if (currentFolder.exists()) {
                    if (currentFolder.isDirectory()) {
                        if (currentFolder.listFiles().length > 0) {
                            sendMessage(dataOutputStream, fileContent(currentFolder));
                        } else {
                            this.sendMessage(clientOut, "510 No files in " + cwd);
                        }
                    } else {
                        this.sendMessage(clientOut, "510 Not a dir " + currentFolder.getPath());
                        System.out.println(currentFolder.isDirectory());
                    }
                } else {
                    System.out.println(currentFolder.exists());
                    this.sendMessage(clientOut, "510 Folder doesn't exist" + currentFolder.getPath());
                }
            } else {
                final File folder = new File(getCurrentPath() + cwd + folderName[0]);
                if (folder.exists()) {
                    if (folder.isDirectory()) {
                        if (folder.listFiles().length > 0) {
                            sendMessage(dataOutputStream, "files at /filesToSend/: \n");
                            for (File file : folder.listFiles()) {
                                sendMessage(dataOutputStream, ("\t-" + file.getName() + "\n"));
                            }
                            // clear remaining data if there is any
                            dataOutputStream.flush();
                            dataOutputStream.close();
                        } else {
                            this.sendMessage(clientOut, "510 No files in the specified directory");
                        }
                    } else {
                        this.sendMessage(clientOut, "510 " + folderName[0] + "is not a dir");
                    }
                } else {
                    this.sendMessage(clientOut, "510" + folderName[0] + " doesn't exist");
                }
            }
            // close data socket
            dataTransferSocket.close();
            // success message
            this.sendMessage(clientOut, "226 Current directory content sent");
        }
    }

    // Create a function named fileContent that takes a File as an argument and
    // returns a String
    private String fileContent(File currentFolder) throws IOException {
        // Initialize an empty StringBuilder to store the result
        StringBuilder sb = new StringBuilder();
        // Append the current folder name to the result
        sb.append("Directory: " + currentFolder.getAbsolutePath() + "\n");
        // Append the column headers to the result
        sb.append("Mode\t\tLastWriteTime\t\t\tLength\tType\t\t\tName\n");
        // Append the column separators to the result
        sb.append("----\t\t-------------\t\t\t------\t----\t\t\t----\n");
        // Loop through each file in the current folder
        for (File file : currentFolder.listFiles()) {
            // Get the file mode (read/write/execute) as a String
            String mode = getFileMode(file);
            // Get the file last write time as a String
            String lastWriteTime = getLastWriteTime(file);
            // Get the file length as a String
            String length = String.valueOf(file.length());
            // Get the file type as a String
            String type = getFileType(file);
            // Get the file name as a String
            String name = file.getName();
            // Append the file details to the result
            sb.append(mode + "\t\t" + lastWriteTime + "\t\t" + length + "\t" + type + "\t" + name + "\n");
        }
        return sb.toString();
    }

    // Create a helper method that returns the file mode as a String
    private String getFileMode(File file) {
        // Initialize an empty String
        String mode = "";
        // Check if the file is readable
        if (file.canRead()) {
            // Append a r to the mode
            mode += "r";
        } else {
            // Append a - to the mode
            mode += "-";
        }
        // Check if the file is writable
        if (file.canWrite()) {
            // Append a w to the mode
            mode += "w";
        } else {
            // Append a - to the mode
            mode += "-";
        }
        // Check if the file is executable
        if (file.canExecute()) {
            // Append a x to the mode
            mode += "x";
        } else {
            // Append a - to the mode
            mode += "-";
        }
        // Return the mode
        return mode;
    }

    // Create a helper method that returns the file last write time as a String
    private String getLastWriteTime(File file) {
        // Get the file last modified time in milliseconds
        long lastModified = file.lastModified();
        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        // Create a Date object from the last modified time
        Date date = new Date(lastModified);
        // Format the date as a String
        String lastWriteTime = sdf.format(date);
        // Return the last write time
        return lastWriteTime;
    }

    // Create a helper method that returns the file type as a String
    private String getFileType(File file) throws IOException {
        // Get the file path as a Path object
        Path path = file.toPath();
        // Use the Files.probeContentType method to get the MIME type
        String mimeType = Files.probeContentType(path);
        // If the MIME type is null, return an empty String
        if (mimeType == null) {
            if (file.isDirectory()) {
                return "Directory/Folder";
            }
            return "";
        }
        // Otherwise, return the MIME type
        return mimeType + "\t";
    }

    private void sendFile(String fileName) throws Exception {
        if (dataTransferSocket == null) {
            this.sendMessage(clientOut, "443 No data connection");
        } else {
            this.sendMessage(clientOut, "150 Accepted data connection");
            // récup la current path
            final Path currentDirectorPath = FileSystems.getDefault().getPath("");
            String currentDirectoryName = currentDirectorPath.toAbsolutePath().toString();
            try (final FileInputStream fileInputStream = new FileInputStream(
                    currentDirectoryName + cwd + fileName);) {

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
                this.sendMessage(clientOut, "226 File successfully transferred.");
            } catch (final IOException e) {
                this.sendMessage(clientOut, "426 Unable to send file.");
                throw new Exception(e.getMessage());
            }
        }
    }
}