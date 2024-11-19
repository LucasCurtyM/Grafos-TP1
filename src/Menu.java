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
        System.out.println(caminhoArquivo);

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
            System.out.println("0- Sair");

            escolha = scanner.nextInt();
            System.out.println(escolha);
            switch (escolha) {
                case 1:
                    limparTela();    
                    grafo.mostrarMatriz();
                    System.out.printf("\n\n\n");
                    break;
                case 2:
                    limparTela();
                    System.out.printf("Ordem do Grafo: %d\n\n\n", grafo.getOrdem());
                    break;
                case 3: 
                    limparTela();   
                    System.out.printf("Tamanho do Grafo: %d\n\n\n", grafo.getTamanho());
                    break;
                case 4:
                    limparTela();
                    System.out.printf("Densidade do Grafo: %.2f\n\n\n", grafo.densidade());
                    break;
                case 5:
                    limparTela();
                    System.out.println("Escolha o Vértice V: ");
                    int escolhaVertice = scanner.nextInt();
                    if(escolhaVertice > grafo.getOrdem() || escolhaVertice < 1){
                        limparTela();
                        System.out.printf("Valor inválido para Vértice, V(%d) não existe!\n\n\n", escolhaVertice);
                        break;
                    }
                    List<Integer> vizinho = grafo.vizinhos(escolhaVertice);
                    System.out.printf("Vizinhos de V(%d): %s\n\n\n", escolhaVertice, vizinho);
                    break;
                case 6:
                    limparTela();
                    System.out.println("Escolha o Vértice V: ");
                    escolhaVertice = scanner.nextInt();
                    if(escolhaVertice > grafo.getOrdem() || escolhaVertice < 1){
                        limparTela();
                        System.out.printf("Valor inválido para Vértice, V(%d) não existe no grafo!\n\n\n", escolhaVertice);
                        break;
                    }
                    System.out.printf("Grau de V(%d): %d\n\n\n", escolhaVertice, grafo.grauVertice(escolhaVertice));
                    break;
                case 0:
                    limparTela();
                    System.out.println("Encerrando...\n\n\n");
                    
                    break;
                default:
                    limparTela();
                    System.out.println("Opção inválida!\n\n\n");
                    break;
            }
        } while (escolha != 0);
        scanner.close();
    }
}
