package core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class SingleProcess implements Comparable<SingleProcess> {
        public CPU owner;
        public int cpuId;
        public int appearTime;
        public int fullTime;
        public int timeRemaining;
        public double cpuUsage;

        public SingleProcess(int cpuId, int appearTime, int fullTime, double processorUsage) {
            owner = null;
            this.cpuId = cpuId;
            this.appearTime = appearTime;
            this.fullTime = fullTime;
            this.timeRemaining = fullTime;
            this.cpuUsage = processorUsage;
        }

        public void connectTo(CPU cpu){
            owner = cpu;
            this.cpuId = cpuId;
        }

        public boolean doTime(int time){
            timeRemaining -= time;
            return timeRemaining > 0;
        }

        @Override
        public int compareTo(SingleProcess o) {
            return Double.compare(appearTime, o.appearTime);
        }

        public SingleProcess clone(){
            return new SingleProcess(cpuId, appearTime, fullTime, cpuUsage);
        }

    @Override
    public String toString() {
        return "SingleProcess{" +
                "owner=" + owner +
                ", cpuId=" + cpuId +
                ", appearTime=" + appearTime +
                ", executionTime=" + fullTime +
                ", timeRemaining=" + timeRemaining +
                ", processorUsage=" + cpuUsage +
                '}';
    }

    public static class Generate {
        public ArrayList<SingleProcess> generateProcessList(int N, int maxAppearTime, int maxExecutionTime, int numberOfProcesses, double z) {
            ArrayList<SingleProcess> list = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < numberOfProcesses; i++) {
                int cpuId = random.nextInt(N);
                int appearTime = random.nextInt(maxAppearTime);
                int executionTime = random.nextInt(maxExecutionTime);
                double cpuLoad = random.nextDouble() * z;
                list.add(new SingleProcess(cpuId, appearTime, executionTime, cpuLoad));
            }
            list.sort(Comparator.naturalOrder());
            return list;
        }
    }
}