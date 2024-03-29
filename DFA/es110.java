class es110 {
    public static void main(String[] args) {
        String[] ar = { "/****/", "/*a*a*/", "/*a/**/", "/**a///a/a**/", "/**/", "/*/*/", "/*/", "/**/***/" };
        for (int i = 0; i < ar.length; i++)
            System.out.println(scan(ar[i]) ? "OK" : "NOPE");
    }

    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if (ch == 'a' || ch == '*')
                        state = 1;
                    else if (ch == '/')
                        state = 2;
                    else
                        state = -1;
                    break;
                case 1:
                    if (ch == 'a' || ch == '*')
                        state = 1;
                    else if (ch == '/')
                        state = 2;
                    else
                        state = -1;
                    break;
                case 2:
                    if (ch == 'a')
                        state = 1;
                    else if (ch == '*')
                        state = 3;
                    else if (ch == '/')
                        state = 2;
                    else
                        state = -1;
                    break;
                case 3:
                    if (ch == 'a' || ch == '/')
                        state = 3;
                    else if (ch == '*')
                        state = 4;
                    else
                        state = -1;
                    break;

                case 4:
                    if (ch == '*')
                        state = 4;
                    else if (ch == 'a')
                        state = 3;
                    else if (ch == '/')
                        state = 5;
                    else
                        state = -1;
                    break;
                case 5:
                    if (ch == 'a' || ch == '*')
                        state = 5;
                    else if (ch == '/')
                        state = 6;
                    else
                        state = -1;
                    break;
                case 6:
                    if (ch == '*' || ch == 'a' || ch == '/')
                        state = 4;
            }
        }
        return state == 1 || state == 5;
    }
}