package fr.esisar.exercice2;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        Client c = new Client();
        c.execute();
    }

    private void execute() throws Exception {
        System.out.println("Démarrage du client ...");

        Socket socket = new Socket();

        InetSocketAddress adrDest = new InetSocketAddress("127.0.0.1", 7050);
        socket.connect(adrDest);
        
        // Après s'être connecté au serveur, il faut lui envoyer le nom du
        // fichier à copier
        //byte[] bufE = new String("/home/rivendell/NE441.TDM4/file_copy.txt").getBytes();
        // Ici on essaie de piéger le serveur
        byte[] bufE = new String("/Users/pim/Documents/ESISAR/Year2/NE441/NE441.TDM9/").getBytes();
        OutputStream os = socket.getOutputStream();
        os.write(bufE);
        Thread.sleep(1000);
        bufE = new String("file1").getBytes();
        os.write(bufE);
        System.out.println("Nom du fichier envoyé au serveur");

        long start = System.currentTimeMillis();

        // On se prépare à recevoir la taille du fichier de la part du serveur
        InputStream is = socket.getInputStream();
        byte[] bufR = new byte[2048];
        int len = is.read(bufR);
        boolean b = true;
        String message = "";
        Long fileSize = 0L;
        while (len != -1 && b) {
            message = message.concat(new String(bufR, 0, len));
            if (message.contains(";")) {
                b = false;
                int place = message.indexOf(';');
                fileSize = Long.parseLong(message.substring(0, place));
            } else {
                len = is.read(bufR);
            }
        }

        // Il faut ensuite répondre au serveur
        bufE = new String("OK").getBytes();
        os.write(bufE);
        System.out.println("Réponse au serveur");

        // On peut donc afficher une progression
        Long receivedSize = 0L;

        // On prépare l'écriture du fichier
        FileOutputStream fos = new FileOutputStream("/Users/pim/Documents/ESISAR/Year2/NE441/NE441.TDM9/file2");

        // On lit tous les messages venant du serveur
        len = is.read(bufR);
        //System.out.println("0%");
        System.out.println("========================================|");
        int count = 1;
        while (len != -1) {
            // Gestion de l'avancement
            receivedSize += len;
            float temp = receivedSize.floatValue() / fileSize.floatValue() * 100;
            count = percentage(temp, count);

            fos.write(bufR, 0, len);
            len = is.read(bufR);
        }

        System.out.println("|");

        long stop = System.currentTimeMillis();
        System.out.println("Elapsed Time = " + (stop - start) + "ms");

        fos.close();

        socket.close();

        System.out.println("Arrêt du client");
    }

    private int percentage(float value, int count) {
        if (value >= count * 10) {
            //System.out.println(count + "0%");
            System.out.print("====");
            return count + 1;
        }
        return count;
    }
}
