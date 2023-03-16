import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        String line = in.readLine();
        int children = Integer.parseInt(line);
        int max = Integer.MIN_VALUE;
        int sticks = 0;

        for(int i = 0; i <children; i++){
            String[]parts = in.readLine().split(" ");
            sticks = Integer.parseInt(parts[0]);
            for(int j = 1; j<=sticks;j++){
                if (Integer.parseInt(parts[j]) > max) max = Integer.parseInt(parts[j]);
            }
        }

        in.close();
        System.out.println(max);
    }
}
