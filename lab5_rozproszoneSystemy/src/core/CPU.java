package core;

import java.util.ArrayList;

public class CPU {
    public int CPUid;
    public int asksCount = 0;
    public double load;
    private double maxLoad;
    private int time;
    private double loadSum;
    private int loadRequests;
    private ArrayList<SingleProcess> obecneZadania;
    private ArrayList<SingleProcess> zrobioneZadania;


    public int getLoadRequests() {
        return loadRequests;
    }

    public CPU(double p, int CPUid) {
        this.CPUid = CPUid;
        this.obecneZadania = new ArrayList<>();
        this.zrobioneZadania = new ArrayList<>();
        this.load = 0.0;
        this.time = 0;
        this.loadSum = 0;
        this.loadRequests = 0;
        maxLoad = p;
    }

    public boolean loadProcess(SingleProcess process){
//        if (ifOverloaded())
//            return false;
//        else {
            obecneZadania.add(process);
            process.connectTo(this);
            load += process.cpuUsage;
            return true;
        //}
    }

    public void updateTime(int time){
        if (obecneZadania.size() == 0) load = 0.0;
//        System.out.println("Processor "+ CPUid + "  " + runningTasks.size() + "  " + load);
//        for (SingleProcess proc : runningTasks)
//            System.out.println(proc.toString());
        loadSum += load;
        asksCount++;
        for (int i = 0; i < obecneZadania.size(); i++) {
            SingleProcess process = obecneZadania.get(i);
            if (!process.doTime(time-this.time)) {
                load -= process.cpuUsage;
                zrobioneZadania.add(process);
                obecneZadania.remove(process);
                i--;
            }
        }
        this.time = time;

    }

    public ArrayList<SingleProcess> releaseTasks(){
        ArrayList<SingleProcess> toReturn = new ArrayList<>();
        while (load > maxLoad){
            SingleProcess process = obecneZadania.get(0);
            load -= process.cpuUsage;
            toReturn.add(process);
            obecneZadania.remove(process);
        }
        return toReturn;
    }

    public int getNextEventTime() {
        int minTime = 2000000000;
        for (SingleProcess p : obecneZadania) {
            if (p.timeRemaining < minTime) {
                minTime = p.timeRemaining;
            }
        }
        return time + minTime;
    }

    public void removeProcess(SingleProcess proc) {
        obecneZadania.remove(proc);
    }

    public boolean ifOverloaded(){
        loadRequests++;
        return (load > maxLoad);
    }

    public double getAvgLoad(){
        return loadSum/asksCount;
    }
}
