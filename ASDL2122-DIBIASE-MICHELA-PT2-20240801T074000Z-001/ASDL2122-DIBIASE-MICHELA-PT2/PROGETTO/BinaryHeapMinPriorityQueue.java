/**
 *
 */
package it.unicam.cs.asdl2122.pt2;

import java.util.ArrayList;
import java.util.NoSuchElementException;



//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE


/**
 * Implementazione di una coda con priorità tramite heap binario. Gli oggetti
 * inseriti in coda implementano l'interface PriorityQueueElement che permette
 * di gestire la priorità e una handle dell'elemento. La handle è fondamentale
 * per realizzare in tempo logaritmico l'operazione di decreasePriority che,
 * senza la handle, dovrebbe cercare l'elemento all'interno dello heap e poi
 * aggiornare la sua posizione. Nel caso di heap binario rappresentato con una
 * ArrayList la handle è semplicemente l'indice dove si trova l'elemento
 * nell'ArrayList. Tale campo naturalmente va tenuto aggiornato se l'elemento
 * viene spostato in un'altra posizione.
 *
 * @author Template: Luca Tesei
 *
 *
 */
public class BinaryHeapMinPriorityQueue {

    /*
     * ArrayList per la rappresentazione dello heap. Vengono usate tutte le
     * posizioni (la radice dello heap è quindi in posizione 0).
     */
    private ArrayList<PriorityQueueElement> heap;

    /**
     * Crea una coda con priorità vuota.
     *
     */
    public BinaryHeapMinPriorityQueue() {
        this.heap = new ArrayList<PriorityQueueElement>();
    }

    /**
     * Add an element to this min-priority queue. The current priority
     * associated with the element will be used to place it in the correct
     * position in the heap. The handle of the element will also be set
     * accordingly.
     *
     * @param element
     *                    the new element to add
     * @throws NullPointerException
     *                                  if the element passed is null
     */
    public void insert(PriorityQueueElement element) {
        if(element == null) {
            throw new NullPointerException();
        }
        heap.add(element);
        int i = heap.size()-1;
        PriorityQueueElement current = element;
        if(i <= 0) {// significa che c'e solo un l'emento
            return;
        }
        int fatherI = (i-1)/2;
        PriorityQueueElement father = heap.get(fatherI);
        while(i > 0 && current.getPriority() < father.getPriority()) {// confronta se il padre
            // e minore del figlio se si entra nel while
            heap.set(i, father); // modififica nella posizione del figlio ci mette il valore del padre
            father.setHandle(i);
            heap.set(fatherI, current);// nella posizione del padre ci va il valore del figlio
            current.setHandle(fatherI);
            i = fatherI; // i ora prende la posizione del padre perche deve controllare i nodi successivi
            if(i > 0) {
                fatherI = (i-1)/2;
                current = heap.get(i);// restituisce la posizione del elemrnt
                father = heap.get(fatherI);// restituisce la posizione del padre
            }
        }
    }

    /**
     * Returns the current minimum element of this min-priority queue without
     * extracting it. This operation does not affect the heap.
     *
     * @return the current minimum element of this min-priority queue
     *
     * @throws NoSuchElementException
     *                                    if this min-priority queue is empty
     */
    public PriorityQueueElement minimum() {
        if(heap.isEmpty()) {
            throw new NoSuchElementException();
        }
        return heap.get(0);// il minimo e la radice
    }

    /**
     * Extract the current minimum element from this min-priority queue. The
     * ternary heap will be updated accordingly.
     *
     * @return the current minimum element
     * @throws NoSuchElementException
     *                                    if this min-priority queue is empty
     */
    public PriorityQueueElement extractMinimum() {
        if(heap.isEmpty()) {
            throw new NoSuchElementException();
        }
        int i = 0;
        PriorityQueueElement root = heap.get(i);// la radice che dobbiamo estrarre
        while(2*i+1 < heap.size()) {// se la posizione del figlio sinistro e minore della lunghezza del heap
            PriorityQueueElement left = heap.get(2*i+1);
            if(2*i+2 < heap.size()) {
                PriorityQueueElement right = heap.get(2*i+2);
                if(left.getPriority() < right.getPriority()) { // se il dodo di sinistra e piu piccolo di
                    // quello di destra
                    heap.set(i, left);// modifico la radice con il valore di left
                    left.setHandle(i);
                    i = 2*i+1; // modifico la i mettrndogli la posizione del figlio di sinistra
                } else {
                    heap.set(i, right);// modifico la radice con il valore di right
                    right.setHandle(i);
                    i = 2*i+2;// modifico la i mettrndogli la posizione del figlio di destra
                }
            } else {// se non trovo niente a destra prendo quelo di sinistra
                heap.set(i, left);// modifico la radice con il valore di left
                left.setHandle(i);
                i = 2*i+1;// modifico la i mettrndogli la posizione del figlio di sinistra
            }

        }
        heap.remove(i);// rimuoviamo la radice
        return root;// ritorniamo la radice che avevamo posizionato sulla variabile d'appoggio
    }

    /**
     * Decrease the priority associated to an element of this min-priority
     * queue. The position of the element in the heap must be changed
     * accordingly. The changed element may become the minimum element. The
     * handle of the element will also be changed accordingly.
     *
     * @param element
     *                        the element whose priority will be decreased, it
     *                        must currently be inside this min-priority queue
     * @param newPriority
     *                        the new priority to assign to the element
     *
     * @throws NoSuchElementException
     *                                      if the element is not currently
     *                                      present in this min-priority queue
     * @throws IllegalArgumentException
     *                                      if the specified newPriority is not
     *                                      strictly less than the current
     *                                      priority of the element
     */
    public void decreasePriority(PriorityQueueElement element,double newPriority) {

        if(!heap.contains(element)) {// se non e presente l'elemnto
            throw new NoSuchElementException();// lancia l'eccezione
        }
        if(newPriority >= element.getPriority()) {// la nuova priorita deve essere minore
            throw new IllegalArgumentException();// altrimenti lancia l'eccezione
        }

        element.setPriority(newPriority);// modifica la prioria vecchia con quella nuova
        int i = element.getHandle();//la posizione dell'elemento con nuova priorita
        int fatherI = (i-1)/2; // posizione del padre
        PriorityQueueElement father = heap.get(fatherI); // l'elemento che si trova nella posizione del padre
        while(i > 0 && element.getPriority() < father.getPriority()) {// fin quando la nuova priorita e minore di quella del padre
            heap.set(i, father);// modifica l'elemnto in posizione i con l'elemento del father
            father.setHandle(i);
            heap.set(fatherI, element);// modifica l'elemnto in posizione del father con element
            element.setHandle(fatherI);
            i = fatherI; // i  prende la posizione del padre
            if(i > 0) {
                fatherI = (i-1)/2;
                element = heap.get(i);// restituisce la posizione del elemrnt
                father = heap.get(fatherI);// restituisce la posizione del father
            }
        }
    }

    /**
     * Determines if this priority queue is empty.
     *
     * @return true if this priority queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Return the current size of this queue.
     *
     * @return the number of elements currently in this queue.
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Erase all the elements from this min-priority queue. After this operation
     * this min-priority queue is empty.
     */
    public void clear() {
        this.heap.clear();
    }

    /*
     * Metodo inserito per fini di test JUnit
     */
    protected ArrayList<PriorityQueueElement> getBinaryHeap() {
        return this.heap;
    }

}
