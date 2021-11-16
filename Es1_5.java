import java.util.Scanner;

public class Es1_5{

    public static boolean scan(String s) {
        String state = "0";
        int i = 0;
        while (state != "-1" && i < s.length()) {
            char ch = s.charAt(i);
            if(Character.isLetter(ch))
                ch = Character.toLowerCase(ch);
            switch (state) {
            case "0":
                if (Character.isLetter(ch)) {
                    if (97 <= ch && ch <= 107)
                        state = "A";
                    else
                        state = "B";
                } else
                    state = "-1";
                break;
            case "A": // corso A
                if (Character.isDigit(ch)) {
                    if (ch % 2 == 0)
                        state = "AnP";
                    else
                        state = "AnD";
                    break;
                } else if (Character.isLetter(ch)) {
                    state = "A";
                    break;
                }
                state = "-1";
                break;
            case "B": // corso B
                if (Character.isDigit(ch)) {
                    if (ch % 2 == 0)
                        state = "BnP";
                    else
                        state = "BnD";
                    break;
                } else if (Character.isLetter(ch)) {
                    state = "B";
                    break;
                } else
                    state = "-1";
                break;
            case "AnP":
                if (Character.isDigit(ch)) {
                    if (ch % 2 == 0)
                        state = "AnP";
                    else
                        state = "AnD";
                    break;
                } else
                    state = "-1";
            case "AnD":
                if (Character.isDigit(ch)) {
                    if (ch % 2 == 0)
                        state = "AnP";
                    else
                        state = "AnD";
                    break;
                } else
                    state = "-1";
            case "BnP":
                if (Character.isDigit(ch)) {
                    if (ch % 2 == 0)
                        state = "BnP";
                    else
                        state = "BnD";
                    break;
                } else
                    state = "-1";
                break;
            case "BnD":
                if (Character.isDigit(ch)) {
                    if (ch % 2 == 0)
                        state = "BnP";
                    else
                        state = "BnD";
                    break;
                } else
                    state = "-1";
                break;
            }

            i += 1;
        }
        return state == "AnP" || state == "BnD";
    }

    public static void main(String[] args) {
        String st;
        Scanner scanner = new Scanner(System.in);
        st = scanner.nextLine();
        scanner.close();
        System.out.println(scan(st) ? "OK" : "NOPE");
    }
}
