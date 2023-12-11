import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static class Product {
        /*
         *  Cria um produto com tipo TYPE e valor VALUE
         */

        private final char type;
        private final int value;

        Product(char type, int value) {
            this.type = type;
            this.value = value;
        }

        /*
         *  retorna o tipo TYPE do produto
         */
        public char getType() {
            return type;
        }

        /*
         *  retorna o valor VALUE do produto
         */
        public int getValue() {
            return value;
        }
    }

    public static int[] lcsAdaptada(Product[] l1, Product[] l2, int n, int m) {

        /* Inicia duas matrizes, tendo como tamanho o número de produtos de cada conveyor +1.
         * A matriz val é orientada para os valores e a seq é onde se guarda as sequências.
         */
        int[][] val = new int[n + 1][m + 1];
        int[][] seq = new int[n + 1][m + 1];
        
        // Inicia um array res com tamanho 2 utilizado para dar return dos resultados obtidos no final.
        int[] res = new int[2];

        //  inicializar as extremidades das matrizes a zero
        for (int i = 0; i <= n; i++) {
            val[i][0] = 0;
            seq[i][0] = 0;
        }
        for (int j = 1; j <= m; j++) {
            val[0][j] = 0;
            seq[0][j] = 0;
        }
        /*  percorrer a matriz  */
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {

                // se ambos os produtos forem do mesmo tipo
                if (l1[i - 1].getType() == l2[j - 1].getType()) {

                    //  verifica se a soma do valor dos produtos com a diagonal é maior que
                    //  o máximo da casa da esquerda com a de cima
                    //  se for, essa soma fica na casa corrente da martriz e adiciona 1 à matriz dos pares.
                    if (l1[i - 1].getValue() + l2[j - 1].getValue() + val[i - 1][j - 1] > Math.max(val[i - 1][j], val[i][j - 1])) {
                        val[i][j] = l1[i - 1].getValue() + l2[j - 1].getValue() + val[i - 1][j - 1];
                        seq[i][j] = seq[i - 1][j - 1] + 1;

                        // se a soma for inferior, o valor colocado na casa corrente será o máximo
                        // do valor da esquerda com o de cima.
                    } else {
                        val[i][j] = Math.max(val[i - 1][j], val[i][j - 1]);

                        // se os valores da posição acima e da esquerda forem iguais
                        // coloca-se o valor mínimo da matriz dos pares dessas duas posições
                        if (val[i - 1][j] == val[i][j - 1]) {
                            seq[i][j] = Math.min(seq[i - 1][j], seq[i][j - 1]);

                            //  se o valor da posição acima for o maior, coloca-se o valor da matriz dos pares dessa posição
                        } else if (val[i][j] == val[i - 1][j]) {
                            seq[i][j] = seq[i - 1][j];

                            //  se o valor da posição anterior for o maior, coloca-se o valor da matriz dos pares dessa posição
                        } else {
                            seq[i][j] = seq[i][j - 1];
                        }

                    }

                    // se não forem do mesmo tipo, faz-se o mesmo que é feito nos 4 comentários anteriores
                    // isto é, não se verifica o valor obtido pela soma de ambos os produtos com o valor da sua diagonal
                } else {
                    val[i][j] = Math.max(val[i - 1][j], val[i][j - 1]);
                    if (val[i - 1][j] == val[i][j - 1]) {
                        seq[i][j] = Math.min(seq[i - 1][j], seq[i][j - 1]);
                    } else if (val[i][j] == val[i - 1][j]) {
                        seq[i][j] = seq[i - 1][j];
                    } else {
                        seq[i][j] = seq[i][j - 1];
                    }
                }
            }
        }

        // coloca-se o resultado no array para dar return
        res[0] = val[n][m];
        res[1] = seq[n][m];
        return res;
    }

    public static void main(String[] args) throws IOException {

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        // número de test cases
        int test_c = Integer.parseInt(input.readLine());

        // primeiro belt
        Product[] conveyor1 = new Product[1];

        // segundo belt   
        Product[] conveyor2 = new Product[1];

        // array para guardar os inputs e depois separá-los
        String[] parts;

        // utilizado como auxiliar para a leitura do input
        int count = 0;

        // número de produtos no primeiro belt
        int num_p1 = 0;

        // número de produtos no segundo belt
        int num_p2 = 0;

        // lista para guardar os valores que são retornados pelo método lcsAdaptada()
        List<Integer> output = new ArrayList<>();

        // loop para os test cases, enquanto houver test cases, continua a pedir valores
        for (int i = 0; i < test_c * 2; i++) {
            int num_p = Integer.parseInt(input.readLine());

            if (count % 2 == 0) {
                num_p1 = num_p;
                conveyor1 = new Product[num_p1];
            } else {
                num_p2 = num_p;
                conveyor2 = new Product[num_p2];
            }

            for (int j = 0; j < num_p; j++) {
                parts = input.readLine().split(" ");
                if (count % 2 == 0) {
                    conveyor1[j] = new Product(parts[1].charAt(0), Integer.parseInt(parts[2]));
                } else {
                    conveyor2[j] = new Product(parts[1].charAt(0), Integer.parseInt(parts[2]));
                }
            }

            count++;

            if (count % 2 == 0) {
                output.add(lcsAdaptada(conveyor1, conveyor2, num_p1, num_p2)[0]);
                output.add(lcsAdaptada(conveyor1, conveyor2, num_p1, num_p2)[1]);
            }
        }

        // apresentação dos resultados no formato pedido.
        for (int k = 0; k < test_c * 2; k = k + 2) {
            System.out.println(output.get(k) + " " + output.get(k + 1));
        }

    }
}
