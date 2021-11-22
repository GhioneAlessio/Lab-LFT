import java.util.Scanner;

public class Es1_7 {
    public static boolean scan(String s) {
        int state = 0;
        for (int i = 0; state != -1 && i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (state) {
            case 0:
                if (ch == 'A') {
                    state = 1;
                    break;
                } else {
                    state = 8;
                    break;
                }
            case 1:
                if (ch == 'l') {
                    state = 2;
                    break;
                } else {
                    state = 9;
                    break;
                }
            case 2:
                if (ch == 'e') {
                    state = 3;
                    break;
                } else {
                    state = 10;
                    break;
                }
            case 3:
                if (ch == 's') {
                    state = 4;
                    break;
                } else {
                    state = 11;
                    break;
                }
            case 4:
                if (ch == 's') {
                    state = 5;
                    break;
                } else {
                    state = 12;
                    break;
                }
            case 5:
                if (ch == 'i') {
                    state = 6;
                    break;
                } else {
                    state = 13;
                    break;
                }
            case 6:
                state = 7;
                break;
            case 7:
                state = -1;
                break;
            case 8:
                if (ch == 'l') {
                    state = 9;
                    break;
                } else {
                    state = -1;
                    break;
                }
            case 9:
                if (ch == 'e') {
                    state = 10;
                    break;
                } else {
                    state = -1;
                    break;
                }
            case 10:
                if (ch == 's') {
                    state = 11;
                    break;
                } else {
                    state = -1;
                    break;
                }
            case 11:
                if (ch == 's') {
                    state = 12;
                    break;
                } else {
                    state = -1;
                    break;
                }
            case 12:
                if (ch == 'i') {
                    state = 13;
                    break;
                } else {
                    state = -1;
                    break;
                }
            case 13:
                if (ch == 'o') {
                    state = 7;
                    break;
                } else {
                    state = -1;
                    break;
                }
            }  
        }
        return state == 7;
    }

    public static void main(String[] args) {
        String st;
        Scanner scanner = new Scanner(System.in);
        st = scanner.nextLine();
        scanner.close();
        System.out.println(scan(st) ? "OK" : "NOPE");

    }
}
