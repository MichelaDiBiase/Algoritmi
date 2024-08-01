/**
 *
 */
package it.unicam.cs.asdl2122.pt2;

import java.util.*;



// ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe che implementa un grafo orientato tramite matrice di adiacenza. Non
 * sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 *
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 *
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato (che può cambiare nel tempo). Il
 * dominio della mappa rappresenta quindi l'insieme dei nodi.
 *
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco.
 *
 * Questa classe supporta i metodi di cancellazione di nodi e archi e supporta
 * tutti i metodi che usano indici, utilizzando l'indice assegnato a ogni nodo
 * in fase di inserimento ed eventualmente modificato successivamente.
 *
 * @author Luca Tesei (template)
 *
 *
 */
public class AdjacencyMatrixDirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixDirectedGraph() {
        matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public int nodeCount() {
        return matrix.size();
    }

    @Override
    public int edgeCount() {
        int counter = 0;
        //conto tutti gli archi che non sono null
        for(int i = 0; i < matrix.size(); i++) {
            for(int j = 0; j < matrix.size(); j++) {
                if(matrix.get(i).get(j) != null) {
                    counter++;
                }
            }
        }
        return counter;
    }

    @Override
    public void clear() {
        // usiamo il metodo clear per svuotare sia la matrice con gli archi sia nodesIndex coi nodi
        matrix.clear();
        nodesIndex.clear();
    }

