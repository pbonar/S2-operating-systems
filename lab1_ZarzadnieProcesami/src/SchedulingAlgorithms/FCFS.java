//First Come First Served
package SchedulingAlgorithms;

import Processor.*;

import java.util.ArrayList;

public class FCFS {

    MemoryToDo memoryToDo;
    MemoryDone memoryDone;
    MemoryToAppear memoryToAppear;


    public float algorithmFCFS(ArrayList<SingleProcess> toAppear){
        memoryToDo = new MemoryToDo();
        memoryDone = new MemoryDone();
        memoryToAppear = new MemoryToAppear(toAppear);
        //memoryToAppear.write();
        int actualTime = 0;
        int i = -1;
        long sumOfTime = 0;
        //System.out.println(memoryToDo.getLength() + " " + memoryToAppear.getLength());


        while (memoryToDo.getLength() != 0 || memoryToAppear.getLength() != 0){

            //faza sprawdzania jakie nowe procesy sie pojawily w czasie wykonywania ostatniego
            SingleProcess p = memoryToAppear.getProcess(actualTime);
            while(p.getAppearingMoment() != -1){
                //System.out.println(p.getAppearingMoment());
                memoryToDo.addProcess(p);
                p = memoryToAppear.getProcess(actualTime);
            }
            if (memoryToDo.getLista().size() == 0)
                i = -1;
            else i = (i+1)%memoryToDo.getLista().size();
            //System.out.println(i + " " + actualTime);

            //faza robienia procesu
            if (i != -1){
                int oneTimee = memoryToDo.getLista().get(i).getTimeLeft();
                //System.out.println(oneTimee + " " + oneTime);
                actualTime += oneTimee;
                //memoryToDo.doProcess(i, oneTimee);
                for (int j = 0; j < oneTimee; j++){
                    memoryToDo.doProcess(i, 1);
                    actualTime++;
                    //sumOfTime += memoryToDo.getLength();
                    p = memoryToAppear.getProcess(actualTime);
                    while(p.getAppearingMoment() != -1){
                        //System.out.println(p.getAppearingMoment());
                        memoryToDo.addProcess(p);
                        p = memoryToAppear.getProcess(actualTime);
                    }
                }

                //memoryToDo.write();
                if (memoryToDo.getLista().get(i).getTimeLeft() <= 0){
                    sumOfTime += actualTime - memoryToDo.getLista().get(i).getAppearingMoment();
                    memoryDone.addProcess(memoryToDo.takeProcessOff(i));
                    //System.out.println(actualTime);
                }
            }
            else
                actualTime++;
        }
        //memoryDone.write();
        //return (float) (sumOfTime*1.0/memoryDone.getLength());

        return memoryDone.getAverageTime();
    }

}
