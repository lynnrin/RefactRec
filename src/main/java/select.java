import java.util.InputMismatchException;
import java.util.Scanner;

public class select {
    public static int selection(){
        int x = 0;
        Scanner scanner = new Scanner(System.in);
        parameter pm = new parameter();
        String[] refactoringType = pm.getRefactoringTypeList();
        do {
            try {
                System.out.println("plz enter the integer according to the list below");
                for (int i = 0; i < refactoringType.length; i++){
                    System.out.println(i + " : " + refactoringType[i]);
                }
                x = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("invalid input value");
            }
        }while (!(x >= 0 && x < refactoringType.length));

        return x;
    }
}
