//Shortest Job First
package SchedulingAlgorithms;

import Processor.*;

import java.util.ArrayList;

public class SJF {

    MemoryToDo memoryToDo;
    MemoryDone memoryDone;
    MemoryToAppear memoryToAppear;
    long time_sum = 0;

    public float algorithmSFJ(ArrayList<SingleProcess> toAppear, boolean ifWithStopping){
        memoryToDo = new MemoryToDo();
        memoryDone = new MemoryDone();
        memoryToAppear = new MemoryToAppear(toAppear);
        int actualTime = 0;

        while (memoryToDo.getLength() != 0 || memoryToAppear.getLength() != 0){
            //faza sprawdzania jakie nowe procesy sie pojawily w czasie wykonywania ostatniego
            SingleProcess p = memoryToAppear.getProcess(actualTime);
            while(p.getAppearingMoment() != -1){
                memoryToDo.addProcess(p);
                p = memoryToAppear.getProcess(actualTime);
            }
            int i = 0;
            //faza robienia procesu
            i = searcherSJF();
            if (i != -1){
                if(!ifWithStopping){
                    while (!memoryToDo.doProcess(i, 1)) {
                        actualTime++;
                        SingleProcess l = memoryToAppear.getProcess(actualTime);
                        while(l.getAppearingMoment() != -1){
                            //System.out.println(p.getAppearingMoment());
                            memoryToDo.addProcess(l);
                            l = memoryToAppear.getProcess(actualTime);
                        }
                    }

                }
                else {
                    memoryToDo.doProcess(i, 1);
                    //memoryToDo.write();
                    actualTime++;
                    p = memoryToAppear.getProcess(actualTime);
                    while(p.getAppearingMoment() != -1){
                        //System.out.println(p.getAppearingMoment());
                        memoryToDo.addProcess(p);
                        p = memoryToAppear.getProcess(actualTime);
                    }
                }
                if (memoryToDo.getLista().get(i).getTimeLeft() <= 0) {
                    time_sum += actualTime;
                    memoryDone.addProcess(memoryToDo.takeProcessOff(i));
                }
            }
            else
                actualTime++;
        }
        memoryDone.write();
        return memoryDone.getAverageTime();
        //return time_sum/memoryDone.getLength();
    }


    public int searcherSJF() {
        ArrayList<SingleProcess> lista = memoryToDo.getLista();
        int ind = 0;
        if (lista.size() == 0)
            return -1;
        for (int i = 1; i < lista.size(); i++){
            if (lista.get(i).getTimeLeft() < lista.get(ind).getTimeLeft())
                ind = i;
        }
        return ind;
    }

    public int searcherFCFS(){
        ArrayList<SingleProcess> listt = memoryToDo.getLista();
        if (listt.size() > 0)
            return 0;
        else return -1;
    }
}