import java.util.Locale;
import java.util.ArrayList;
import java.util.List;

public class Grafo{
    private double[][] matrizValores;
    private int numeroVertices;
    private int numeroArestas;
    private double densidade;
    public Grafo(String enderecoArquivoEntrada){
        try{
            LeitorArquivo arquivo = new LeitorArquivo(enderecoArquivoEntrada);
            arquivo.lerArquivo();
            this.matrizValores = arquivo.getMatrizValores();
            this.numeroVertices = arquivo.getNumeroLinhas();    
            this.numeroArestas = contarNumeroArestas();
            this.densidade = densidade();
        }catch(Exception e){
            System.out.println("Falha ao inicializar grafo! "+ e);
        }
    }

    // -------- Getters ---------
    public int getOrdem(){
        return this.numeroVertices;
    }
    public int getTamanho(){
        return this.numeroArestas;
    }
    public double getDensidade(){
        return this.densidade;
    }

    // -------- ***** ----------
    public int contarNumeroArestas(){
        int contadorArestas = 0;
        for(int i = 0; i < numeroVertices; i++){
            for(int j = 0; j < numeroVertices; j++){
                if(matrizValores[i][j] != 0){
                    contadorArestas++;
                }
            }
        }
        //Numero de valores na matriz / 2, por conta de ser uma matriz simétrica
        return (contadorArestas / 2);
    }

    public double densidade(){
        if(this.numeroVertices < 2){
            return 0.0;
        }
        double densidade = ((2.0 * this.numeroArestas) / (this.numeroVertices * (this.numeroVertices - 1)));
        return densidade;
    }

    public List<Integer> vizinhos(int vertice) {
        List<Integer> vizinhos = new ArrayList<>();
    
        // Ajuste o índice para a matriz (vertice - 1) porque a matriz usa índices baseados em 0
        for (int i = 0; i < this.numeroVertices; i++) {
            // Verifica se há uma aresta entre o vértice fornecido (vertice-1) e o vértice i
            if (matrizValores[vertice - 1][i] != 0 && vertice - 1 != i) {
                // Adiciona i + 1, pois o vértice começa em 1, e o índice da matriz é baseado em 0
                vizinhos.add(i + 1);
            }
        }
        
        return vizinhos;
    }
    
    

    
    public void mostrarMatriz() {
        System.out.println("Matriz de Valores:");
        for (int i = 0; i < this.numeroVertices; i++) {
            for (int j = 0; j < this.numeroVertices; j++) {
                System.out.print(this.matrizValores[i][j] + " ");
            }
            System.out.println();
        }
    }



    //TESTE DA CLASSE
    public static void main(String[] Args){
        Locale.setDefault(Locale.US);
        Grafo grafo = new Grafo("./dados/grafo.txt");
        grafo.mostrarMatriz();
        System.out.printf("Número de Vértices: %d\n", grafo.getOrdem());
        System.out.printf("Número de Arestas: %d\n ", grafo.getTamanho());
        System.out.printf("Densidade do Grafo: %.2f\n", grafo.getDensidade());
        int vertice = 1;
        List<Integer> vizinhos = grafo.vizinhos(vertice);
        System.out.printf("Vizinhos do Vértice %d: %s", vertice, vizinhos);
    }

}
