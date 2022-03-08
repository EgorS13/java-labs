import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ///Scanner in = new Scanner(System.in);
        ///System.out.print("Введите слово: ");
        ///String str = in.nextLine();
        for (int i = 0; i < args.length; i++) {
            String s = args[i];
            System.out.println(s);
            System.out.println(reverseString(s));
            if (isPalindrome(s) == true)
            {
                System.out.println("True");
                System.out.println(" ");
            }
            else
            {
                System.out.println("False");
                System.out.println(" ");
            }
        }
    }
    public static String reverseString(String s){
        String sfin = "";
        for (int i = 0; i < s.length(); i++) {
            sfin += s.charAt(s.length() - 1 - i);
        }
        return sfin;
    }
    public static boolean isPalindrome(String s){
        return (s.equals(reverseString(s)));
    }

}
