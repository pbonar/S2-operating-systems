import things.adress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Main {
//  Zadanie 3
//  Badanie algorytmów zastępowania stron.
//  Należy samodzielnie sformułować założenia symulacji:
//  - rozmiar pamięci wirtualnej (ilość stron).
//  - rozmiar pamięci fizycznej (ilość ramek).
//  - długość (powinna być znaczna - min. 1000) i sposób generowania ciągu odwołań do stron (koniecznie uwzględnić zasadę lokalności odwołań).
//  Działanie programu:
//  - wygenerować losowy ciąg n odwołań do stron
//  - dla wygenerowanego ciągu podać liczbę błędów strony dla różnych algorytmów zastępowania stron:
//  1. FIFO (usuwamy stronę najdłużej przebywającą w pamięci fizycznej)
//  2. OPT (optymalny - usuwamy stronę, która nie będzie najdłużej używana)
//  3. LRU (usuwamy stronę, do której najdłużej nie nastąpiło odwołanie)
//  4. aproksymowany
//  5. RAND (usuwamy losowo wybraną stronę)
//  - symulacje przeprowadzić (na tym samym ciągu testowym) dla różnej liczby ramek (np. kilku (3, 5, 10?)  wartości podanych przez użytkownika)
//  Zakres materiału: wszystko o pamięci wirtualnej (z wykładu).

    public static final int PAGES_NUMBER = 32;
    public static final int PAGES_SIZE = 512;
    public static final int FRAMES_NUMBER = 16;
    public static final int ASKS_NUMBER = 100000;
    public static adress[] ASKS = generateAsks();
    public static boolean[] pomocdlaaproksymowanego = new boolean[FRAMES_NUMBER];

    //ASK: generating and coping;
    static adress[] generateAsks() {
        Random rand = new Random();
        adress[] toReturn = new adress[ASKS_NUMBER];
        toReturn[0] = new adress(rand.nextInt(PAGES_NUMBER), rand.nextInt(PAGES_SIZE));
        toReturn[1] = new adress(rand.nextInt(PAGES_NUMBER), rand.nextInt(PAGES_SIZE));
        for (int i = 2; i < ASKS_NUMBER; i++) {
            int los = rand.nextInt()%3;
            if(los%3 == 0) toReturn[i] = toReturn[i-1];
            else if(los%3 == 1) toReturn[i] = toReturn[i-2];
            else toReturn[i] = new adress(rand.nextInt(PAGES_NUMBER), rand.nextInt(PAGES_SIZE));
        }
        return toReturn;
    }

    static adress[] copyAsks(adress[] asks) {
        adress[] toReturn = new adress[ASKS_NUMBER];
        for (int i = 0; i < ASKS_NUMBER; i++) {
            toReturn[i] = new adress(asks[i].getPage(), asks[i].getDistance());
        }
        return toReturn;
    }

    //ALGORITMS: which page to delete
    static void FIFO(int currentAsk, ArrayList<Integer> pages, int pageName) {
        //jesli jest jakies puste miejsce
        if (pages.size() < FRAMES_NUMBER) {
            pages.add(pageName);
            return;
        } else {
            pages.remove(0);
            pages.add(pageName);
        }
    }

    static void OPT(int currentAsk, ArrayList<Integer> pages, int pageName) {
        if (pages.size() < FRAMES_NUMBER) {
            pages.add(pageName); // Jest jeszcze miejsce w ramce stron, dodaj nową stronę
        } else {
            int[] nextReferences = new int[FRAMES_NUMBER]; // Tablica przechowująca indeksy następnych odwołań do każdej strony
            Arrays.fill(nextReferences, -1); // Ustawiamy początkowe wartości na -1, co oznacza, że strona nie zostanie już użyta
            for (int i = 0; i < FRAMES_NUMBER; i++) {
                int page = pages.get(i);
                for (int j = currentAsk + 1; j < ASKS_NUMBER; j++) {
                    if (ASKS[j].getPage() == page) {
                        nextReferences[i] = j; // Znaleziono następne odwołanie do strony, zapisz indeks
                        break;
                    }
                }
            }
            int pageToRemove = 0;
            for (int i = 1; i < FRAMES_NUMBER; i++) {
                if (nextReferences[i] == -1) {
                    pageToRemove = i;
                    break; // Nie będzie już używana, usuwamy ją od razu
                }
                if (nextReferences[i] > nextReferences[pageToRemove]) {
                    pageToRemove = i; // Wybieramy stronę, która zostanie użyta najpóźniej
                }
            }
            pages.set(pageToRemove, pageName); // Zastępujemy wybraną stronę nową
        }
    }

    static void LRU(int currentAsk, ArrayList<Integer> pages, int pageName) {
        if (pages.size() < FRAMES_NUMBER) {
            pages.add(pageName);
        } else {
            int indexToRemove = -1; // Indeks strony do usunięcia z ramki stron
            int maxDistance = -1; // Maksymalna odległość pomiędzy bieżącym żądaniem a poprzednim użyciem strony
            for (int i = 0; i < pages.size(); i++) {
                for (int j = currentAsk; j >= 0; j--) {
                    if (pages.get(i) == ASKS[j].getPage()) {
                        // Obliczamy odległość między bieżącym żądaniem a poprzednim użyciem strony
                        int distance = currentAsk - j;
                        if (distance > maxDistance) {
                            // Zapisujemy indeks i odległość do usunięcia strony z ramki stron
                            indexToRemove = i;
                            maxDistance = distance;
                        }
                        break;
                    }
                }
            }
            if (indexToRemove >= 0) {
                pages.remove(indexToRemove); // Usuwamy najdłużej nieużywaną stronę z ramki stron
            } else if (pages.size() >= FRAMES_NUMBER) {
                pages.remove(0); // Usuwamy najdłużej nieużywaną stronę z ramki stron
            }
            pages.add(ASKS[currentAsk].getPage()); // Dodajemy nową stronę na koniec ramki stron}
        }
    }

    static void Aproksymalny(int currentAsk, ArrayList<Integer> pages, int pageName) {
        // Jeśli jest wolne miejsce w ramce stron, dodajemy nową stronę
        if (pages.size() < FRAMES_NUMBER) {
            pages.add(pageName);
        } else {
            int indexToRemove = 0;
            for (int i = 0; i < pages.size(); i++) {
                if(pomocdlaaproksymowanego[i])
                    pomocdlaaproksymowanego[i] = false;
                else {
                    indexToRemove = i;
                    break;
                }
            }
            pages.set(indexToRemove, pageName); // Zastępujemy najdłużej nieużywaną stronę nową stroną
            pomocdlaaproksymowanego[indexToRemove] = true;
        }
    }

    static void Rand(int currentAsk, ArrayList<Integer> pages, int pageName) {
        if (pages.size() < FRAMES_NUMBER) {
            pages.add(pageName);
            return;
        } else {
            Random rand = new Random();
            pages.set(rand.nextInt(FRAMES_NUMBER), pageName);
        }
    }

    //MAIN
    public static void main(String[] args) {
        ArrayList<Integer> pages = new ArrayList<>();
        int faults = 0;

        // FIFO
        System.out.println("FIFO");
        faults = 0;
        pages.clear();
        for (int i = 0; i < ASKS_NUMBER; i++) {
            int pageName = ASKS[i].getPage();
            if (!pages.contains(pageName)) {
                FIFO(i, pages, pageName);
                faults++;
            }
        }
        System.out.println("faults: " + faults);

        // OPT
        System.out.println("OPT");
        faults = 0;
        pages.clear();
        for (int i = 0; i < ASKS_NUMBER; i++) {
            int pageName = ASKS[i].getPage();
            if (!pages.contains(pageName)) {
                OPT(i, pages, pageName);
                faults++;
            }
        }
        System.out.println("faults: " + faults);

        // LRU
        System.out.println("LRU");
        faults = 0;
        pages.clear();
        for (int i = 0; i < ASKS_NUMBER; i++) {
            int pageName = ASKS[i].getPage();
            if (!pages.contains(pageName)) {
                LRU(i, pages, pageName);
                faults++;
            }
        }
        System.out.println("faults: " + faults);

        // Aproksymowany
        for (int i = 0; i < FRAMES_NUMBER; i++){
            pomocdlaaproksymowanego[i] = true;
        }
        System.out.println("APROKSYMOWANY");
        faults = 0;
        pages.clear();
        for (int i = 0; i < ASKS_NUMBER; i++) {
            int pageName = ASKS[i].getPage();
            if (!pages.contains(pageName)) {
                Aproksymalny(i, pages, pageName);
                faults++;
            }
        }
        System.out.println("faults: " + faults);

        // Random
        System.out.println("RANDOM");
        faults = 0;
        pages.clear();
        for (int i = 0; i < ASKS_NUMBER; i++) {
            int pageName = ASKS[i].getPage();
            if (!pages.contains(pageName)) {
                Rand(i, pages, pageName);
                faults++;
            }
        }
        System.out.println("faults: " + faults);

    }
}