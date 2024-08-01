package it.unicam.cs.asdl2122.pt2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Applica diversi algoritmi di folding alle stesse sequenze di nucleotidi di
 * lunghezza crescente. Per ogni lunghezza genera un certo numero dato di
 * sequenze casuali. I dati relativi al numero di confronti, il tempo di
 * esecuzione in nanosecondi di ogni algoritmo su ogni sequenza sono scritti su
 * un file .csv (Comma Separated Values). In un altro file .csv sono riportate
 * le sequenze generate.
 * 
 * @author Luca Tesei
 *
 */
public class FoldingAlgorithmEvaluationFramework {

    public static void main(String[] args) {
        String dirName = null;
        if (args.length > 0)
            dirName = args[0];
        else
            dirName = ".";
        // Variabili per il conteggio del tempo di esecuzione
        long startTimeNano = 0;
        long elapsedTimeNano = 0;
        // Creo i file di output
        PrintStream o = null;
        PrintStream sequences = null;
        try {
            o = new PrintStream(new File(dirName
                    + "/" + "evalfram.csv"));
            sequences = new PrintStream(
                    new File(dirName + "/"
                            + "sequences.csv"));
        } catch (FileNotFoundException e) {
            System.out.println("Errore creazione file di ouput");
            System.exit(0);
        }
        // Creo una lista di algoritmi di folding
        List<FoldingAlgorithm> algs = new ArrayList<FoldingAlgorithm>();
        // Inserisco gli algoritmi che voglio testare
        algs.add(new NussinovFolder(""));
        // Creo una lista di stringhe (inizialmente vuote) per contenere le
        // copie delle sequenze
        // di cui fare il folding, una per ogni algoritmo
        List<String> lists = new ArrayList<String>();
        for (int k = 0; k < algs.size(); k++)
            lists.add(new String());
        // Inserisco la linea di intestazione dei dati nei file csv
        o.print("SeqId,");
        for (FoldingAlgorithm a : algs) {
            o.print(a.getName() + "Folding Tns,");
        }
        o.print("\n"); // Fine riga
        sequences.print("SeqId,");
        sequences.print("\n");

        // Generazione delle sequenze e dei dati
        // Creo un generatore di numeri casuali da inserire nella sequenza
        Random randomGenerator = new Random();
        // Indice per le lunghezze
        int n;
        // Contatore sequenze della stessa lunghezza per codice sequenza
        int count = 0;
        // Variabili di appoggio per la costruzione delle sequenze casuali
        int nucleotideCode = -1;
        char nucleotide = ' ';
        StringBuffer sb = null;
        // Ciclo esterno
        for (n = FoldingAlgorithmEvaluationFrameworkParameters.MIN_LENGTH; n <= FoldingAlgorithmEvaluationFrameworkParameters.MAX_LENGTH; n += FoldingAlgorithmEvaluationFrameworkParameters.INCREMENTO_LUNGHEZZA) {
            // Ciclo interno
            for (int i = 0; i < FoldingAlgorithmEvaluationFrameworkParameters.NUMBER_OF_SAMPLES_PER_LENGTH; i++) {
                // Scrivo in output il nome della sequenza
                o.print("seq" + "_" + n + "_" + count + ",");
                sequences.print("seq" + "_" + n + "_" + count + ",");
                // Genero la sequenza casuale di nucleotidi lunga n
                sb = new StringBuffer();
                for (int j = 0; j < n; j++) {
                    nucleotideCode = randomGenerator.nextInt(4);
                    switch (nucleotideCode) {
                    case 0:
                        nucleotide = 'A';
                        break;
                    case 1:
                        nucleotide = 'G';
                        break;
                    case 2:
                        nucleotide = 'U';
                        break;
                    case 3:
                        nucleotide = 'C';
                        break;
                    }
                    sb.append(nucleotide);
                }
                // Aggiungo la sequenza a tutte le liste
                for (int k = 0; k < lists.size(); k++)
                    lists.set(k, sb.toString());
                // Salvo l'elemento sul file delle sequenze
                sequences.print(sb.toString() + ",");
                // Sequenza generata
                sequences.print("\n"); // Fine riga
                System.out.println(
                        "Generata sequenza " + "seq" + "_" + n + "_" + count);
                // debug System.out.println(lists.get(0).toString());
                // debug System.out.println(lists.get(1).toString());
                // debug System.out.println(lists.get(2).toString());
                // Incremento il puntatore della sequenza
                count++;
                // Indice associato ad ogni algoritmo per fare get sulla list
                // associata
                int idx = 0;
                // Chiamo tutti gli algoritmi di ordinamento sulla sequenza e
                // scrivo i risultati in output
                for (FoldingAlgorithm a : algs) {
                    // Creo un nuovo oggetto folding algorithm del tipo di a e
                    // gli passo la sequenza randomizzata di nucleotidi corrente
                    Class<?> clazz = null;
                    try {
                        clazz = Class.forName(a.getClass().getName());
                    } catch (ClassNotFoundException e) {
                        System.out.println("Classe " + a.getClass().getName()
                                + "non trovata!");
                        System.exit(1);
                    }
                    Constructor<?> ctor = null;
                    try {
                        ctor = clazz.getConstructor(String.class);
                    } catch (NoSuchMethodException | SecurityException e) {
                        System.out.println("Costruttore di "
                                + a.getClass().getName()
                                + " con parametro stringa non trovato!");
                        System.exit(1);
                    }
                    Object object = null;
                    try {
                        object = ctor.newInstance(lists.get(idx));
                    } catch (InstantiationException | IllegalAccessException
                            | IllegalArgumentException
                            | InvocationTargetException e) {
                        System.out.println("Errore di creazione oggetto di "
                                + a.getClass().getName() + "!");
                        e.printStackTrace();
                        System.exit(1);
                    }
                    FoldingAlgorithm currentFoldingAlgorithmInstance = (FoldingAlgorithm) object;
                    // debug System.out.println(a.getName());
                    // debug System.out.println(lists.get(idx).toString());
                    // Guardo il tempo corrente in millisecondi e nanosecondi
                    startTimeNano = System.nanoTime();
                    // Chiamo l'algoritmo di folding
                    currentFoldingAlgorithmInstance.fold();
                    // Registro il tempo impiegato dall'algoritmo
                    elapsedTimeNano = System.nanoTime() - startTimeNano;
                    // debug System.out.println(result.getL().toString());
                    // Scrivo sul file di output
                    o.print(elapsedTimeNano + ",");
                    idx++;
                }
                o.print("\n"); // Fine riga
            } // End for interno
            count = 0; // riazzero il contatore
        } // End for esterno
        o.close();
        sequences.close();
    } // end main

}
