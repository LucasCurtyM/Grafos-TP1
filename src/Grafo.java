import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

    public double[][] getMatrizValores() {
        return matrizValores;
    }

    public void setMatrizValores(double[][] matrizValores) {
        this.matrizValores = matrizValores;
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
    }



    public void mostrarMatriz() {
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
                 // buscaProfundidade começa de apenas um componente
                break;
            }
        }

        // Verifica se todos os vértices (exceto o removido) foram visitados
        for (int i = 1; i <= this.numeroVertices; i++) { // Começa em 1
            if (i != vertice && !visitados.contains(i)) {
                 // Se algum vértice não foi visitado, é articulação
                return true;
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
        double[][] matrizVerificação = new double[this.numeroVertices][this.numeroVertices];
        for (int i = 0; i < this.numeroVertices; i++) {
            matrizVerificação[i] = this.matrizValores[i].clone();
        }


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
             // Se o vértice ainda não foi visitado
            if (!visitados[vertice - 1]) { 
                List<Integer> componente = new ArrayList<>();
                buscaProfundidadeconexa(vertice, visitados, componente);
                componentes.add(componente); 
            }
        }
        return componentes;
    }

    private void buscaProfundidadeconexa(int vertice, boolean[] visitados, List<Integer> componente) {
        visitados[vertice - 1] = true;
        componente.add(vertice);

        // Explora os vizinhos do vértice
        for (int vizinho = 1; vizinho <= this.numeroVertices; vizinho++) {
            if (this.matrizValores[vertice - 1][vizinho - 1] != 0.0 && !visitados[vizinho - 1]) {
                buscaProfundidadeconexa(vizinho, visitados, componente);
            }
        }
    }

    public boolean verificaCiclos() {
        boolean[] visitados = new boolean[this.numeroVertices];
        int[] pais = new int[this.numeroVertices];
        Arrays.fill(pais, -1); // Inicializa os pais como -1

        // Verifica ciclos em todas as componentes conexas do grafo
        for (int verticeInicial = 1; verticeInicial <= this.numeroVertices; verticeInicial++) {
            if (!visitados[verticeInicial - 1]) { // Se o vértice ainda não foi visitado
                if (buscaProfundidadeDetectaCiclo(verticeInicial, visitados, pais)) {
                    //Ciclo encontrado
                    return true;
                }
            }
        }
        //Nenhum ciclo encontrado
        return false;
    }

    private boolean buscaProfundidadeDetectaCiclo(int verticeInicial, boolean[] visitados, int[] pais) {
        Queue<Integer> fila = new LinkedList<>();
        fila.add(verticeInicial);
        visitados[verticeInicial - 1] = true;

        while (!fila.isEmpty()) {
            int verticeAtual = fila.poll();

            for (int vizinho = 1; vizinho <= this.numeroVertices; vizinho++) {
                if (this.matrizValores[verticeAtual - 1][vizinho - 1] != 0.0) { // Há uma aresta
                    if (!visitados[vizinho - 1]) { // Vizinho não visitado
                        fila.add(vizinho);
                        visitados[vizinho - 1] = true;
                        pais[vizinho - 1] = verticeAtual; // Define o pai do vizinho
                    } else if (pais[verticeAtual - 1] != vizinho) { // Vizinho visitado, mas não é pai
                         // Ciclo encontrado
                        return true;
                    }
                }
            }
        }
         // Nenhum ciclo encontrado nesta componente
        return false;
    }



    public void caminhoMinimo(int verticeInicial) {
        double[] distancias = new double[this.numeroVertices];
        int[] predecessores = new int[this.numeroVertices];

        // Inicialização: distância infinita e nenhum predecessor
        Arrays.fill(distancias, Double.POSITIVE_INFINITY);
        Arrays.fill(predecessores, -1);

        // A distância do vértice inicial para ele mesmo é 0
        distancias[verticeInicial - 1] = 0;

        // Relaxamento das arestas
        for (int i = 1; i <= this.numeroVertices - 1; i++) {
            for (int u = 0; u < this.numeroVertices; u++) {
                for (int v = 0; v < this.numeroVertices; v++) {
                    if (this.matrizValores[u][v] != 0.0) { // Existe aresta (u, v)
                        double peso = this.matrizValores[u][v];
                        if (distancias[u] + peso < distancias[v]) {
                            distancias[v] = distancias[u] + peso;
                            predecessores[v] = u;
                        }
                    }
                }
            }
        }

        // Verificação de ciclos negativos
        for (int u = 0; u < this.numeroVertices; u++) {
            for (int v = 0; v < this.numeroVertices; v++) {
                if (this.matrizValores[u][v] != 0.0) {
                    double peso = this.matrizValores[u][v];
                    if (distancias[u] + peso < distancias[v]) {
                        System.out.println("O grafo contém um ciclo de peso negativo.");
                        return;
                    }
                }
            }
        }

        // Exibição dos resultados
        System.out.println("Resultados de Bellman-Ford para o vértice inicial " + verticeInicial + ":");
        for (int v = 0; v < this.numeroVertices; v++) {
            System.out.printf("Distância para o vértice %d: %.2f, Caminho: %s\n",
                    v + 1, distancias[v], reconstruirCaminho(predecessores, v));
        }
    }

    private String reconstruirCaminho(int[] predecessores, int vertice) {
        List<Integer> caminho = new ArrayList<>();
        for (int v = vertice; v != -1; v = predecessores[v]) {
            caminho.add(v + 1);
        }
        Collections.reverse(caminho);
        return caminho.toString();
    }

    //Determinar a árvore geradora mínima de um grafo, utilizei algoritmo de prim
   public void arvoreGeradoraMinimaPrim(String arquivoSaida) {
    double[][] mst = new double[this.numeroVertices][this.numeroVertices];
    boolean[] visitados = new boolean[this.numeroVertices];
    double[] menorCusto = new double[this.numeroVertices];
    int[] predecessores = new int[this.numeroVertices];

    Arrays.fill(menorCusto, Double.POSITIVE_INFINITY);
    menorCusto[0] = 0; // Começa pelo vértice 0
    predecessores[0] = -1;

    for (int i = 0; i < this.numeroVertices - 1; i++) {
        int u = extrairMinimo(visitados, menorCusto);
        visitados[u] = true;

        for (int v = 0; v < this.numeroVertices; v++) {
            if (this.matrizValores[u][v] != 0.0 && !visitados[v] && this.matrizValores[u][v] < menorCusto[v]) {
                predecessores[v] = u;
                menorCusto[v] = this.matrizValores[u][v];
            }
        }
    }

    // Construindo a MST e calculando o peso total
    double pesoTotal = 0;
    for (int i = 1; i < this.numeroVertices; i++) {
        int u = predecessores[i];
        if (u != -1) {
            mst[u][i] = this.matrizValores[u][i];
            mst[i][u] = this.matrizValores[i][u];
            pesoTotal += this.matrizValores[u][i];
        }
    }

    // Exibindo o peso total
    System.out.printf("Peso total da árvore geradora mínima: %.2f\n", pesoTotal);

    // Salvando a MST no arquivo
    try (PrintWriter writer = new PrintWriter(new File(arquivoSaida))) {
        for (int i = 0; i < this.numeroVertices; i++) {
            for (int j = 0; j < this.numeroVertices; j++) {
                writer.print(mst[i][j] + " ");
            }
            writer.println();
        }
        writer.printf("Peso total: %.2f\n", pesoTotal);
    } catch (IOException e) {
        System.out.println("Erro ao salvar a árvore geradora mínima: " + e.getMessage());
    }
}

