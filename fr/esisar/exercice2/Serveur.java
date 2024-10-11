package fr.esisar.exercice2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Serveur {

    private List<ThreadServeur> threads;

    public Serveur() {
        threads = new ArrayList<>();
    }
    public static void main(String[] args) throws Exception {
        Serveur serveur = new Serveur();
        serveur.execute();
    }

    private void execute() throws IOException {
        System.out.println("Demarrage du serveur ...");

        ServerSocket socketEcoute = new ServerSocket();
        socketEcoute.bind(new InetSocketAddress(7050));


        // Attente de la connexion d'un client
        int i = 0;
        while (true) {
            System.out.println("Attente de la connexion du client ...");
            Socket socketConnexion = socketEcoute.accept();
            threads.add(new ThreadServeur(socketConnexion));
            threads.get(i).start();
            i++;
        }

        // Arret du serveur 
        //socketEcoute.close();
        //System.out.println("Arret du serveur");
    }

}
