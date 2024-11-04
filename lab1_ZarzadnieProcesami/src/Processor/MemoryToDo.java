package Processor;

import java.util.ArrayList;

public class MemoryToDo {
    ArrayList<SingleProcess> lista = new ArrayList<SingleProcess>();
    boolean ifWaitingTimeAtActiveObject = true;

    public void addProcess(SingleProcess process) {
        lista.add(process);
    }

    public SingleProcess takeProcessOff(int id) {
        SingleProcess process = lista.get(id);
        lista.remove(id);
        return process;
    }

    public SingleProcess getProcess(int id){
        return lista.get(id);
    }

    public boolean doProcess(int id, int time){
        for(int i = 0; i < lista.size(); i++)
            if (lista.get(i).getId() != id || ifWaitingTimeAtActiveObject)
                lista.get(i).addTimeWaiting(time);
        return lista.get(id).addTimeDone(time);
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