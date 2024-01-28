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
| PWD         |             | Retourne le chemin du répertoire de travail courant                                                 |
| CWD         | path        | Change le répertoire du travail                                                                     |
| PORT        | a,b,c,d,i,p | Indique au serveur l'adresse du socket de données (mode actif)                                      |
| PASV        |             | Demande au serveur de créer un socket de données (le port est envoyé dans la réponse) (mode passif) |
| EPSV        |             | Demande au serveur de créer un socket de données (le port est envoyé dans la réponse) (mode passif) |
| LIST        | [path]      | Liste le contenu d'un répertoire (données envoyées sur le socket de données)                        |
| SIZE        | path        | Retourne la taille du fichier                                                                       |
| RETR        | path        | Récupérer le contenu du fichier (données envoyées sur le socket de données)                         |
| MDTM        | path        | Récupère la date de dernière modification                                                           |

## Comment étendre le code

Les fonctionnalités se trouvent dans la classe Server plus précisément dans la fonction méthode, qui contient un switch case servant comme handler pour les commandes envoyé par le client. Et dans pour implementer de nouvelles fonctionnalité il suffit de rajouté la commande correspondante au switch case et appeler la méthode pour effectuer les processus.
