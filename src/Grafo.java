import java.io.FileWriter;
import java.io.IOException;
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

    public double[] caminhoMinimo(int verticeInicial) {
        double[] distancias = new double[this.numeroVertices];
    
        // Inicialização: distância infinita e nenhum predecessor
        Arrays.fill(distancias, Double.POSITIVE_INFINITY);
    
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
                        throw new IllegalArgumentException("O grafo contém um ciclo de peso negativo.");
                    }
                }
            }
        }
        return distancias; // Retorna as distâncias mínimas
    }

    private String reconstruirCaminho(int[] predecessores, int vertice) {
        List<Integer> caminho = new ArrayList<>();
        for (int v = vertice; v != -1; v = predecessores[v]) {
            caminho.add(v + 1);
        }
        Collections.reverse(caminho);
        return caminho.toString();
    }

    public void arvoreGeradoraMinimaPrim(){
        boolean[] visitados = new boolean[this.numeroVertices];
        double[] chave = new double[this.numeroVertices];
        int[] pai = new int[this.numeroVertices];
        
        Arrays.fill(chave, Double.POSITIVE_INFINITY);
        Arrays.fill(pai, -1);

        chave[0] = 0;

        for (int count = 0; count < this.numeroVertices; count++) {
            int u = menorChave(chave, visitados);
            visitados[u] = true;
    
            for (int v = 0; v < this.numeroVertices; v++) {
                if (this.matrizValores[u][v] != 0 && !visitados[v] && this.matrizValores[u][v] < chave[v]) {
                    chave[v] = this.matrizValores[u][v];
                    pai[v] = u;
                }
            }
        }
    
        imprimirAGM(pai);
    }

    private int menorChave(double[] chave, boolean[] visitados) {
        double min = Double.POSITIVE_INFINITY;
        int index = -1;
    
        for (int v = 0; v < this.numeroVertices; v++) {
            if (!visitados[v] && chave[v] < min) {
                min = chave[v];
                index = v;
            }
        }
        return index;
    }

    private void imprimirAGM(int[] pai) {
        double pesoTotal = 0;
        StringBuilder resultado = new StringBuilder("Árvore geradora mínima:\n");
    
        // Construir e calcular arestas diretamente
        for (int i = 1; i < this.numeroVertices; i++) {
            int u = pai[i];
            int v = i;
            double peso = this.matrizValores[v][u];
            resultado.append(String.format("(%d, %d) %.2f\n", u + 1, v + 1, peso));
            pesoTotal += peso;
        }
    
        resultado.append(String.format("Peso total: %.2f\n", pesoTotal));
    
        // Salvar no arquivo
        try (FileWriter writer = new FileWriter("./dados/saida/arvoreGeradoraMinima.txt")) {
            writer.write(resultado.toString());
            System.out.println("Árvore Geradora Mínima salva no arquivo 'arvoreGeradoraMinima.txt' em dados/saida.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar a Árvore Geradora Mínima no arquivo: " + e.getMessage());
        }

    }


    
    //Heurística do grau máximo

    public List<Integer> coberturaMinimaVertices() {
        // Lista para armazenar os vértices da cobertura mínima
        List<Integer> cobertura = new ArrayList<>();
        
        // Criação de uma matriz de adjacência temporária para manipulação
        double[][] matrizTemp = new double[this.numeroVertices][this.numeroVertices];
        for (int i = 0; i < this.numeroVertices; i++) {
            matrizTemp[i] = this.matrizValores[i].clone();
        }
        
        // Enquanto existirem arestas no grafo
        while (existemArestas(matrizTemp)){
            // Identificar o vértice de maior grau
            int verticeMaiorGrau = -1;
            int maiorGrau = -1;
            
            for (int i = 0; i < this.numeroVertices; i++) {
                int grau = calcularGrau(matrizTemp, i);
                if (grau > maiorGrau) {
                    maiorGrau = grau;
                    verticeMaiorGrau = i;
                }
            }
            
            // Adicionar o vértice de maior grau à cobertura
            cobertura.add(verticeMaiorGrau + 1);
            
            // Remover as arestas incidentes ao vértice escolhido
            for (int j = 0; j < this.numeroVertices; j++) {
                matrizTemp[verticeMaiorGrau][j] = 0;
                matrizTemp[j][verticeMaiorGrau] = 0;
            }
        }
        
        return cobertura;
    }
    
    private boolean existemArestas(double[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] != 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private int calcularGrau(double[][] matriz, int vertice) {
        int grau = 0;
        for (int j = 0; j < matriz[vertice].length; j++) {
            if (matriz[vertice][j] != 0) {
                grau++;
            }
        }
        return grau;
    }
    



    public List<int[]> emparelhamentoMaximoEdmonds() {
        // Cria um emparelhamento vazio
        int[] emparelhamento = new int[this.numeroVertices];
        Arrays.fill(emparelhamento, -1); // -1 significa que o vértice está livre

        // Armazena as arestas do grafo
        List<Aresta> arestas = new ArrayList<>();
        for (int i = 0; i < this.numeroVertices; i++) {
            for (int j = i + 1; j < this.numeroVertices; j++) { 
                if (this.matrizValores[i][j] != 0) {
                    arestas.add(new Aresta(i, j, this.matrizValores[i][j]));
                }
            }
        }

        // Ordena as arestas por peso (em ordem decrescente)
        arestas.sort((a, b) -> Double.compare(b.peso, a.peso));

        // Itera por todas as arestas e tenta formar o emparelhamento máximo
        for (Aresta aresta : arestas) {
            int u = aresta.u;
            int v = aresta.v;

            // Se ambos os vértices estão livres, ou um está emparelhado com o outro,
            // adiciona o emparelhamento
            if (emparelhamento[u] == -1 && emparelhamento[v] == -1) {
                emparelhamento[u] = v;
                emparelhamento[v] = u;
            }
        }

        // Converte o emparelhamento para o formato desejado (pares de vértices emparelhados)
        List<int[]> resultado = new ArrayList<>();
        for (int i = 0; i < emparelhamento.length; i++) {
            if (emparelhamento[i] > i) { // Garantir que cada emparelhamento é adicionado uma vez
                resultado.add(new int[]{i + 1, emparelhamento[i] + 1});
            }
        }

        return resultado;
    }

    // Classe auxiliar para armazenar as arestas com seus pesos
    private class Aresta {
        int u, v;
        double peso;

        Aresta(int u, int v, double peso) {
            this.u = u;
            this.v = v;
            this.peso = peso;
        }
    }






    public double calcularCentralidadeDeProximidade(int vertice) {
        double[] distancias = caminhoMinimo(vertice); // Usando o método ajustado
        double somaDistancias = 0;
    
        for (double distancia : distancias) {
            if (distancia != Double.POSITIVE_INFINITY) { // Ignorar vértices não alcançáveis
                somaDistancias += distancia;
            }
        }
    
        return (this.numeroVertices - 1) / somaDistancias; // Fórmula da centralidade
    }





    
    public static void main(String[] Args) {
        Locale.setDefault(Locale.US);
        Grafo grafo = new Grafo("./dados/entrada/grafo.txt");
        grafo.mostrarMatriz();
        grafo.arvoreGeradoraMinimaPrim();
        
        // Executar a heurística de cobertura mínima de vértices
        List<Integer> cobertura = grafo.coberturaMinimaVertices();
        System.out.println("Cobertura mínima de vértices: " + cobertura);
        
        // Exibir o emparelhamento máximo
        List<int[]> emparelhamento = grafo.emparelhamentoMaximoEdmonds();
        System.out.println("Emparelhamento máximo:");
        for (int[] aresta : emparelhamento) {
            System.out.println("(" + aresta[0] + ", " + aresta[1] + ")");
        }

        //centralidade
        double centralidade = grafo.calcularCentralidadeDeProximidade(2);
        System.out.println("Centralidade de proximidade do vértice 2: "+ + centralidade);
    


        //Caminho minimo
        // int verticeInicial = 1;
        // double[] distancias = new double[grafo.numeroVertices];
        // distancias = grafo.caminhoMinimo(verticeInicial);
        // System.out.printf("Caminho mínimo do vértice %d:\n", verticeInicial);
        // for (int i = 0; i < distancias.length; i++) {
        //     System.out.printf("Para o vértice %d: %.2f\n", i + 1, distancias[i]);
        // }

    }

}