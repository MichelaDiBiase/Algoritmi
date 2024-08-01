package it.unicam.cs.asdl2122.pt2;

/**
 * Classe che rappresenta un legame debole in una struttura secondaria di RNA.
 * Viene rappresentato con la coppia {@code (i,j), i < j}, indicante il fatto
 * che il nucleotide in posizione i è legato al nucleotide in posizione j.
 * 
 * La numerazione delle posizioni dei nucleotidi in una sequenza inizia da 1.
 * 
 * @author Luca Tesei
 *
 */
public class WeakBond {
    private final int i;

    private final int j;

    /**
     * @param i
     *              indice del nucleotide più a sinistra nella sequenza di
     *              questo legame
     * @param j
     *              indice del nucleotide più a destra nella sequenza di questo
     *              legame
     * 
     * @throws IllegalArgumentException
     *                                      se gli indici non sono validi, cioè
     *                                      se {@code j <= i} oppure
     *                                      {@code i <= 0} oppure {@code j <= 0}
     */
    public WeakBond(int i, int j) {
        if (j <= i || i <= 0 || j <= 0)
            throw new IllegalArgumentException("Indici del legame non validi");
        this.i = i;
        this.j = j;
    }

    /**
     * Restituisce il primo indice del legame.
     * 
     * @return il primo indice del legame
     */
    public int getI() {
        return i;
    }

    /**
     * Restituisce il secondo indice del legame.
     * 
     * @return il secondo indice del legame
     */
    public int getJ() {
        return j;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + i;
        result = prime * result + j;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof WeakBond))
            return false;
        WeakBond other = (WeakBond) obj;
        if (i != other.i)
            return false;
        if (j != other.j)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "(" + i + ", " + j + ")";
    }

}
