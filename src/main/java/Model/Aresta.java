/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Alison Rodolfo
 */
public class Aresta  implements Comparable<Aresta> {

    public int saida, destino, peso;

    public Aresta(int saida, int destino, int peso) {
        this.saida = saida;
        this.destino = destino;
        this.peso = peso;
    }

    public Aresta() {
    }

    public int getSaida() {
        return saida;
    }

    public void setSaida(int saida) {
        this.saida = saida;
    }

    public int getDestino() {
        return destino;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
    
    

    // Comparator function used for sorting edges  
    // based on their weight 
    public int compareTo(Aresta compareEdge) {
                return this.peso - compareEdge.peso;
    }

}
