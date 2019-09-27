package com.alisonbarreiro.kruskal;

import Model.Aresta;
import Model.Nodo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class FXMLController implements Initializable {

    private static ArrayList<Nodo> nodos = new ArrayList<Nodo>();

    private static final String NAME = "NAME:";
    private static final String NODES = "DIMENSION:";

    private String nome;
    private int NUM_NODES;

    private boolean SELECT_HEAPSORT = false;
    private boolean SELECT_CountingSort = false;

    @FXML
    private Label label;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        //System.out.println("You clicked me!");
        //label.setText("Hello World!");
        addInstance("src/main/resources/instances-Kruskal/n1500C.txt");
        long inicio = System.currentTimeMillis();
        for(int i = 0; i < 10;i++){
            GrafoKruskal g = new GrafoKruskal();
             g.main(); 
        }
       
        long fim = System.currentTimeMillis();
        System.out.println("HeapSort: "+(fim-inicio));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    class GrafoKruskal {
        // Uma classe para representar uma aresta de grafo

        // Uma classe para representar um subconjunto para encontrar a união
        class subset {

            int parent, rank;
        };

        int V, A;    // V-> numero de vértices & A-> numero de arestas 
        Aresta arestas[]; // coleção de todas as arestas

        // Cria um grafo com vértices V e arestas A
        GrafoKruskal(int v, int a) {
            V = v;
            A = a;
            arestas = new Aresta[A];
            for (int i = 0; i < a; ++i) {
                arestas[i] = new Aresta();
            }
        }

        GrafoKruskal() {

        }

        // Uma função utilitária para encontrar o conjunto de um elemento i
        int find(subset subsets[], int i) {
            // encontre a cabeça e faça cabeça como pai de uma (compressão de caminho)
            if (subsets[i].parent != i) {
                subsets[i].parent = find(subsets, subsets[i].parent);
            }

            return subsets[i].parent;
        }

        // Uma função que faz a união de dois conjuntos de x e y 
        void Union(subset subsets[], int x, int y) {
            int xroot = find(subsets, x);
            int yroot = find(subsets, y);

            // Anexar árvore de classificação menor sob a raiz da árvore de classificação alta
            if (subsets[xroot].rank < subsets[yroot].rank) {
                subsets[xroot].parent = yroot;
            } else if (subsets[xroot].rank > subsets[yroot].rank) {
                subsets[yroot].parent = xroot;
            } // Se as classificações forem iguais, crie uma como cebeça e incremente
            else {
                subsets[yroot].parent = xroot;
                subsets[xroot].rank++;
            }
        }

        // A função  MAIN para construir o MST usando o algoritmo de Kruskal
        void KruskalMST() {
            Aresta result[] = new Aresta[V];  // Isso armazenará o MST resultante
            int e = 0;  // Avariável de índice n, usada para o resultado []
            int i = 0;  // Uma variável de índice, usada para arestas classificadas
            for (i = 0; i < V; ++i) {
                result[i] = new Aresta();
            }
            
            

            // Etapa 1: classifique todas as arestas em ordem não decrescente
            // peso. não é permitido alterar o grafo fornecido,
            // pode criar uma cópia da matriz de arestas
            // ALGORTIMO DE ORDENAÇÃO SOLICITADO NO TRABALHO
            
            if (SELECT_HEAPSORT) {
                heapSort(arestas);
            } else if (SELECT_CountingSort) {
                countingSort(arestas);
            } else {
                Arrays.sort(arestas);
            }

            // Alocar memória para criar V subsets
            subset subsets[] = new subset[V];
            for (i = 0; i < V; ++i) {
                subsets[i] = new subset();
            }

            // Create V subsets with single elements 
            for (int v = 0; v < V; ++v) {
                subsets[v].parent = v;
                subsets[v].rank = 0;
            }

            i = 0;  

            // O número de arestas a serem tiradas é igual a V-1
            while (e < V - 1) {
                // Etapa 2: Escolha a menor aresta. E incremento
                // o indice para a próxima iteração 
                Aresta next_edge = new Aresta();
                next_edge = arestas[i++];

                int x = find(subsets, next_edge.saida);
                int y = find(subsets, next_edge.destino);

                // Se a inclusão dessa aresta não causa ciclo,
                 // inclua no resultado e aumente o índice
                // do resultado para a próxima borda

                if (x != y) {
                    result[e++] = next_edge;
                    Union(subsets, x, y);
                }
                // Caso contrário, descarte a  next_edge 
            }

         
            
        }


        public void main() {

            ArrayList<Aresta> arestas = new ArrayList<Aresta>();

            int aux = 1;
            for (int i = 0; i < NUM_NODES - 1; i++) {

                for (int j = aux; j < NUM_NODES; j++) {
                    arestas.add(new Aresta(i, j, (int) Math.sqrt(Math.pow(nodos.get(j).getX() - nodos.get(i).getX(), 2) + Math.pow(nodos.get(j).getY() - nodos.get(i).getY(), 2))));

                }
                aux++;
            }

            int V = NUM_NODES; 
            int E = arestas.size(); 
            GrafoKruskal graph = new GrafoKruskal(V, E);

            for (int i = 0; i < arestas.size(); i++) {
                graph.arestas[i].saida = arestas.get(i).saida;
                graph.arestas[i].destino = arestas.get(i).destino;
                graph.arestas[i].peso = arestas.get(i).peso;
            }

            graph.KruskalMST();
        }
    }

    public void addInstance(String file) {

        System.out.println("LENDO ARQUIVO");
        try {
            FileReader arq = new FileReader(file);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            String dados;
            while (linha != null) {
                dados = linha.trim().replaceAll("\t", "").replaceAll("\\s+", " ");

                if (dados.startsWith(NAME)) {
                    //System.out.println("NOME: " + dados.substring(NAME.length()).replaceAll("\\s+", ""));
                    //textname.setText(dados.substring(NAME.length()).replaceAll("\\s+", ""));
                    //heuristica.setInstancia(dados.substring(NAME.length()).replaceAll("\\s+", ""));
                } else if (dados.startsWith(NODES)) {
                    // System.out.println("DIMENSION: " + dados.substring(DIMENSION.length()).replaceAll("\\s+", ""));
                    NUM_NODES = Integer.parseInt(dados.substring(NODES.length()).replaceAll("\\s+", ""));
                    // textdimension.setText(dados.substring(DIMENSION.length()).replaceAll("\\s+", ""));
                    //textcapacity.setText(dados.substring(CAPACITY.length()).replaceAll("\\s+", ""));
                } else if (dados.startsWith("DISPLAY_DATA_SECTION")) {
                    linha = lerArq.readLine();
                    break;
                }
                linha = lerArq.readLine(); // lê da segunda até a última linha
            }

            for (int i = 0; i < NUM_NODES; i++) {

                dados = linha.trim().replaceAll("\t", "").replaceAll("\\s+", " ");
                String tokens[] = dados.split(" ");

                nodos.add(new Nodo(Integer.parseInt(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));

                linha = lerArq.readLine();

            }

            arq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
        } finally {

        }
    }

    public void heapSort(Aresta arr[]) {
        int n = arr.length;

        // Build heap (rearrange array) 
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // One by one extract an element from heap 
        for (int i = n - 1; i >= 0; i--) {
            // Move current root to end 
            Aresta temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // call max heapify on the reduced heap 
            heapify(arr, i, 0);
        }
    }

    // To heapify a subtree rooted with node i which is 
    // an index in arr[]. n is size of heap 
    void heapify(Aresta arr[], int n, int i) {
        int largest = i; // Initialize largest as root 
        int l = 2 * i + 1; // left = 2*i + 1 
        int r = 2 * i + 2; // right = 2*i + 2 

        // If left child is larger than root 
        if (l < n && arr[l].getPeso() > arr[largest].getPeso()) {
            largest = l;
        }

        // If right child is larger than largest so far 
        if (r < n && arr[r].getPeso() > arr[largest].getPeso()) {
            largest = r;
        }

        // If largest is not root 
        if (largest != i) {
            Aresta swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Recursively heapify the affected sub-tree 
            heapify(arr, n, largest);
        }
    }

    /* A utility function to print array of size n */
    public void printArray(Aresta arr[]) {
        int n = arr.length;
        for (int i = 0; i < n; ++i) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public void countingSort(Aresta arr[]) {

        Aresta[] aux = new Aresta[arr.length];

        // find the smallest and the largest value
        Aresta min = arr[0];
        Aresta max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].getPeso() < min.getPeso()) {
                min = arr[i];
            } else if (arr[i].getPeso() > max.getPeso()) {
                max = arr[i];
            }
        }

        // init array of frequencies
        Aresta[] counts = new Aresta[max.getPeso() - min.getPeso() + 1];
        

        

        // init the frequencies
        for (int i = 0; i < arr.length; i++) {
            int auxa = arr[i].getPeso() - min.getPeso();
             counts[auxa++] = arr[i];
           
            //counts[arr[i] - min]++;
        }
        
         for(int i =0; i <counts.length;i++){
            System.out.print(counts[i]+" ");
        }
        /*

        // recalculate the array - create the array of occurences
        //counts[0]--;
        counts[arr[0].getPeso() - min.getPeso()].setPeso(counts[arr[0].getPeso() - min.getPeso()].getPeso()-1);
        for (int i = 1; i < counts.length; i++) {
            counts[i] = 
            counts[i].setPeso(counts[arr[i].getPeso() - min.getPeso()].getPeso()+1);
            counts[i] = counts[i] + counts[i - 1];
        }

        /*
      Sort the array right to the left
      1) Look up in the array of occurences the last occurence of the given value
      2) Place it into the sorted array
      3) Decrement the index of the last occurence of the given value
      4) Continue with the previous value of the input array (goto set1), 
         terminate if all values were already sorted
         */
 

    }
}
