//mi sono rotto il cazzo, incompleto
import java.util.Scanner;

public class Es1_8 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserisci numero: ");
        String sr = scanner.nextLine();
        scanner.close();
        String state = "-1";
        for(int i = 0; state != "-1" && i < sr.length(); i++){
            switch(state){ 
                case "0":
                    if(0 <= sr.charAt(i) && sr.charAt(i) <= 9){
                        state = "0";
                        break;
                    }
                    else if(sr.charAt(i)== '.'){
                        state = "punto";
                    }
                    else if(sr.charAt(i) == '+' || sr.charAt(i) == '-'){
                        state = "segno";
                        break;
                    }                             
                    else 
                        state = "-1";
                case "num": 
                    if(0 <= sr.charAt(i) && sr.charAt(i) <= 9){
                        state = "num";
                        break;
                    }  
                    else if(sr.charAt(i) == '.'){
                        state = "punto";
                        break;
                    }
                    else if(sr.charAt(i) == 'e'){
                        state = "esp";
                        break;
                    }
                    else state = "-1";
                    break;
                case "punto": 
                    if(0 <= sr.charAt(i) && sr.charAt(i) <= 9){
                        state = "NdP";
                        break;
                    }
                    else
                        state = "-1";
                        break;     
                case "NdP":
                    if(0 <= sr.charAt(i) && sr.charAt(i) <= 9){
                        state = "NdPe";
                        break;
                    }
                    else state = "-1";
                    break;
                case "NdPe":
                    if(0 <= sr.charAt(i) && sr.charAt(i) <= 9){
                        state = "NdPe";
                        break;
                    }
                    else if(sr.charAt(i) == 'e'){
                        state = "esp";
                        break;
                    }
                    else state = "-1"; 
                case "segno":
                    if(0 <= sr.charAt(i) && sr.charAt(i) <= 9){
                        state = "num";
                        break;
                    }
                    else if(sr.charAt(i) == '.'){
                        state = "punto";
                        break;
                    }
                    else 
                        state = "-1"; 
                    break;
                case "esp":
                  
            }
        }

        System.out.printf("Stringa accetata: %s", (state == "0" || state == "1")? "Si" : "No");
    }    
}
