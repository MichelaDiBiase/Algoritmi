package it.unicam.cs.asdl2122.pt2;

import java.util.*;

/**
 * Un oggetto di questa classe rappresenta una struttura secondaria di RNA.
 *
 * @author Luca Tesei
 *
 */
public class SecondaryStructure {

    private final String primarySequence;

    private Set<WeakBond> bonds;

    /**
     * Costruisce una struttura secondaria con un insieme vuoto di legami
     * deboli.
     *
     * @param primarySequence
     *                            la sequenza di nucleotidi
     *
     * @throws IllegalArgumentException
     *                                      se la primarySequence contiene dei
     *                                      codici di nucleotidi sconosciuti
     * @throws NullPointerException
     *                                      se la sequenza di nucleotidi è nulla
     */
    public SecondaryStructure(String primarySequence) {
        if (primarySequence == null)
            throw new NullPointerException(
                    "Tentativo di costruire un solutore Nussinov a partire da una sequenza nulla");
        String seq = primarySequence.toUpperCase().trim();
        // check bases in the primary structure
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
        this.bonds = new HashSet<WeakBond>();
    }

    /**
     * Costruisce una struttura secondaria con un insieme dato di legami deboli.
     *
     * @param primarySequence
     *                            la sequenza di nucleotidi
     * @param bonds
     *                            l'insieme dei legami deboli presenti nella
     *                            struttura
     *
     * @throws IllegalArgumentException
     *                                       se la primarySequence contiene dei
     *                                       codici di nucleotidi sconosciuti
     * @throws NullPointerException
     *                                       se la sequenza di nucleotidi è
     *                                       nulla
     * @throws NullPointerException
     *                                       se l'insieme dei legami è nullo
     * @throws IndexOutOfBoundsException
     *                                       se almeno uno dei due indici di uno
     *                                       dei legami deboli passati esce
     *                                       fuori dai limiti della sequenza
     *                                       primaria di questa struttura
     * @throws IllegalArgumentException
     *                                       se almeno uno dei legami deboli
     *                                       passati connette due nucleotidi a
     *                                       formare una coppia non consentita.
     *
     */
    public SecondaryStructure(String primarySequence, Set<WeakBond> bonds) {
        if (primarySequence == null)
            throw new NullPointerException(
                    "Tentativo di costruire un solutore Nussinov a partire da una sequenza nulla");
        String seq = primarySequence.toUpperCase().trim();
        // check bases in the primary structure
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
        this.bonds = new HashSet<WeakBond>();
        /*
            for(int i = 0; i < bonds.size(); i++) {
                WeakBond b = bonds[i];
            }
         */
        for (WeakBond b : bonds)
            this.addBond(b);
    }

    /**
     * Restituisce la sequenza di nucleotidi di questa struttura secondaria.
     *
     * @return la sequenza di nucleotidi di questa struttura secondaria
     */
    public String getPrimarySequence() {
        return this.primarySequence;
    }

    /**
     * Restituisce l'insieme dei legami deboli di questa struttura secondaria.
     *
     * @return l'insieme dei legami deboli di questa struttura secondaria
     */
    public Set<WeakBond> getBonds() {
        return this.bonds;
    }

