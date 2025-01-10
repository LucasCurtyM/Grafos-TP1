import java.util.Locale;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    
    public static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] Args) {
        Locale.setDefault(Locale.US);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o caminho do arquivo de entrada: ");
        String caminhoArquivo = scanner.nextLine();

        System.out.println("\n\n\n");
        Grafo grafo = new Grafo(caminhoArquivo);
        int escolha;

        do {
            System.out.println("Digite a sua escolha!");
            System.out.println("1- Mostrar matriz de Valores: ");
            System.out.println("2- Ordem do Grafo: ");
            System.out.println("3- Tamanho do Grafo: ");
            System.out.println("4- Densidade ε(G) do grafo: ");
            System.out.println("5- Vizinhos do vértice V: ");
            System.out.println("6- Grau do vértice V: ");
            System.out.println("7- Verifica se o vértice V é articulação: ");
            System.out.println("8- Verifica arestas fora da árvore de busca em largura e ordem seguida a partir do vértice V: ");
            System.out.println("9- Verifica as componentes conexas partir do vértice V: ");
            System.out.println("10- Verifica se há ciclos no grafo: ");
            System.out.println("11- Distância e Caminho mínimo: ");
            System.out.println("12- Arvore Geradora Minima (Arquivo): ");
            System.out.println("13- Cobertura Minima de vértices: ");
            System.out.println("14- Emparelhamento máximo: ");
            System.out.println("15- Centralidade de proximidade do vértice V: ");
            System.out.println("0- Sair");

            escolha = scanner.nextInt();
            System.out.println(escolha);

            switch (escolha) {

                case 1:
                    limparTela();

                    System.out.printf("Matriz de Valores:\n");
                    grafo.mostrarMatriz();

                    System.out.printf("\n\n\n");
                    break;

                case 2:
                    limparTela();

                    System.out.printf("\n\n\nOrdem do Grafo: %d", grafo.getOrdem());

                    System.out.println("\n\n\n");
                    break;
                
                case 3: 
                    limparTela();

                    System.out.printf("\n\n\nTamanho do Grafo: %d", grafo.getTamanho());  

                    System.out.println("\n\n\n");
                    break;
                
                case 4:
                    limparTela();

                    System.out.printf("\n\n\nDensidade do Grafo: %.2f", grafo.densidade());

                    System.out.println("\n\n\n");
                    break;
          
                  case 5:
                    limparTela();

                    System.out.println("Escolha o Vértice V: ");

                    int escolhaVertice = scanner.nextInt();
                    if(escolhaVertice > grafo.getOrdem() || escolhaVertice < 1){
                        
                        System.out.printf("\n\n\nValor inválido para Vértice, V(%d) não existe!", escolhaVertice);
                        break;
                    }
                    List<Integer> vizinho = grafo.vizinhos(escolhaVertice);
                    System.out.printf("\n\n\nVizinhos de V(%d): %s", escolhaVertice, vizinho);

                    System.out.println("\n\n\n");
                    break;
        
                case 6:
                    limparTela();

                    System.out.println("Escolha o Vértice V: ");
                    escolhaVertice = scanner.nextInt();

                    if(escolhaVertice > grafo.getOrdem() || escolhaVertice < 1){
                        
                        System.out.printf("\n\n\nValor inválido para Vértice, V(%d) não existe no grafo!", escolhaVertice);

                        System.out.println("\n\n\n");
                        break;
                    }

                    System.out.printf("\n\n\nGrau de V(%d): %d", escolhaVertice, grafo.grauVertice(escolhaVertice));

                    System.out.println("\n\n\n");
                    break;
    
                case 7:
                    limparTela();

                    System.out.println("\n\n\nEscolha o Vértice V: ");
                    escolhaVertice = scanner.nextInt();

                    if(escolhaVertice > grafo.getOrdem() || escolhaVertice < 1){
                        
                        System.out.printf("\n\n\nValor inválido para Vértice, V(%d) não existe no grafo!", escolhaVertice);

                        System.out.println("\n\n\n");
                        break;
                    }

                   if(grafo.verificaArticulacao(escolhaVertice)){
                       System.out.printf("\n\n\nO vértice "+ escolhaVertice +" é articulação!");
                   }else {
                       System.out.printf("\n\n\nO vértice "+ escolhaVertice +" não é articulação!");
                   }

                    System.out.println("\n\n\n");
                    break;

                case 8:
                    limparTela();

                    System.out.println("Escolha o Vértice V: ");
                    escolhaVertice = scanner.nextInt();

                    if(escolhaVertice > grafo.getOrdem() || escolhaVertice < 1){
                        
                        System.out.printf("\n\n\nValor inválido para Vértice, V(%d) não existe no grafo!", escolhaVertice);
                        
                        System.out.println("\n\n\n");
                        break;
                    }

                    List<String> foraDaArvore = grafo.arestasForaArvoreLargura(escolhaVertice);
                    List<Integer> verticesVisitados = grafo.ordemVerticesVisitados(escolhaVertice);

                    System.out.println("\n\n\nA ordem dos vértices visitados foi");
                    for (int i = 0; i<verticesVisitados.size(); i++) {
                        System.out.print(verticesVisitados.get(i));
                        if (i<verticesVisitados.size()-1){
                            System.out.print(" -> ");
                        }
                    }
                    System.out.println("\n");

                    System.out.println("\n\n\nArestas não visitadas");
                    for (String aresta : foraDaArvore) {
                        System.out.println(aresta);
                    }

                    System.out.println("\n\n\n");
                    break;
                
                case 9:
                    limparTela();

                    List<List<Integer>> componentes = grafo.componentesConexas();

                    System.out.println("Número de componentes conexas: " + componentes.size());
                    for (int i = 0; i < componentes.size(); i++) {
                        System.out.println("Componente " + (i + 1) + ": " + componentes.get(i));
                    }

                    System.out.println("\n\n\n");
                    break;
            
                case 10:
                    limparTela();

                    if(grafo.verificaCiclos()){
                        System.out.println("O grafo possui ciclos!\n\n\n");
                    }else{
                        System.out.println("O grafo não possui ciclos!\n\n\n");
                    }

                    System.out.println("\n\n\n");
                    break;

                case 11:
                    limparTela();
                    System.out.println("Escolha o Vértice V: ");
                    int verticeInicial = scanner.nextInt();

                    if(verticeInicial > grafo.getOrdem() || verticeInicial < 1){
                        System.out.printf("Valor inválido para Vértice, V(%d) não existe!\n\n\n", verticeInicial);
                        System.out.println("\n\n\n");
                        break;
                    }

                    double[] distancias = new double[grafo.getOrdem()];
                    distancias = grafo.caminhoMinimo(verticeInicial);

                    System.out.printf("Caminho mínimo do vértice %d:\n", verticeInicial);
                    for (int i = 0; i < distancias.length; i++) {
                        System.out.printf("Para o vértice %d: %.2f\n", i + 1, distancias[i]);
                    }

                    System.out.println("\n\n\n");
                    break;

                case 12:
                    limparTela();
                    
                    grafo.arvoreGeradoraMinimaPrim();

                    System.out.println("\n\n\n");
                    break;

                case 13:
                    limparTela();

                    List<Integer> cobertura = grafo.coberturaMinimaVertices();

                    System.out.print("Cobertura Minima de vértices:" + cobertura);

                    System.out.println("\n\n\n");
                    break;

                case 14:
                    limparTela();
                    
                    List<int[]> emparelhamento = grafo.emparelhamentoMaximoEdmonds();

                    System.out.println("Emparelhamento máximo:");
                    for (int[] aresta : emparelhamento) {
                        System.out.println("(" + aresta[0] + ", " + aresta[1] + ")");
                    }

                    System.out.println("\n\n\n");
                    break;
                
                case 15:
                    limparTela();
                    System.out.println("Escolha o Vértice V: ");
                    int vertice = scanner.nextInt();
                    
                    if(vertice > grafo.getOrdem() || vertice < 1){
                        System.out.printf("Valor inválido para Vértice, V(%d) não existe!\n\n\n", vertice);
                        System.out.println("\n\n\n");
                        break;
                    }

                    double centralidade = grafo.calcularCentralidadeDeProximidade(vertice);
                    System.out.printf("Centralidade de proximidade do vértice %d é: ", vertice);
                    System.out.printf("%.4f", centralidade);
    
                    System.out.println("\n\n\n");
                    break;
                
                    case 0:
                    limparTela();

                    System.out.println("\n\n\nEncerrando...\n\n\n");
                    
                    break;
                    
                default:
                    
                    System.out.println("Opção inválida!\n\n\n");
                    break;
            }

        } while (escolha != 0);
        scanner.close();
    }
}
