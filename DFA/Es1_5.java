import java.util.Scanner;

public class Es1_5{

    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state != -1 && i < s.length()) {
            char ch = s.charAt(i);
            if(Character.isLetter(ch))
                ch = Character.toLowerCase(ch);
            switch (state) {
            case 0:
                if (Character.isLetter(ch)) {
                    if (65<= ch && ch <= 75)
                        state = 1;
                    else if (ch>=76 && ch<=90)
                        state = 2;
                    else state = -1;
                        break;
                } else
                    state = -1;
                break;
            case 1: // corso A
                if (Character.isDigit(ch)) {
                    if (ch % 2 == 0)
                        state = 3;
                    else
                        state = 4;
                    break;
                } else if (Character.isLetter(ch)) {
                    state = 1;
                    break;
                }
                state = -1;
                break;
            case 2: // corso B
                if (Character.isDigit(ch)) {
                    if (ch % 2 == 0)
                        state = 5;
                    else
                        state = 6;
                    break;
                } else if (Character.isLetter(ch)) {
                    state = 2;
                    break;
                } else
                    state = -1;
                break;
            case 3: //anp
                if (Character.isDigit(ch)) {
                    if (ch % 2 == 0)
                        state = 3;
                    else
                        state = 4;
                    break;
                } else
                    state = -1;
            case 4://and
                if (Character.isDigit(ch)) {
                    if (ch % 2 == 0)
                        state = 3;
                    else
                        state = 4;
                    break;
                } else
                    state = -1;
            case 5://bnp
                if (Character.isDigit(ch)) {
                    if (ch % 2 == 0)
                        state = 5;
                    else
                        state = 6;
                    break;
                } else
                    state = -1;
                break;
            case 6:
                if (Character.isDigit(ch)) {
                    if (ch % 2 == 0)
                        state = 5;
                    else
                        state = 6;
                    break;
                } else
                    state = -1;
                break;
            }

            i += 1;
        }
        return state == 3 || state == 6;
    }

    public static void main(String[] args) {
        String st;
        Scanner scanner = new Scanner(System.in);
        st = scanner.nextLine();
        scanner.close();
        System.out.println(scan(st) ? "OK" : "NOPE");
    }
}
