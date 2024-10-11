package fr.esisar.exercice2;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ThreadServeur extends Thread {
    private Socket socketConnexion;

    public ThreadServeur(Socket socketConnexion) {
        this.socketConnexion = socketConnexion;
    }

    public void run() {
        try {

            // Affichage du port et de l'ip du client 
            System.out.println("Un client est connecté");
            System.out.println("IP:"+socketConnexion.getInetAddress());
            System.out.println("Port:"+socketConnexion.getPort());

            // Un client s'est connecté, il faut lui envoyer le contenu du fichier
            // qu'il demande
            byte[] bufR = new byte[2048];
            InputStream is = socketConnexion.getInputStream();
            int lenBufR = is.read(bufR);
            String path = "";
            boolean b = true;
            FileInputStream fis = new FileInputStream("/Users/pim/.vimrc");
            while (lenBufR != -1 && b) {
                // Il faut que l'on soit certain d'avoir bien lu un chemin correct
                // Donc on teste le chemin jusqu'à ce que ça fonctionne
                path = path.concat(new String(bufR, 0, lenBufR));
                System.out.println("Nom du fichier reçu = " + path);
                b = false; // On part du principe qu'on a bien lu tout le nom du
                           // fichier

                try {
                    fis = new FileInputStream(path);
                } catch (Exception e) {
                    // Il ne se passe rien, il faut juste continuer de lire
                    System.out.println("Erreur dans la lecture du fichier");
                    b = true;
                    lenBufR = is.read(bufR);
                }
            }

            // On récupère la taille du fichier pour l'envoyer au client
            File f = new File(path);
            long fileSize = f.length();

            // On prépare les messages pour le client
            OutputStream os = socketConnexion.getOutputStream();

            // On envoye la taille au client
            byte[] bufE = (String.valueOf(fileSize) + ";").getBytes();
            System.out.println("Message envoyé: " + String.valueOf(fileSize) + ";");
            os.write(bufE);
            System.out.println("Taille du fichier envoyé");

            // On attend ensuite une réponse du client avant d'envoyer le contenu
            // du fichier
            lenBufR = is.read(bufR);
            b = true;
            String message;
            while (lenBufR != -1 && b) {
                message = new String(bufR, 0, lenBufR);
                b = !message.equals("OK");
            }

            // Un client est connecté, il faut lui envoyer le contenu du fichier
            byte[] buf = new byte[1000];

            int len = fis.read(buf);
            while (len != -1) {
                os.write(buf, 0, len);
                len = fis.read(buf);
            }
            fis.close();

            socketConnexion.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de l'exécution d'un thread");
        }

    }
}
