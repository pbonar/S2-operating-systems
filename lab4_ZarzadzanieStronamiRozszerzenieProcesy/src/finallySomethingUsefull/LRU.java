package finallySomethingUsefull;

import theUglySideOfFile.MyProcess;
import java.util.ArrayList;

public class LRU {
    public int doWholeLRU(int FRAMES_NUMBER, MyProcess process){
        ArrayList<Integer> frames = new ArrayList<>();
        int faults = 0;
        faults = 0;
        frames.clear();
        for (int i = 0; i < process.getNumberOfRequests(); i++) {
            int pageName = process.getRequestAt(i).getPage();
            if (!frames.contains(pageName)) {
                doLRU(i, frames, pageName, FRAMES_NUMBER, process);
                faults++;
            }
        }
        return faults;
    }

    public void doLRU(int currentAsk, ArrayList<Integer> pages, int pageName, int FRAMES_NUMBER, MyProcess process) {
        if (pages.size() < FRAMES_NUMBER) {
            pages.add(pageName);
        } else {
            int indexToRemove = -1; // Indeks strony do usunięcia z ramki stron
            int maxDistance = -1; // Maksymalna odległość pomiędzy bieżącym żądaniem a poprzednim użyciem strony
            for (int i = 0; i < pages.size(); i++) {
                for (int j = currentAsk; j >= 0; j--) {
                    if (pages.get(i) == process.getRequestAt(j).getPage()) {
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
            pages.add(process.getRequestAt(currentAsk).getPage()); // Dodajemy nową stronę na koniec ramki stron}
        }
    }
}
