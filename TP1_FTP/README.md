# TP 1 – Serveur FTP

Le 28/01/2024

## But du tp

Réaliser un serveur FTP en JAVA, le serveur doit pouvoir communiquer avec un client FTP (Celui de linux ou windows).

## Fonctionnalités implémentées

| FTP Command | Paramètre   | Description                                                                                         |
| ----------- | ----------- | --------------------------------------------------------------------------------------------------- |
| USER        | username    | Défini le nom d'utilisateur du client                                                               |
| PASS        | password    | Défini le mot de passe du client                                                                    |
| QUIT        |             | Ferme la connexion                                                                                  |
| SYST        |             | Indique le type de système utilisé par le serveur (UNIX Type: L8)                                   |
| CWD         | path        | Change le répertoire du travail                                                                     |
| PORT        | a,b,c,d,i,p | Indique au serveur l'adresse du socket de données (mode actif)                                      |
| EPSV        |             | Demande au serveur de créer un socket de données (le port est envoyé dans la réponse) (mode passif) |
| PASV(todo)  |             | Demande au serveur de créer un socket de données (le port est envoyé dans la réponse) (mode passif) |
| LIST        | [path]      | Liste le contenu d'un répertoire (données envoyées sur le socket de données)                        |
| SIZE        | path        | Retourne la taille du fichier                                                                       |
| RETR        | path        | Récupérer le contenu du fichier (données envoyées sur le socket de données)                         |

## Comment étendre le code

Les fonctionnalités se trouvent dans la classe Server plus précisément dans la fonction `methods`, qui contient un switch case servant comme handler pour les commandes envoyé par le client. Et donc pour implementer de nouvelles fonctionnalités il suffit de rajouter la commande correspondante au switch case et implementer la méthode pour effectuer les processus.

### Runnable

Pour que le serveur FTP supporte le multithreading, je peux créer un nouveau thread pour chaque client qui se connecte. Chaque thread gérera la communication avec un client spécifique. Voici comment je le ferai :

- Créez une nouvelle classe qui implémente l’interface Runnable. Cette classe représentera le thread qui gérera la communication avec un client spécifique.

```java
class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        // le code pour gérer la communication
    }
}
```

Et dans la méthode `run` de la classe Server on met un code similaire.

```java
public void run() {
    try {
        while (true) {
            Socket clientSocket = server.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            new Thread(clientHandler).start();
        }
    } catch (IOException e) {
        System.out.println(e.getMessage());
    }
}
```
