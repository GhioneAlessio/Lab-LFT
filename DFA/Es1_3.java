import java.util.Scanner;

public class Es1_3 {

    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            char ch = s.charAt(i);
            switch (state) {
            case 0:
                if (Character.isDigit(ch))
                    state = 1;
                else 
                    state = -1;
                break;
            case 1:
                if (Character.isDigit(ch))
                    state = 1;
                else if (Character.isLetter(ch)){
                     Character.toLowerCase(ch);
                    if(97 <= ch && ch <= 107)
                        state = 2; 
                    else 
                        state = 3;
                }
                else
                    state = -1;
                break;
            case 2:
                if (Character.isLetter(ch))
                    state = 2;
                else 
                    state = -1;
                break;
            case 3:
                if (Character.isLetter(ch))
                    state = 3;
                else
                    state = -1;
                break;
            }
            i+=1;
        }
        return state == 2 || state == 3;
    }

    public static void main(String[] args) {
        String st;
        Scanner scanner = new Scanner(System.in);
        st = scanner.nextLine();
        scanner.close();            
        System.out.println(scan(st) ? "OK" : "NOPE");
    }

}
