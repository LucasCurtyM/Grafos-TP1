import java.util.*;

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

    public int grauVertice(int vertice){
        int contadorGrau = 0;

        for(int i = 0; i < this.numeroVertices; i++){
            if(matrizValores[vertice - 1][i] != 0 && vertice - 1 != i){
                contadorGrau++;
            }
        }
        return contadorGrau;
        //Tentar simplificar
        //return ((ArrayList<Integer>)vizinhos(vertice)).lenght();
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

    public boolean verificaArticulacao(int vertice) {
        List<Integer> visitados = new ArrayList<>();

        // Executa buscaProfundidade a partir de um vértice diferente do que será ignorado
        for (int i = 1; i <= this.numeroVertices; i++) { // Começa em 1
            if (i != vertice && !visitados.contains(i)) {
                buscaProfundidade(i, vertice, visitados);
                break; // buscaProfundidade começa de apenas um componente
            }
        }

        // Verifica se todos os vértices (exceto o removido) foram visitados
        for (int i = 1; i <= this.numeroVertices; i++) { // Começa em 1
            if (i != vertice && !visitados.contains(i)) {
                return true; // Se algum vértice não foi visitado, é articulação
            }
        }

        return false;
    }

    private void buscaProfundidade(int atual, int ignorar, List<Integer> visitados) {
        visitados.add(atual);

        for (int vizinho = 1; vizinho <= this.numeroVertices; vizinho++) { // Começa em 1
            if (this.matrizValores[atual - 1][vizinho - 1] != 0.0 // Ajuste de índice
                    && vizinho != ignorar && !visitados.contains(vizinho)) {
                buscaProfundidade(vizinho, ignorar, visitados);
            }
        }
    }

    public List<String> arestasForaArvoreLargura(int verticeInicial) {
        double[][] matrizVerificação = this.matrizValores;
        boolean[] visitados = new boolean[this.numeroVertices];
        List<String> arestasNaoNaArvore = new ArrayList<>();
        Queue<Integer> fila = new LinkedList<>();

        fila.add(verticeInicial);
        visitados[verticeInicial - 1] = true;

        while (!fila.isEmpty()) {
            int verticeAtual = fila.poll();

            for (int vizinho = 1; vizinho <= this.numeroVertices; vizinho++) {
                if (this.matrizValores[verticeAtual - 1][vizinho - 1] != 0.0) {
                    if (!visitados[vizinho - 1]) {
                        fila.add(vizinho);
                        matrizVerificação[verticeAtual - 1][vizinho - 1] = -1;
                        matrizVerificação[vizinho - 1][verticeAtual - 1] = -1;
                        visitados[vizinho - 1] = true;
                    }
                }
            }
        }

        for (int i = 0; i < this.numeroVertices; i++) {
            for (int j = 0; j < i; j++) {
                if (matrizVerificação[i][j] != 0.0 && matrizVerificação[i][j] != -1) {
                    arestasNaoNaArvore.add("(" + (j+1) + ", " + (i+1) + ")");
                }
            }
        }

        return arestasNaoNaArvore;
    }

    public List<Integer> ordemVerticesVisitados(int verticeInicial) {
        boolean[] visitados = new boolean[this.numeroVertices];
        List<Integer> ordemVisitados = new ArrayList<>();
        Queue<Integer> fila = new LinkedList<>();

        fila.add(verticeInicial);
        ordemVisitados.add(verticeInicial);
        visitados[verticeInicial - 1] = true;

        while (!fila.isEmpty()) {
            int verticeAtual = fila.poll();

            for (int vizinho = 1; vizinho <= this.numeroVertices; vizinho++) {
                if (this.matrizValores[verticeAtual - 1][vizinho - 1] != 0.0) {
                    if (!visitados[vizinho - 1]) {
                        fila.add(vizinho);
                        visitados[vizinho - 1] = true;
                        ordemVisitados.add(vizinho);
                    }
                }
            }
        }


        return ordemVisitados;
    }

    public List<List<Integer>> componentesConexas() {
        boolean[] visitados = new boolean[this.numeroVertices];
        List<List<Integer>> componentes = new ArrayList<>();

        // Loop para visitar cada vértice do grafo
        for (int vertice = 1; vertice <= this.numeroVertices; vertice++) {
            if (!visitados[vertice - 1]) {  // Se o vértice ainda não foi visitado
                List<Integer> componente = new ArrayList<>();
                buscaProfundidadeconexa(vertice, visitados, componente);
                componentes.add(componente);  // Adiciona a componente encontrada à lista de componentes
            }
        }
        return componentes;
    }

    private void buscaProfundidadeconexa(int vertice, boolean[] visitados, List<Integer> componente) {
        visitados[vertice - 1] = true;
        componente.add(vertice);  // Adiciona o vértice à componente

        // Explora os vizinhos do vértice
        for (int vizinho = 1; vizinho <= this.numeroVertices; vizinho++) {
            if (this.matrizValores[vertice - 1][vizinho - 1] != 0.0 && !visitados[vizinho - 1]) {
                buscaProfundidadeconexa(vizinho, visitados, componente);
            }
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
        System.out.printf("Vizinhos do Vértice %d: %s\n", vertice, vizinhos);
        
        System.out.printf("Grau do vértice %d: %d\n", vertice, grafo.grauVertice(vertice));

        for (int i=1; i<= grafo.numeroVertices; i++){
            System.out.println(grafo.verificaArticulacao(i));
        }
    }

}
