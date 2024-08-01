package it.unicam.cs.asdl2122.pt2;

/**
 * Interfaccia per algoritmi di folding di strutture secondarie di RNA.
 * 
 * @author Luca Tesei
 *
 */
public interface FoldingAlgorithm {

    /**
     * Restituisce il nome dell'algoritmo di folding.
     * 
     * @return il nome dell'algoritmo
     */
    public String getName();

    /**
     * Restituisce la sequenza da cui è stata calcolata la struttura secondaria.
     * 
     * @return la stringa di nucleotidi oggetto del folding
     */
    public String getSequence();

    /**
     * Esegue il folding sulla sequenza.
     */
    public void fold();

    /**
     * Determina se il folding è stato eseguito.
     * 
     * @return true se il folding sulla sequenza è stato eseguito, false
     *         altrimenti
     */
    public boolean isFolded();

    /**
     * Restituisce una struttura secondaria ottima secondo la definizione
     * dell'algoritmo.
     * 
     * @return una struttura secondaria ottima per la sequenza di nucleotidi
     *         oggetto di questo algoritmo
     * 
     * @throws IllegalStateException
     *                                   se il folding sulla sequenza non è
     *                                   ancora stato eseguito
     */
    public SecondaryStructure getOneOptimalStructure();

}