    @Override
    public boolean isDirected() {
        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
        // prima controlliamo tutte le possibile eccezioni
        if(node == null)// se il nodo e null lancia l'eccezione
            throw new NullPointerException();
        if(getNode(node) != null)// se il nodo e gia prensete return false
            return false;
        ArrayList<GraphEdge<L>> newRow = new ArrayList<>();
        for(int i = 0; i < matrix.size(); i++)
            newRow.add(null);// creiamo la riga e gli assenniamo tutti valori nulli
        this.matrix.add(newRow);// aggiungiamo una riga
        for(int i = 0; i < matrix.size(); i++) {// aggiungiamo una colonna
            this.matrix.get(i).add(null);
        }
        this.nodesIndex.put(node, matrix.size()-1); // aggiungiamo il nodo con il suo indice su nodesIndex
        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {

        if(label == null)
            throw new NullPointerException();
        GraphNode<L> newNode = new GraphNode<>(label);
        return addNode(newNode);
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {

        if(node == null)// se il nodo e null lancia l'eccezione
            throw new NullPointerException();

        if(!nodesIndex.containsKey(node))//se il nodo passato non esiste in questo grafo
            throw new IllegalArgumentException();


        int index = nodesIndex.get(node);
        matrix.remove(index);// rimuovo la riga
        for (ArrayList<GraphEdge<L>> row : matrix) {// rimuovo la colonna
            row.remove(index);
        }
        nodesIndex.remove(node);
        //aggiusto gli indici su nodeIndex dopo aver rimosso il nodo
        for (GraphNode<L> currNode : nodesIndex.keySet()) {
            int currIndex = nodesIndex.get(currNode);
            if(currIndex > index) {
                nodesIndex.put(currNode, currIndex-1);
            }
        }
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
        if(label == null)
            throw new NullPointerException();
        GraphNode<L> nodeToRemove = new GraphNode<>(label);// inseriamo lable in un oggetto
        removeNode(nodeToRemove);// succesivamente richiamiamo il metodo removeNode
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        GraphNode<L> node = getNodeFrom(i);
        if(node == null)
            throw new IndexOutOfBoundsException();
        removeNode(node);
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {

        if(node == null)
            throw new NullPointerException();

        for (GraphNode<L> currNode : nodesIndex.keySet()) {
            if(currNode.equals(node)) {
                return currNode;
            }
        }
        return null;
    }

    @Override
    public GraphNode<L> getNode(L label) {
        GraphNode<L> node = new GraphNode<>(label);
        return getNode(node);
    }

    @Override
    public GraphNode<L> getNode(int i) {
        GraphNode<L> node = getNodeFrom(i);
        if(node == null)
            throw new IndexOutOfBoundsException();
        return getNode(node);
    }

    /**
     * returns the node from its index
     * @param i as the index
     * @return the node from its index
     */
    private GraphNode<L> getNodeFrom(int i) {
        for (GraphNode<L> currNode : nodesIndex.keySet()) {
            int currIndex = nodesIndex.get(currNode);
            if(currIndex == i) {
                return currNode;
            }
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        if(node == null)
            throw new NullPointerException();
        if(!nodesIndex.containsKey(node))
            throw new IllegalArgumentException();
        return nodesIndex.get(node);
    }

    @Override
    public int getNodeIndexOf(L label) {
        GraphNode<L> node = new GraphNode<>(label);
        return getNodeIndexOf(node);
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return this.nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {

        if(edge == null)
            throw new NullPointerException();

        GraphNode<L> node1 = edge.getNode1();
        GraphNode<L> node2 = edge.getNode2();
        //index1 e index2 indicano la posizione map nodesIndex
        Integer index1 = nodesIndex.get(node1);
        Integer index2 = nodesIndex.get(node2);

        if(index1 == null || index2 == null) {
            throw new IllegalArgumentException();
        }

        if(!edge.isDirected())
            throw new IllegalArgumentException();
        //trovo l'arco
        GraphEdge<L> foundEdge = matrix.get(index1).get(index2);
        if(foundEdge != null)
            return false;
        matrix.get(index1).set(index2, edge);
        return true;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, true);// insersce i dati nell'oggetto
        return addEdge(edge);// successivamente richiama addEdge
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2,
                                   double weight) {
        GraphEdge<L> edge =  new GraphEdge<>(node1, node2, true, weight);// insersce i dati nell'oggetto
        return addEdge(edge);// successivamente richiama addEdge
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        GraphNode<L> node1 = new GraphNode<L>(label1);// insersce i dati nell'oggetto
        GraphNode<L> node2 = new GraphNode<L>(label2);
        return addEdge(node1, node2);// successivamente richiama addEdge
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        GraphNode<L> node1 = new GraphNode<L>(label1);// insersce i dati nell'oggetto
        GraphNode<L> node2 = new GraphNode<L>(label2);
        return addWeightedEdge(node1, node2, weight);// successivamente richiama addWeightedEdge
    }

    @Override
    public boolean addEdge(int i, int j) {
        GraphNode<L> node1 = getNodeFrom(i);
        GraphNode<L> node2 = getNodeFrom(j);
        if(node1 == null || node2 == null) {
            throw new IndexOutOfBoundsException();
        }
        return addEdge(node1, node2);
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        GraphNode<L> node1 = getNodeFrom(i);
        GraphNode<L> node2 = getNodeFrom(j);
        if(node1 == null || node2 == null) {
            throw new IndexOutOfBoundsException();
        }
        return addWeightedEdge(node1, node2, weight);
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {

        if(edge == null)
            throw new NullPointerException();
        GraphNode<L> node1 = edge.getNode1();
        GraphNode<L> node2 = edge.getNode2();
        //index1 e index2 indicano la posizione map nodesIndex
        Integer index1 = nodesIndex.get(node1);
        Integer index2 = nodesIndex.get(node2);
        // coi facendo possiamo vedere se i vaori sono allinterno di nodesIndex
        if(index1 == null || index2 == null) {
            throw new IllegalArgumentException();
        }
        GraphEdge<L> foundEdge = matrix.get(index1).get(index2);
        if(foundEdge != null && foundEdge.equals(edge)) { // se all'interno della matrice l'arco non e nullo
            matrix.get(index1).set(index2, null);// allora lo rimuvo mettendo nullo
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, true);// insersce i dati nell'oggetto
        removeEdge(edge);// successivamente richiama removeEdge
    }

    @Override
    public void removeEdge(L label1, L label2) {
        GraphNode<L> node1 = new GraphNode<>(label1);// insersce i dati nell'oggetto
        GraphNode<L> node2 = new GraphNode<>(label2);
        removeEdge(node1, node2);// successivamente richiama removeEdge
    }

    @Override
    public void removeEdge(int i, int j) {
        GraphNode<L> node1 = getNodeFrom(i);// insersce i dati nell'oggetto
        GraphNode<L> node2 = getNodeFrom(j);
        if(node1 == null || node2 == null) {
            throw new IndexOutOfBoundsException();// contolla se i nodi non sono nulli
        }
        removeEdge(node1, node2);// successivamente richiama removeEdge
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        //controlliamo le eccezioni
        if(edge == null)
            throw new NullPointerException();
        GraphNode<L> node1 = edge.getNode1();
        GraphNode<L> node2 = edge.getNode2();
        //index1 e index2 indicano la posizione map nodesIndex
        Integer index1 = nodesIndex.get(node1);
        Integer index2 = nodesIndex.get(node2);
        // coi facendo possiamo vedere se i vaori sono allinterno di nodesIndex
        if(index1 == null || index2 == null) {
            throw new IllegalArgumentException();
        }
        GraphEdge<L> foundEdge = matrix.get(index1).get(index2);
        if(foundEdge != null && foundEdge.equals(edge)) {// se l'arco e stato trovato
            return foundEdge;
        } else {
            return null;
        }
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        GraphEdge<L> edge = new GraphEdge<>(node1, node2, true);// insersce i dati nell'oggetto
        return getEdge(edge);// successivamente richiama getEdge
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        GraphNode<L> node1 = new GraphNode<>(label1);// insersce i dati nell'oggetto
        GraphNode<L> node2 = new GraphNode<>(label2);
        return getEdge(node1, node2);// successivamente richiama getEdge
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        GraphNode<L> node1 = getNodeFrom(i);// insersce i dati nell'oggetto
        GraphNode<L> node2 = getNodeFrom(j);
        if(node1 == null || node2 == null) {// contolla se i nodi non sono nulli
            throw new IndexOutOfBoundsException();
        }
        return getEdge(node1, node2);// successivamente richiama getEdge
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {

        if(node == null)
            throw new NullPointerException();
        Integer index = nodesIndex.get(node);
        if(index == null)
            throw new IllegalArgumentException();

        Set<GraphNode<L>> adjacentNodes = new HashSet<>();
        ArrayList<GraphEdge<L>> row = matrix.get(index);// a partire dalla riga del nodo node
        for(int j = 0; j < row.size(); j++) {
            if(row.get(j) != null) {// se il nodo e adiacente
                adjacentNodes.add(row.get(j).getNode2());// lo aggiungo all'indieme
            }
        }
        return adjacentNodes;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        GraphNode<L> node = new GraphNode<>(label);// insersce i dati nell'oggetto
        return getAdjacentNodesOf(node);// successivamente richiama getAdjacentNodesOf
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        GraphNode<L> node = getNodeFrom(i);// inserisco come valore un nodo in un graphNode
        if(node == null)// controllo le eccezioni
            throw new NullPointerException();
        return getAdjacentNodesOf(node);// successivamente richiama getAdjacentNodesOf
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {

        if(node == null)
            throw new NullPointerException();
        Integer index = nodesIndex.get(node);
        if(index == null)
            throw new IllegalArgumentException();

        Set<GraphNode<L>> predecessors = new HashSet<>();
        for(int i = 0; i < matrix.size(); i++) {// scorro gli altri nodi (le righe)
            GraphEdge<L> edge = matrix.get(i).get(index);
            if(edge != null) {// se c'e un arco collegato a node
                predecessors.add(edge.getNode1());// allora inseriamo il nodo nella lista
            }
        }
        return predecessors;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        GraphNode<L> node = new GraphNode<>(label);// insersce i dati nell'oggetto
        return getPredecessorNodesOf(node);// successivamente richiama getPredecessorNodesOf
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        GraphNode<L> node = getNodeFrom(i);// inserisco come valore un nodo in un graphNode
        if(node == null)// controllo le eccezioni
            throw new NullPointerException();
        return getPredecessorNodesOf(node);// successivamente richiama getPredecessorNodesOf
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if(node == null)
            throw new NullPointerException();
        Integer index = nodesIndex.get(node);
        if(index == null)
            throw new IllegalArgumentException();

        Set<GraphEdge<L>> edges = new HashSet<>();
        ArrayList<GraphEdge<L>> row = matrix.get(index);// parto dalla riga del nodo node
        for(int j = 0; j < row.size(); j++) {
            if(row.get(j) != null) {//se l'arco e presente
                edges.add(row.get(j));//allora lo aggiungo all'insieme
            }
        }
        return edges;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        GraphNode<L> node = new GraphNode<>(label);//inserisco come valore un nodo in un graphNode
        return getEdgesOf(node);// successivamente richiama getEdgesOf
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        GraphNode<L> node = getNodeFrom(i);//inserisco come valore un nodo in un graphNode
        if(node == null)// controllo le eccezioni
            throw new NullPointerException();
        return getEdgesOf(node);// successivamente richiama getEdgesOf
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        if(node == null)
            throw new NullPointerException();
        Integer index = nodesIndex.get(node);
        if(index == null)
            throw new IllegalArgumentException();

        Set<GraphEdge<L>> edges = new HashSet<>();
        for(int i = 0; i < matrix.size(); i++) {// scorro le righe (altri nodi)
            GraphEdge<L> edge = matrix.get(i).get(index);
            if(edge != null) {// se l'arco e collegato
                edges.add(edge);// aggiunge alla lista
            }
        }
        return edges;
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        GraphNode<L> node = new GraphNode<>(label);//inserisco come valore un nodo in un graphNode
        return getIngoingEdgesOf(node);// successivamente richiama getIngoingEdgesOf
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        GraphNode<L> node = getNodeFrom(i);//inserisco come valore un nodo in un graphNode
        if(node == null)// controllo le eccezioni
            throw new NullPointerException();
        return getIngoingEdgesOf(node);// successivamente richiama getIngoingEdgesOf
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        Set<GraphEdge<L>> edges = new HashSet<>();
        for(int i = 0; i < nodeCount(); i++) {
            for(int j = 0; j < nodeCount(); j++) {
                if(matrix.get(i).get(j) != null)
                    edges.add(matrix.get(i).get(j));// cicla la matrice e se la posicinone controllata non e nulla la
                                                    // inserisce in edges
            }
        }
        return edges;
    }
}
