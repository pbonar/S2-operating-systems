package theUglySideOfFile;

import java.util.ArrayList;
import java.util.Random;

public class Generating {
    public Adress[] generateAsks(int ASKS_NUMBER, int MEMORY_NUMBER, int MEMORY_SIZE) {
        Random rand = new Random();
        Adress[] toReturn = new Adress[ASKS_NUMBER];
        toReturn[0] = new Adress(rand.nextInt(MEMORY_NUMBER), rand.nextInt(MEMORY_SIZE));
        toReturn[1] = new Adress(rand.nextInt(MEMORY_NUMBER), rand.nextInt(MEMORY_SIZE));
        for (int i = 2; i < ASKS_NUMBER; i++) {
            int los = rand.nextInt() % 3;
            if (los % 3 == 0) toReturn[i] = toReturn[i - 1];
            else if (los % 3 == 1) toReturn[i] = toReturn[i - 2];
            else toReturn[i] = new Adress(rand.nextInt(MEMORY_NUMBER), rand.nextInt(MEMORY_SIZE));
        }
        return toReturn;
    }

    public ArrayList<Adress> generateAsksList(int ASKS_NUMBER, int MEMORY_NUMBER, int MEMORY_SIZE) {
        Adress[] adresses = generateAsks(ASKS_NUMBER, MEMORY_NUMBER, MEMORY_SIZE);
        ArrayList<Adress> toReturn = new ArrayList<>();
        for (Adress a : adresses){
            toReturn.add(a);
        }
        return toReturn;
    }

    Adress[] copyAsks(Adress[] asks, int ASKS_NUMBER) {
        Adress[] toReturn = new Adress[ASKS_NUMBER];
        for (int i = 0; i < ASKS_NUMBER; i++) {
            toReturn[i] = new Adress(asks[i].getPage(), asks[i].getDistance());
        }
        return toReturn;
    }

    public ArrayList<Integer> generateSequence(MyProcess[] processes){
        int CHANCE_FOR_NEXT_SAME_PROCESS = 50;
        ArrayList<Integer> toReturn = new ArrayList<Integer>();
        ArrayList<Integer> howMuchLeftPerProcess = new ArrayList<>();
        for (MyProcess p : processes)
            howMuchLeftPerProcess.add(p.getNumberOfRequests());
        int last = new Random().nextInt(processes.length);
        while (true){
            //dodawanie procesu
            if (new Random().nextInt(100) < CHANCE_FOR_NEXT_SAME_PROCESS && howMuchLeftPerProcess.get(last) > 0){
                toReturn.add(last);
                howMuchLeftPerProcess.set(last, howMuchLeftPerProcess.get(last)-1);
            } else {
                do {
                    last = (last+1)%processes.length;
                } while(howMuchLeftPerProcess.get(last) <= 0);
                toReturn.add(last);
                howMuchLeftPerProcess.set(last, howMuchLeftPerProcess.get(last)-1);
            }
            //badanie czy juz wszystkie
            boolean b = true;
            for (Integer i : howMuchLeftPerProcess){
                if (i > 0) b = false;
            }
            if (b) break;
        }
        return toReturn;
    }
}