private int extrairMinimo(boolean[] visitados, double[] menorCusto) {
    double min = Double.POSITIVE_INFINITY;
    int indiceMinimo = -1;

    for (int i = 0; i < this.numeroVertices; i++) {
        if (!visitados[i] && menorCusto[i] < min) {
            min = menorCusto[i];
            indiceMinimo = i;
        }
    }
    return indiceMinimo;
}

// IMplementação heuristica cobertura minima
public List<Integer> coberturaMinimaPorHeuristica() {
    // Lista para armazenar os vértices da cobertura
    List<Integer> cobertura = new ArrayList<>();
    
    // Array para rastrear se um vértice já está na cobertura
    boolean[] coberto = new boolean[numeroVertices];

    // Enquanto ainda houver arestas na matriz
    while (existeAresta()) {
        // Encontrar o vértice com o maior grau (maior número de arestas conectadas)
        int verticeEscolhido = -1;
        int maxGrau = -1;

        for (int i = 0; i < numeroVertices; i++) {
            if (!coberto[i]) { // Apenas vértices não cobertos
                int grau = calcularGrau(i);
                if (grau > maxGrau) {
                    maxGrau = grau;
                    verticeEscolhido = i;
                }
            }
        }

        // Adicionar o vértice escolhido à cobertura
        cobertura.add(verticeEscolhido + 1); // Adicionar 1 para representar os vértices como 1-based
        coberto[verticeEscolhido] = true;

        // Remover todas as arestas incidentes ao vértice escolhido
        removerArestasDoVertice(verticeEscolhido);
    }

    return cobertura;
}

// Método para verificar se ainda existem arestas na matriz
private boolean existeAresta() {
    for (int i = 0; i < numeroVertices; i++) {
        for (int j = 0; j < numeroVertices; j++) {
            if (matrizValores[i][j] != 0) { // Existe aresta
                return true;
            }
        }
    }
    return false;
}

// Método para calcular o grau de um vértice
private int calcularGrau(int vertice) {
    int grau = 0;
    for (int j = 0; j < numeroVertices; j++) {
        if (matrizValores[vertice][j] != 0) { // Contar arestas conectadas
            grau++;
        }
    }
    return grau;
}

// Método para remover todas as arestas incidentes a um vértice
private void removerArestasDoVertice(int vertice) {
    for (int j = 0; j < numeroVertices; j++) {
        matrizValores[vertice][j] = 0; // Remover aresta na linha
        matrizValores[j][vertice] = 0; // Remover aresta na coluna
    }
}

}

