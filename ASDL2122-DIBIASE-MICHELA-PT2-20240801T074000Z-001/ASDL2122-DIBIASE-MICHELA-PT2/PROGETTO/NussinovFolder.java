/**
 *
 */
package it.unicam.cs.asdl2122.pt2;



//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE


/**
 * Implementazione dell'algoritmo di Nussinov-Jacobson per trovare, data una
 * sequenza di nucleotidi, una struttura secondaria senza pseudonodi che ha un
 * numero massimo di legami deboli.
 *
 * @author Luca Tesei
 *
 */
public class NussinovFolder implements FoldingAlgorithm {

    private final String primarySequence;
    private SecondaryStructure optimalSubstructure;
    private int[][] nussinovJacobson;



    /**
     * Costruisce un solver che utilizza l'algoritmo di Nussinov-Jacobson.
     *
     * @param primarySequence
     *                            la sequenza di nucleotidi di cui fare il
     *                            folding
     *
     * @throws IllegalArgumentException
     *                                      se la primarySequence contiene dei
     *                                      codici di nucleotidi sconosciuti
     * @throws NullPointerException
     *                                      se la sequenza di nucleotidi è nulla
     */
    public NussinovFolder(String primarySequence) {
        if (primarySequence == null)
            throw new NullPointerException(
                    "Tentativo di costruire un solutore Nussinov a partire da una sequenza nulla");
        String seq = primarySequence.toUpperCase().trim();
        // check bases in the primary structure - IUPAC nucleotide codes
        for (int i = 0; i < seq.length(); i++)
            switch (seq.charAt(i)) {
                case 'A':
                case 'U':
                case 'C':
                case 'G':
                    break;
                default:
                    throw new IllegalArgumentException(
                            "INPUT ERROR: primary structure contains an unkwnown nucleotide code at position "
                                    + (i + 1));
            }
        this.primarySequence = seq;

        this.nussinovJacobson = new int[seq.length()][seq.length()];

        this.optimalSubstructure = null;
    }

    public String getName() {
        return "NussinovFolder";
    }

    @Override
    public String getSequence() {
        return this.primarySequence;
    }

    @Override
    public SecondaryStructure getOneOptimalStructure() {
        if (!isFolded())
            throw new IllegalStateException();
        return optimalSubstructure;
    }

    @Override
    public void fold() {

        optimalSubstructure = new SecondaryStructure(this.primarySequence);
        //per 1 ≤ i ≤ n, Ni,i = 0 ed Ni,i−1 = 0;
        // per ogni i compresa tra 1 e n, inseriren nella posizione (i,i) e (i,i-1) lo zero
        for(int i = 0; i < primarySequence.length(); i++) {
            nussinovJacobson[i][i] = 0;
            if(i > 0)
                nussinovJacobson[i][i-1] = 0;
        }

        for(int n = 1; n < primarySequence.length(); n++) {// prende la stringa per iniziare a controllare i
            // neuclotidi (Si noti che il primo nucleotidi  ha posizione 1)
            for(int i = 0; i < primarySequence.length()-n; i++) {// inizio a controllare le colonne
                int j = i+n;//La matrice di Nussinov-Jacobson N e` una matrice di dimensione n × (n + 1)
                // assegna come massimo il i, j-1 della matrice
                int max = nussinovJacobson[i][j-1];
                // in questo for troviamo il valore di val
                for(int k = i; k < j; k++) {
                    int val = -1;//il valore di val quando non ci sono legami
                    //controlla se ci sono legami
                    if(areValid(primarySequence.charAt(k), primarySequence.charAt(j)))
                        if(k > 0)
                            val = nussinovJacobson[i][k-1] + nussinovJacobson[k+1][j-1] + 1;
                        else
                            val = nussinovJacobson[k+1][j-1] + 1;
                    if (val > max) {// se e piu grande val
                        max = val;// allora val diventa il nuovo massimo
                    }
                }
                nussinovJacobson[i][j] = max;// inserisco il nuovo massimo sulla matrice in posizione i e j
            }
        }

        int i = 0;
        int j = primarySequence.length()-1;
        traceback(i, j);
    }

    private void traceback(int i, int j) {
        if(j <= i)
            return;
        if(nussinovJacobson[i][j] == nussinovJacobson[i][j-1]) {// se in valore in posizione [i][j]
            // e uguale a quello [i][j-1]
            traceback(i, j-1);// faccio la ricorsione e diminuisco la j
            return;
        }
        for(int k = i; k < j; k++) {// a k gli viene assegnatoa  il valore della colonna i
            int val = 0;//assegnamo 0 a val
            if(k > 0) { // se k e maggiore di zero
                val = nussinovJacobson[i][k-1];// val prende il valore della posizione della matrice
                // che stavamo controllando
            }
            if(areValid(primarySequence.charAt(k), primarySequence.charAt(j)) &&
                    nussinovJacobson[i][j] == val + nussinovJacobson[k+1][j-1]+1) {
                optimalSubstructure.addBond(new WeakBond(k+1, j+1));
                traceback(i, k-1);
                traceback(k+1, j-1);
                return;
            }
        }
    }
    // sono i legami ammessi
    private boolean areValid(char charI, char charJ) {
        return isValid(charI, charJ, 'G', 'C') ||
                isValid(charI, charJ, 'A', 'U') ||
                isValid(charI, charJ, 'U', 'G');
    }

    private boolean isValid(char n1, char n2, char validN1, char validN2) {
        return n1 == validN2 && n2 == validN1 || n1 == validN1 && n2 == validN2;
    }

    @Override // isFolded restituisce: true se il folding sulla sequenza è stato eseguito, false altrimenti
    public boolean isFolded() {
        return optimalSubstructure != null;
    }

}