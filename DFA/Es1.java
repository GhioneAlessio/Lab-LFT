import java.util.*;
public class Es1{
    public static void main(String[] args) {
        String st;
        int state = 0;
        Scanner scanner = new Scanner(System.in);
        st = scanner.nextLine();
        scanner.close();
        for(int i = 0; state >= 0 && i <st.length(); i ++ ){
            switch(st.charAt(i)){
                case '0':  
                    state++;
                    break;
                case '1':
                    state = 0;
                    break;
                default: 
                    state = -1;
            }
            if(state==3)
            break;
        }
        System.out.println(state==3);
    }
}