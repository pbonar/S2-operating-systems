package Processor;

import java.util.ArrayList;

public class MemoryDone {
    ArrayList<SingleProcess> lista = new ArrayList<SingleProcess>();

    public void addProcess(SingleProcess process) {
        lista.add(process);
    }

    public SingleProcess getProcess(int id){
        return lista.get(id);
    }

    public float getAverageTime(){
        int time = 0;
        int number = 0;
        for (SingleProcess p : lista){
            time += p.getTimeWaiting();
            number++;
        }
        return (float) (time*1.0/number);
    }

    public int getLength(){
        return lista.size();
    }

    public void write(){
        for (SingleProcess s : lista)
            System.out.println(s.toString());
    }
}