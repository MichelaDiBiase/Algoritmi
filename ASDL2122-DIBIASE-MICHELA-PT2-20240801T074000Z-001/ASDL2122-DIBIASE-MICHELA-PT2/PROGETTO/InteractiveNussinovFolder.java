package it.unicam.cs.asdl2122.pt2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Classe di test per provare interattivamente l'algoritmo di Nussinov.
 * 
 * @author Luca Tesei
 *
 */
public class InteractiveNussinovFolder {

    public static void main(String[] args) {
        System.out.println("Inserire una sequenza di nucleotidi");
        BufferedReader input = new BufferedReader(
                new InputStreamReader(System.in));
        String sequence = null;
        try {
            sequence = input.readLine();
        } catch (IOException e) {
            System.out.println("Errore di lettura!");
            System.exit(1);
        }
        if (sequence == null) {
            System.out.println("Errore di lettura!");
            System.exit(1);
        }
        NussinovFolder a = null;
        try {
            a = new NussinovFolder(sequence);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        a.fold();
        SecondaryStructure sol = a.getOneOptimalStructure();
        System.out.println("Soluzione Ottima: " + sol.getCardinality());
        System.out.println("Legami della struttura ottima: ");
        System.out.println(sol.toString());
        System.out.println("Notazione dot-bracket della struttura ottima: ");
        System.out.println(sol.getDotBracketNotation());
    }

}
