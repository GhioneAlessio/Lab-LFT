import java.util.Scanner;

public class Es1_4 {

    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            char ch = s.charAt(i);
            switch (state) {
            case 0:
                if (ch == ' ')
                    break;
                else if (Character.isDigit(ch)) {
                    if (ch % 2 == 0) {
                        state = 1;
                        break;
                    } else {
                        state = 2;
                        break;
                    }
                } else
                    state = -1;
                break;
            case 1:// n pari
                if (Character.isLetter(ch)) {
                    if (Character.isUpperCase(ch)) {
                        if (65 <= ch && ch <= 75) {
                            state = 5;
                            break;
                        } else {
                            state = -1;
                            break;
                        }
                    } else {
                        state = -1;
                        break;
                    }
                } else if (Character.isDigit(ch)) {
                    if (ch % 2 == 0) {
                        break;
                    } else {
                        state = 2;
                        break;
                    }

                } else if (ch == ' ') {
                    state = 3;
                    break;
                } else
                    state = -1;
                break;
            case 2:// n dispari
                if (Character.isLetter(ch)) {
                    if (Character.isUpperCase(ch)) {
                        if (65 <= ch && ch <= 75) {
                            state = -1;
                            break;
                        } else {
                            state = 6;
                            break;
                        }
                    } else {
                        state = -1;
                        break;
                    }
                } else if (Character.isDigit(ch)) {
                    if (ch % 2 == 0) {
                        state = 1;
                        break;
                    } else {
                        break;
                    }

                } else if (ch == ' ') {
                    state = 4;
                    break;
                } else
                    state = -1;
                break;
            case 3:// loop spazio
                if (ch == ' ') {
                    break;
                } else if (Character.isLetter(ch)) {
                    if (Character.isUpperCase(ch)) {
                        if (65 <= ch && ch <= 75) {
                            state = 5;
                            break;
                        } else {
                            state = -1;
                            break;
                        }
                    } else {
                        state = -1;
                        break;
                    }
                } else
                    state = -1;
                break;
            case 4:// loop spazio
                if (ch == ' ') {
                    break;
                } else if (Character.isLetter(ch)) {
                    if (Character.isUpperCase(ch)) {
                        if (65 <= ch && ch <= 75) {
                            state = -1;
                            break;
                        } else {
                            state = 6;
                            break;
                        }
                    } else {
                        state = -1;
                        break;
                    }
                } else
                    state = -1;
                break;
            case 5:// corso A
                if (Character.isLetter(ch)) {
                    break;
                } else if (ch == ' ') {
                    state = 7;
                    break;
                }
            case 6:// corso B
                if (Character.isLetter(ch)) {
                    break;
                } else if (ch == ' ') {
                    state = 7;
                    break;
                }
            case 7:
                if (ch == ' ') {
                    break;
                } else if (Character.isLetter(ch)) {
                    if (Character.isUpperCase(ch)) {
                        state = 8;
                        break;
                    } else {
                        state = -1;
                        break;
                    }
                } else
                    state = -1;
                break;
            case 8:
                if (Character.isLetter(ch)) {
                    break;
                } else if (ch == ' ') {
                    state = 7;
                    break;
                } else
                    state = -1;
                break;
            }
            i += 1;
        }
        return state == 5 || state == 6 || state == 7 || state == 8;
    }

    public static void main(String[] args) {
        String st;
        Scanner scanner = new Scanner(System.in);
        st = scanner.nextLine();
        scanner.close();
        System.out.println(scan(st) ? "OK" : "NOPE");
    }

}
