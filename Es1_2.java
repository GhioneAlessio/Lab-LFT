import java.util.*;
public class Es1_2{
    public static void main(String[] args) {
        String st;
        int state = 0;
        Scanner scanner = new Scanner(System.in);
        st = scanner.nextLine();
        scanner.close();
        
        for(int i=0; i < st.length() && state >=0; i ++)  {
            switch(state){
                case 0:
                    if(st.charAt(i)== '_')
                    state = 1;
                    else if(Character.isLetter(st.charAt(i))) 
                    state = 2;
                    else
                    state = -1;
                    break;    
                case 1: 
                    if(st.charAt(i)== '_')
                        state = 1;
                    else if(Character.isDigit(st.charAt(i)) || Character.isLetter(st.charAt(i)) )
                        state = 2;
                    else 
                        state = -1;
                        break;
                case 2: 
                    if(Character.isDigit(st.charAt(i)) || Character.isLetter(st.charAt(i)) || st.charAt(i) == '_')
                    state = 2; 
                    else state = -1;
                    break;   
            }
        }
        System.out.println(state == 2);
    }
}