import java.util.Scanner;

public class Es1_6 {
    public static boolean scan(String s) {
        String state  = "0";
        int i = 0;
        while (state != "-1" && i < s.length()) {
            char ch = s.charAt(i);
            if(Character.isLetter(ch))
                ch = Character.toLowerCase(ch);
            switch (state) {
            case "0" :
                if (Character.isDigit(ch)){
                    if(ch % 2 == 0){
                        state = "np";
                        break;
                    }
                    else{
                        state = "nd";
                        break;
                    }
                }
                else 
                    state = "-1";
                break;
            case "np":
                if (Character.isDigit(ch)){
                    if(ch % 2 == 0){
                        state = "pp";
                        break;
                    }
                    else{
                        state = "pd";
                        break;
                    }
                }
                else
                    state = "-1";
                break;
            case "nd":
                if (Character.isDigit(ch)){
                    if(ch % 2 == 0){
                        state = "dp";
                        break;
                    }
                    else{
                        state = "dd";
                        break;
                    }
                }
                else
                    state = "-1";
                break;

            case "pp":
                if(Character.isDigit(ch)){
                    if(ch % 2 != 0){
                        state = "pd";
                        break;
                    }else 
                    break;
                }else if(Character.isLetter(ch)){
                    if(97 <= ch && ch <= 107){
                        state = "t2";
                    }else 
                    state ="-1";
                    break;
                }
            case "pd":
                if(Character.isDigit(ch)){
                    if(ch % 2 != 0){
                        state = "dd";
                        break;
                    }else{
                        state = "dp";
                        break;
                    }
                    
                }else if(Character.isLetter(ch)){
                    if(97 <= ch && ch <= 107){
                        state = "t2";
                    }else {
                    state ="-1";
                    break;}
                }else 
                state = "-1";
                break;
                
            case "dp":
                if(Character.isDigit(ch)){
                    if(ch % 2 != 0){
                        state = "pd";
                        break;
                    }else{
                        state = "pp";
                        break;
                    }
                    
                }else if(Character.isLetter(ch)){
                    if(97 <= ch && ch <= 107){
                        state = "-1";
                    }else {
                    state ="t3";
                    break;}
                }else 
                state = "-1";
                break;

            case "dd": 
                if(Character.isDigit(ch)){
                    if(ch % 2 != 0){
                        state = "dd";
                        break;
                    }else{
                        state = "dp";
                        break;
                    }
                    
                }else if(Character.isLetter(ch)){
                    if(97 <= ch && ch <= 107){
                        state = "=1";
                    }else {
                    state ="t3";
                    break;}
                }else 
                state = "-1";
                break;
            }
            i+=1;
    }
    return state == "t2" || state == "t3";
    }

    public static void main(String[] args) {
        String st;
        Scanner scanner = new Scanner(System.in);
        st = scanner.nextLine();
        scanner.close();
        System.out.println(scan(st) ? "OK" : "NOPE");
    }
}
