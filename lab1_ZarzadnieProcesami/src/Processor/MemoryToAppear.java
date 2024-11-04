package Processor;

import java.util.ArrayList;
import java.util.Random;

public class MemoryToAppear {
    ArrayList<SingleProcess> lista = new ArrayList<SingleProcess>();
    Random random = new Random();

    public MemoryToAppear(ArrayList<SingleProcess> lista) {
        for (SingleProcess p : lista){
            this.lista.add(new SingleProcess(p.appearingMoment, p.timeFull));
        }
    }

    public MemoryToAppear(int numberOfProcessesAtBegin, int numberOfProcesses, int timeMinOfProcess, int timeMaxOfProcess , int maxAppearingTime) {
        for (int i = 0; i < numberOfProcessesAtBegin; i++){
            lista.add(new SingleProcess(0, random.nextInt(timeMaxOfProcess-timeMinOfProcess)+timeMinOfProcess));
        }
        for (int i = 0; i < numberOfProcesses-numberOfProcessesAtBegin; i++){
            lista.add(new SingleProcess(random.nextInt(maxAppearingTime), random.nextInt(timeMaxOfProcess-timeMinOfProcess)+timeMinOfProcess));
        }
    }

    public SingleProcess getProcess(int currentTime){
        for (SingleProcess p : lista) {
            if (p.getAppearingMoment() <= currentTime){
                lista.remove(p);
                return p;
            }

        }
        return new SingleProcess(-1, -1);
    }

    public int getLength(){
        return lista.size();
    }

    public ArrayList<SingleProcess> getLista() {
        return lista;
    }

    public void write(){
        for (SingleProcess s : lista)
            System.out.println(s.toString());
    }
}
