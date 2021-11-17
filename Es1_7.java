import java.util.Scanner;

public class Es1_7 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserisci il nome: ");
        String nome = scanner.nextLine();
        System.out.println("Inserisci la stringa da controllare: ");
        String sr = scanner.nextLine();
        scanner.close();
        int state = 0;
        for(int i = 0; state != -1 && i < sr.length(); i++){
            switch(state){ 
                case 0:
                    if(sr.charAt(i) != nome.charAt(i))
                        state = 1; 
                    break;
                case 1: 
                    if(sr.charAt(i) == nome.charAt(i))
                        state = 2;
                    else 
                        state = -1;
                    break;
                case 2: 
                    if(sr.charAt(i) != nome.charAt(i))
                        state = -1;
            }
        }

        System.out.printf("Stringa accetata: %s", (state == 0 || state == 2)? "Si" : "No");
        
    }
}
