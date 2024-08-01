package it.unicam.cs.asdl2122.pt2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Gli oggetti di questa classe sono calcolatori di cammini minimi con sorgente
 * singola su un certo graph orientato e pesato dato. Il graph su cui lavorare
 * deve essere passato quando l'oggetto calcolatore viene costruito e non può
 * contenere archi con pesi negativi. Il calcolatore implementa il classico
 * algoritmo di Dijkstra per i cammini minimi con sorgente singola utilizzando
 * una coda con priorità che estrae l'elemento con priorità minima e aggiorna le
 * priorità con l'operazione decreasePriority in tempo logaritmico (coda
 * realizzata con uno heap binario). In questo caso il tempo di esecuzione
 * dell'algoritmo di Dijkstra è {@code O(n log m)} dove {@code n} è il numero di
 * nodi del graph e {@code m} è il numero di archi.
 *
 * @author Template: Luca Tesei
 *
 * @param <L>
 *                il tipo delle etichette dei nodi del graph
 */
public class DijkstraShortestPathComputer<L>
        implements SingleSourceShortestPathComputer<L> {

    private GraphNode<L> lastSource;

    private final Graph<L> graph;

    private boolean isComputed = false;

    // Coda con priorità usata dall'algoritmo
    private BinaryHeapMinPriorityQueue queue;

    /**
     * Crea un calcolatore di cammini minimi a sorgente singola per un graph
     * diretto e pesato privo di pesi negativi.
     *
     * @param graph
     *                  il graph su cui opera il calcolatore di cammini minimi
     * @throws NullPointerException
     *                                      se il graph passato è nullo
     *
     * @throws IllegalArgumentException
     *                                      se il graph passato è vuoto
     *
     * @throws IllegalArgumentException
     *                                      se il graph passato non è orientato
     *
     * @throws IllegalArgumentException
     *                                      se il graph passato non è pesato,
     *                                      cioè esiste almeno un arco il cui
     *                                      peso è {@code Double.NaN}
     * @throws IllegalArgumentException
     *                                      se il graph passato contiene almeno
     *                                      un peso negativo
     */
    public DijkstraShortestPathComputer(Graph<L> graph) {

        if(graph == null)
            throw new NullPointerException();
        if(graph.isEmpty() || !graph.isDirected())// se il grafo e vuoto o non e orientato
            throw new IllegalArgumentException();

        for(GraphEdge<L> edge: graph.getEdges()) {
            if(!edge.hasWeight() || edge.getWeight() < 0)
                throw new IllegalArgumentException();
        }

        this.graph = graph;
        this.queue = new BinaryHeapMinPriorityQueue();
    }

    @Override
    public void computeShortestPathsFrom(GraphNode<L> sourceNode) {
        //calcolo dei cammini minimi a partire da una sorgente data
        if(sourceNode == null)
            throw new NullPointerException();
        if(graph.getNode(sourceNode) == null) {
            throw new IllegalArgumentException();
        }
        for(GraphEdge<L> edge: graph.getEdges()) {//se almeno un arco ha peso negativo, dijkstra non funziona
            if(edge.getWeight() < 0)
                throw new IllegalStateException();
        }
        this.lastSource = sourceNode;
        sourceNode.setFloatingPointDistance(0);// essendo il primo nodo ha distanza zero con il nodo precedente
        queue.clear();
        queue.insert(sourceNode);

        for(GraphNode<L> node : graph.getNodes()) {// ora fa un for each per controllare tutti i
            // nodi dopo il primo
            if(!node.equals(sourceNode)) {// il nodo che stiamo controllando deve essere diverso dal nodo sorgente
                node.setFloatingPointDistance(Double.MAX_VALUE);// visto che per noi ancora e ignota la distanza dagli
                // alti nodi gli diamo come valore infinito
                node.setPrevious(null);//ancora non sappiamo chi sara il predecessore
                queue.insert(node);
            }
        }
        // algoritmo di Dijkstra per i cammini minimi
        while(!queue.isEmpty()) {
            GraphNode<L> el = (GraphNode<L>) queue.extractMinimum();// possiamo fare il cast perche abbiamo inserito solo
            //GraphNode
            for(GraphEdge<L> edge : graph.getEdgesOf(el)) {
                GraphNode<L> neighbour = edge.getNode2();
                if(queue.getBinaryHeap().contains(neighbour)) {
                    double newDistance = el.getFloatingPointDistance() + edge.getWeight();
                    if(newDistance < neighbour.getFloatingPointDistance()) {
                        neighbour.setPrevious(el);
                        queue.decreasePriority(neighbour, newDistance);
                    }
                }
            }
        }

        this.isComputed = true;
    }

    @Override
    public boolean isComputed() {
        return this.isComputed;
    }

    @Override
    public GraphNode<L> getLastSource() {
        if(!this.isComputed) {
            throw new IllegalStateException();
        }
        return this.lastSource;
    }

    @Override
    public Graph<L> getGraph() {
        return this.graph;
    }

    @Override
    public List<GraphEdge<L>> getShortestPathTo(GraphNode<L> targetNode) {
        if(targetNode == null)
            throw new NullPointerException();
        if(this.graph.getNode(targetNode) == null)
            throw new IllegalArgumentException();
        if(!this.isComputed)
            throw new IllegalStateException();

        GraphNode<L> currNode = targetNode;
        List<GraphEdge<L>> path = new ArrayList<>();
        // ricostruisco il percorso dal nodo target fino alla sorgente
        while(currNode != null && !currNode.equals(this.lastSource)) {
            if(currNode.getPrevious() != null) {
                GraphEdge<L> newEdge = this.graph.getEdge(currNode.getPrevious(), currNode);
                path.add(newEdge);
            }
            currNode = currNode.getPrevious();
        }
        if(currNode == null)
            return null;
        Collections.reverse(path);
        return path;
    }

    /*
     * Metodo inserito per scopi di test JUnit
     */
    protected BinaryHeapMinPriorityQueue getQueue() {
        return this.queue;
    }

}
