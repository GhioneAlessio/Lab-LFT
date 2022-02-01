import java.util.Scanner;

public class Es1_6 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state != -1 && i < s.length()) {
            char ch = s.charAt(i);
            if (Character.isLetter(ch))
                ch = Character.toLowerCase(ch);
            switch (state) {
                case 0:
                    if (Character.isDigit(ch)) {
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
                case 1:
                    if (Character.isDigit(ch)) {
                        if (ch % 2 == 0) {
                            state = 3;
                            break;
                        } else {
                            state = 4;
                            break;
                        }
                    } else
                        state = -1;
                    break;
                case 2:
                    if (Character.isDigit(ch)) {
                        if (ch % 2 == 0) {
                            state = 5;
                            break;
                        } else {
                            state = 6;
                            break;
                        }
                    } else
                        state = -1;
                    break;

                case 3:
                    if (Character.isDigit(ch)) {
                        if (ch % 2 != 0) {
                            state = 4;
                            break;
                        } else
                            break;
                    } else if (Character.isLetter(ch)) {
                        if (97 <= ch && ch <= 107) {
                            state = 7;
                        } else
                            state = -1;
                        break;
                    }
                case 4:
                    if (Character.isDigit(ch)) {
                        if (ch % 2 != 0) {
                            state = 6;
                            break;
                        } else {
                            state = 5;
                            break;
                        }

                    } else if (Character.isLetter(ch)) {
                        if (97 <= ch && ch <= 107) {
                            state = 7;
                        } else {
                            state = -1;
                            break;
                        }
                    } else
                        state = -1;
                    break;

                case 5:
                    if (Character.isDigit(ch)) {
                        if (ch % 2 != 0) {
                            state = 4;
                            break;
                        } else {
                            state = 3;
                            break;
                        }

                    } else if (Character.isLetter(ch)) {
                        if (97 <= ch && ch <= 107) {
                            state = -1;
                        } else {
                            state = 7;
                            break;
                        }
                    } else
                        state = -1;
                    break;

                case 6:
                    if (Character.isDigit(ch)) {
                        if (ch % 2 != 0) {
                            state = 6;
                            break;
                        } else {
                            state = 5;
                            break;
                        }

                    } else if (Character.isLetter(ch)) {
                        if (97 <= ch && ch <= 107) {
                            state = -1;
                        } else {
                            state = 7;
                            break;
                        }
                    } else
                        state = -1;
                    break;

                case 7:
                    if (Character.isLetter(ch))
                        break;
                    else {
                        state = -1;
                        break;
                    }
            }
            i += 1;
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
