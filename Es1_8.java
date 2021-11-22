
import java.util.Scanner;

public class Es1_8 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserisci numero: ");
        String sr = scanner.nextLine();
        scanner.close();
        int state = 0;
        for (int i = 0; state != -1 && i < sr.length(); i++) {
            char ch = sr.charAt(i);
            switch (state) {
            case 0:
                if (Character.isDigit(ch)) {
                    state = 1;
                    break;
                } else if (sr.charAt(i) == '.') {
                    state = 2;
                } else if (sr.charAt(i) == '+' || sr.charAt(i) == '-') {
                    state = 3;
                    break;
                } else
                    state = -1;
                    break;
            case 1:
                if (Character.isDigit(ch))
                    break;
                else if (ch == '.') {
                    state = 2;
                    break;
                } else if (ch == 'e') {
                    state = 4;
                    break;
                } else
                    state = -1;
                break;
            case 2:
                if (Character.isDigit(ch))
                    state = 7;
                else
                    state = -1;
                break;
            case 3:
                if (Character.isDigit(ch)) {
                    state = 1;
                    break;
                } else if (ch == '.') {
                    state = 2;
                    break;
                } else
                    state = -1;
                break;
            case 4:
                if (ch == '+' || ch == '-') {
                    state = 5;
                    break;
                } else if (Character.isDigit(ch)) {
                    state = 6;
                    break;
                }else if(ch == '.'){
                    state = 8;
                    break;
                }
                else state = -1;
                break;
            case 5:
                if(Character.isDigit(ch)){
                    state = 6;
                    break;
                }else 
                state =-1; break;

            case 6: 
                if(Character.isDigit(ch))
                break;
                else if(ch == '.'){
                state = 8;
                break;
                }
                else state = -1;
                break;
            case 7: 
                if(Character.isDigit(ch))
                    break;
                else if(ch == 'e'){
                    state = 4;
                    break;}
                else 
                state = -1;
                break;
            case 8: 
                if(Character.isDigit(ch)){
                    state = 9;
                    break;
                }
                else 
                    state = -1;
                    break;
            case 9:
                if(Character.isDigit(ch))
                    break;
                else 
                    state = -1;
                    break;
            }
            
        }

        System.out.printf("Stringa accetata: %s", (state == 1 || state == 6 || state == 7 || state == 9) ? "Si\n" : "No\n");
    }
}
