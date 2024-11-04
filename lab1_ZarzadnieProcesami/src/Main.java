import Processor.*;
import SchedulingAlgorithms.*;

import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        int n = 1;

        float suma1 = 0;
        float suma2 = 0;
        float suma3 = 0;
        float suma4 = 0;

        int numberOfProcessesAtBegin = 10;
        int numberOfProcesses = 20;
        int timeMinOfProcess = 1;
        int timeMaxOfProcess = 1000;
        int maxAppearingTime = 100;
        int rotacyjnyOne = 1000;

        FCFS fcfs = new FCFS();
        SJF sjf = new SJF();
        Rotacyjny rotacyjny = new Rotacyjny();

        ArrayList<SingleProcess> m1 = new MemoryToAppear(numberOfProcessesAtBegin, numberOfProcesses, timeMinOfProcess, timeMaxOfProcess, maxAppearingTime).getLista();

        for (int i = 0; i < n; i++){
            suma1 += sjf.algorithmSFJ(new MemoryToAppear(m1).getLista(), true);
            System.out.println();
            suma2 += sjf.algorithmSFJ(new MemoryToAppear(m1).getLista(), false);
            suma3 += fcfs.algorithmFCFS(new MemoryToAppear(m1).getLista());
            suma4 += rotacyjny.algorithmRotacyjny(new MemoryToAppear(m1).getLista(), rotacyjnyOne);
        }
        
        System.out.println("Algorithm SFJ-1                  AvgTime: " + suma1/n);
        System.out.println("Algorithm SFJ-2                  AvgTime: " + suma2/n);
        System.out.println("Algorithm FCFS                   AvgTime: " + suma3/n);
        System.out.println("Algorithm Rotacyjny              AvgTime: " + suma4/n);
    }
}