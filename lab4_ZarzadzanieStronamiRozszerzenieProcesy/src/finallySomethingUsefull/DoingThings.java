package finallySomethingUsefull;

import theUglySideOfFile.*;

import java.util.ArrayList;
import java.util.Random;

public class DoingThings {
    public int doProcess(MyProcess process, int ASKS_NUMBER, int FRAMES_NUMBER){
        ArrayList<Integer> pages = new ArrayList<>();
        LRU lru = new LRU();
        int faults = 0;
        pages.clear();
        for (int i = 0; i < ASKS_NUMBER; i++) {
            int pageName = process.getRequestAt(i).getPage();
            if (!pages.contains(pageName)) {
                lru.doLRU(i, pages, pageName, FRAMES_NUMBER, process);
                faults++;
            }
        }
        return faults;
    }

    public int doProcessStrefowo(MyProcess process, int ASKS_NUMBER, int FRAMES_NUMBER, ArrayList<Integer> whichPage, int processName){
        ArrayList<Integer> pages = new ArrayList<>();
        LRU lru = new LRU();
        int faults = 0;
        pages.clear();
        int current = 0;
        for (Integer i : whichPage) {
            if (i == processName) {
                int pageName = process.getRequestAt(i).getPage();
                if (!pages.contains(pageName)) {
                    lru.doLRU(current, pages, pageName, FRAMES_NUMBER, process);
                    faults++;
                    current++;
                }
            } else {
                pages.clear();
            }
        }
        return faults;
    }

    public int doStrefowo(MyProcess[] processes, int FRAMES_NUMBER, ArrayList<Integer> whichPage, int TIME_PART){
        int[] faultsOfProcesses = new int[processes.length];
        int[] framesPerProcess = new int[processes.length];
        for (int i = 0; i < processes.length; i++) {
            framesPerProcess[i] = Math.max(1, FRAMES_NUMBER / processes.length);
        }
        int hm = 0;
        for (Integer i : whichPage){
            for (int j = 0; j < TIME_PART; j++){
                faultsOfProcesses[i] = processes[i].doProcessStrefowoPart(processes[i], framesPerProcess[i], whichPage, i);
            }
            hm++;
            int suma = 0;
            for (int l = 0; l < processes.length; l++){
                suma += processes[l].getPageErrors();
            }
            for (int j = 0; j < processes.length; j++){
                framesPerProcess[j] = Math.max(1, FRAMES_NUMBER*faultsOfProcesses[j]/suma);
            }
        }
//        for (int i = 0; i < processes.length; i++){
//            System.out.println(i + " " + framesPerProcess[i]);
//        }
        int suma = 0;
        for (int i = 0; i < processes.length; i++){
            suma += processes[i].getPageErrors();
        }
        return suma;
    }

    public void doAlgorithms(int PAGES_NUMBER, int PAGES_SIZE, int FRAMES_NUMBER, int ASKS_NUMBER, int NUMBER_OF_PROCESSES, int TIME_PART){
        MyProcess[] processes = new MyProcess[NUMBER_OF_PROCESSES];
        for (int i = 0; i < NUMBER_OF_PROCESSES; i++) {
            processes[i] = new MyProcess(new Random().nextInt(PAGES_NUMBER)+1);
            processes[i].generateRequests(Math.abs(new Random().nextInt(ASKS_NUMBER)+1), PAGES_SIZE);
        }
        //rowny
        System.out.println("ROWNY");
        int n = FRAMES_NUMBER/NUMBER_OF_PROCESSES;
        int sumOfFaults = 0;
        int[] faultsTab = new int[NUMBER_OF_PROCESSES];
        int[] faultsSum = new int[4];
        for(int i = 0; i < NUMBER_OF_PROCESSES; i++){
            int faults = doProcess(processes[i], processes[i].getRequests().size(), n);
            System.out.println("  proces " + i + ": "+ faults + " n: " + n);
            sumOfFaults += faults;
            faultsTab[i] = faults;
        }
        faultsSum[0] = sumOfFaults;
        System.out.println("  SUMA:     " + sumOfFaults);
        //proporcjonalny
        System.out.println("PROPORCJONALNY");
        int sumOfAsks = 0;
        for (int i = 0; i < NUMBER_OF_PROCESSES; i++){
            sumOfAsks += processes[i].getNumberOfRequests();
        }
        int sumOfPages = 0;
        for (MyProcess p : processes) sumOfPages += p.getNumber();
        sumOfFaults = 0;
        for(int i = 0; i < NUMBER_OF_PROCESSES; i++){
            int faults = doProcess(processes[i], processes[i].getRequests().size(), Math.max(1, FRAMES_NUMBER*processes[i].getNumber()/sumOfPages));
            System.out.println("  proces " + i + ": "+ faults + " n: " +  Math.max(1, FRAMES_NUMBER*processes[i].getNumber()/sumOfPages) + " liczba stron: " + processes[i].getNumber());
            sumOfFaults += faults;
        }
        System.out.println("  SUMA:     " + sumOfFaults);
        faultsSum[1] = sumOfFaults;
        //sterowanie czestoscia bledow strony
        System.out.println("STEROWANIE CZESTOSCIA BLEDOW STRONY");
        sumOfFaults = 0;
        for(int i = 0; i < NUMBER_OF_PROCESSES; i++){
            int faults = doProcess(processes[i], processes[i].getRequests().size(), Math.max(1, faultsTab[i]*FRAMES_NUMBER/faultsSum[0]));
            System.out.println("  proces " + i + ": "+ faults + " n: " + Math.max(1, faultsTab[i]*FRAMES_NUMBER/faultsSum[0]));
            sumOfFaults += faults;
        }
        System.out.println("  SUMA:     " + sumOfFaults);
        //model strefowy
        System.out.println("MODEL STREFOWY");
        ArrayList<Integer> whichProcess = new Generating().generateSequence(processes);
        int suma = doStrefowo(processes, FRAMES_NUMBER, whichProcess, TIME_PART);
        for(int i = 0; i < NUMBER_OF_PROCESSES; i++)
            System.out.println("  proces " + i + ": "+ processes[i].getPageErrors());
        System.out.println("  SUMA:     " + suma);

        //model strefowy
//        System.out.println("MODEL STREFOWY GDYBY WSZYSTKO WPADLO NA RAZ");
//        sumOfFaults = 0;
//        for(int i = 0; i < NUMBER_OF_PROCESSES; i++){
//            int faults = doProcess(processes[i], processes[i].getRequests().size(), FRAMES_NUMBER/TIME_PART);
//            System.out.println("  proces " + i + ": "+ faults + " n: " + FRAMES_NUMBER/TIME_PART);
//            sumOfFaults += faults;
//        }
//        System.out.println("  SUMA:     " + sumOfFaults);
//        System.out.println("MODEL STREFOWY");
//        sumOfFaults = 0;
//        ArrayList<Integer> whichProcess = new Generating().generateSequence(processes);
//        for(int i = 0; i < NUMBER_OF_PROCESSES; i++){
//            int faults = doProcessStrefowo(processes[i], processes[i].getRequests().size(), FRAMES_NUMBER/TIME_PART, whichProcess, i);
//            System.out.println("  proces " + i + ": "+ faults + " n: " + FRAMES_NUMBER/TIME_PART);
//            sumOfFaults += faults;
//        }
//        System.out.println("  SUMA:     " + sumOfFaults);
    }
}