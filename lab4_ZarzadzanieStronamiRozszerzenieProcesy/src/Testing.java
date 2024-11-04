import theUglySideOfFile.*;
import finallySomethingUsefull.*;

public class Testing {
    public static final int PAGES_NUMBER = 32;
    public static final int PAGES_SIZE = 512;
    public static final int FRAMES_NUMBER = 64;
    public static final int ASKS_NUMBER = 10000;
    public static final int NUMBER_OF_PROCESSES = 8;
    public static final int TIME_PART = 100;

    public static void coutZalozenia(){
        System.out.println("\nPODSTAWOWE ZALOZENIA SYMULACJI");
        System.out.println("  Strony");
        System.out.println("   -> Liczba stron na proces  " + PAGES_NUMBER);
        System.out.println("   -> Rozmiar stron " + PAGES_SIZE);
        System.out.println("  Ramki");
        System.out.println("   -> Liczba ramek " + FRAMES_NUMBER);
        System.out.println("  Procesy");
        System.out.println("   -> Liczba procesow " + NUMBER_OF_PROCESSES);
        System.out.println("  Odwolania");
        System.out.println("   -> Liczba odwolan " + ASKS_NUMBER + '\n');
    }

    //MAIN
    public static void main(String[] args) {
        coutZalozenia();
        new DoingThings().doAlgorithms(PAGES_NUMBER, PAGES_SIZE, FRAMES_NUMBER, ASKS_NUMBER, NUMBER_OF_PROCESSES, TIME_PART);
    }
}