import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class LeitorArquivo {

    private String caminhoArquivo;
    private double[][] matrizValores;
    private int numeroLinhas;

    // Construtor
    public LeitorArquivo(String enderecoArquivo) {
        this.caminhoArquivo = enderecoArquivo;
    }

    // Lê o arquivo e inicializa os dados
    public void lerArquivo() {
        try (BufferedReader leitorArquivo = new BufferedReader(new FileReader(caminhoArquivo))) {
            this.numeroLinhas = Integer.parseInt(leitorArquivo.readLine().trim());
            this.matrizValores = inicializarMatrizVazia(this.numeroLinhas);

            adicionarValoresNaMatriz(leitorArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro ao converter valores numéricos: " + e.getMessage());
        }
    }

    // Insere 0.0 em todas as posições da matriz
    private double[][] inicializarMatrizVazia(int tamanho) {
        double[][] matriz = new double[tamanho][tamanho];
        for (double[] linha : matriz) {
            Arrays.fill(linha, 0.0);
        }
        return matriz;
    }

    // Insere os valores lidos do arquivo na matriz
    private void adicionarValoresNaMatriz(BufferedReader leitorArquivo) {
        String linha;
        try {
            while ((linha = leitorArquivo.readLine()) != null) {
                String[] partes = linha.split(" ");
                int vertice1 = Integer.parseInt(partes[0]) - 1; // Converte para índice 0-based
                int vertice2 = Integer.parseInt(partes[1]) - 1;
                double peso = Double.parseDouble(partes[2]);

                if (vertice1 >= 0 && vertice1 < this.numeroLinhas && vertice2 >= 0 && vertice2 < this.numeroLinhas) {
                    this.matrizValores[vertice1][vertice2] = peso;
                    this.matrizValores[vertice2][vertice1] = peso; // Se for grafo não-direcionado
                } else {
                    System.out.println("Índices fora dos limites: " + vertice1 + ", " + vertice2);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler a linha do arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro ao converter valores da linha: " + e.getMessage());
        }
    }

    


    public int getNumeroLinhas(){
        return this.numeroLinhas;
    }

    public double[][] getMatrizValores(){
        return this.matrizValores;
    }



    // Método main para teste
    public static void main(String[] args) {
        LeitorArquivo leitor = new LeitorArquivo("./dados/grafo.txt");
        leitor.lerArquivo();
    }
}