    /**
     * Determina se questa struttura contiene pseudonodi.
     *
     * @return true, se in questa struttura ci sono almeno due legami deboli che
     *         si incrociano, false altrimenti
     */
    public boolean isPseudoknotted() {

        List<WeakBond> bondList = new ArrayList<>(this.bonds);
        for(int i = 0; i < bondList.size(); i++) {
            WeakBond b1 = bondList.get(i);
            for(int j = i+1; j < bondList.size(); j++) {
                WeakBond b2 = bondList.get(j);
                /*
                i′ < i < j < j′, cioè la coppia (i′, j′) “contiene” la coppia (i, j);
                i < i′ < j′ < j, cioè la coppia (i, j) “contiene” la coppia (i′, j′);
                j′ < i, cioè la coppia (i′, j′) “precede” la coppia (i, j);
                j < i′, cioè la coppia (i′, j′) “segue” la coppia (i, j);
                */
                if (!((b2.getI() < b1.getI() && b1.getI() < b1.getJ() && b1.getJ() < b2.getJ()) ||
                        (b1.getI() < b2.getI() && b2.getI() < b2.getJ() && b2.getJ() < b1.getJ()) ||
                        (b2.getJ() < b1.getI())||
                        (b1.getJ() < b2.getI()))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Aggiunge un legame debole a questa struttura.
     *
     * @param b
     *              il legame debole da aggiungere
     * @return true se il legame è stato aggiunto, false se era già presente
     *
     * @throws NullPointerException
     *                                       se il legame passato è nullo
     * @throws IndexOutOfBoundsException
     *                                       se almeno uno uno dei due indici
     *                                       del legame debole passato esce
     *                                       fuori dai limiti della sequenza
     *                                       primaria di questa struttura
     * @throws IllegalArgumentException
     *                                       se il legame debole passato
     *                                       connette due nucleotidi a formare
     *                                       una coppia non consentita.
     */
    public boolean addBond(WeakBond b) {

        if (b == null) {
            throw  new NullPointerException();
        }

        if(b.getI() < 1 || primarySequence.length() < b.getJ()){
            throw  new IndexOutOfBoundsException();
        }

        char charI = primarySequence.charAt(b.getI()-1);
        char charJ = primarySequence.charAt(b.getJ()-1);

        if (!(isValid(charI, charJ, 'G', 'C') ||
                isValid(charI, charJ, 'A', 'U') ||
                isValid(charI, charJ, 'U', 'G'))
        ) {
            throw new IllegalArgumentException();
        }

        return this.bonds.add(b);

    }

    private boolean isValid(char n1, char n2, char validN1, char validN2) {
        return n1 == validN2 && n2 == validN1 || n1 == validN1 && n2 == validN2;
    }

    /**
     * Restituisce il numero di legami deboli presenti in questa struttura.
     *
     * @return il numero di legami deboli presenti in questa struttura
     */
    public int getCardinality() {
        return this.bonds.size();
    }

    /**
     * Restituisce una stringa contenente la rappresentazione nella notazione
     * dot-bracket di questa struttura secondaria.
     *
     * @return una stringa contenente la rappresentazione nella notazione
     *         dot-bracket di questa struttura secondaria
     *
     * @throws IllegalStateException
     *                                   se questa struttura secondaria contiene
     *                                   pseudonodi
     */
    public String getDotBracketNotation() {

        if(this.isPseudoknotted())
            throw new IllegalStateException();

        // AAGACCUGCACGCUAGUU
        //.(((..).(..)))....
        StringBuilder bondsNotation = new StringBuilder();
        for(int i = 0; i < this.primarySequence.length(); i++)
            bondsNotation.append(".");

        for (WeakBond b : this.bonds) {
            bondsNotation.setCharAt(b.getI()-1, '(');
            bondsNotation.setCharAt(b.getJ()-1, ')');
        }

        return this.primarySequence + "\n" + bondsNotation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bonds == null) ? 0 : bonds.hashCode());
        result = prime * result
                + ((primarySequence == null) ? 0 : primarySequence.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SecondaryStructure))
            return false;
        SecondaryStructure other = (SecondaryStructure) obj;
        if (bonds == null) {
            if (other.bonds != null)
                return false;
        } else if (!bonds.equals(other.bonds))
            return false;
        if (primarySequence == null) {
            if (other.primarySequence != null)
                return false;
        } else if (!primarySequence.equals(other.primarySequence))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        Iterator<WeakBond> i = this.bonds.iterator();
        if (i.hasNext()) {
            WeakBond current = i.next();
            while (i.hasNext()) {
                WeakBond next = i.next();
                sb.append(current.toString() + ", ");
                if (!i.hasNext()) {
                    sb.append(next.toString());
                    break;
                }
                current = next;
            }
        }
        sb.append("}");
        return sb.toString();
    }

}
