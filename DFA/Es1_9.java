//progetto ghione && frattarola
import java.util.Scanner;

public class Es1_9 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserisci la stringa: ");
        String sr = scanner.nextLine();
        scanner.close();
        int state = 0;
        for (int i = 0; state != -1 && i < sr.length(); i++) {
            char ch = sr.charAt(i);
            switch (state) {

            case 0:
                if (ch == '/'){
                    state = 1;
                    break;
                }  
                else
                    state = -1;
                break;
            case 1:
                if (ch == '*'){
                    state = 2;
                    break;
                }
                else
                    state = -1;
                break;
            case 2:
                if (ch == '*') {
                    state = 3;
                    break;
                } else if (ch == '/' || ch == 'a')
                    break;
                else
                    state = -1;
                break;
            case 3:
                if (ch == '/') {
                    state = 4;
                    break;
                } else if (ch == 'a') {
                    state = 2;
                    break;
                } else if(ch == '*')
                    break;
                else
                    state = -1;
                break;
            case 4:
                    state = -1;
            }
        }
        System.out.printf("Stringa accetata: %s", (state == 4) ? "Si\n" : "No\n");

    }
}