import core.CPU;
import core.SingleProcess;

import java.util.ArrayList;
import java.util.Random;

public abstract class Simulations {
    private final ArrayList<CPU> procesory;
    private final ArrayList<SingleProcess> zadania;
    private int liczenieMigracji;
    private int sumaCzasu;

    public Simulations(ArrayList<SingleProcess> taskList, int cpuCount, double p) {
        procesory = new ArrayList<>();
        zadania = taskList;
        sumaCzasu = 0;

        for (int i = 0; i < cpuCount; i++)
            procesory.add(new CPU(p, i));
    }

    protected void migrateProcess(SingleProcess proc, CPU destination) {
        if (proc.owner != null) proc.owner.removeProcess(proc);
        destination.loadProcess(proc);
        liczenieMigracji++;
    }

    public int getMigrationCount() {
        return liczenieMigracji;
    }
    public void start() {
        int time = 0;
        int index = 0;

        while (index < zadania.size()) {
            SingleProcess nextProcess = zadania.get(index);
            int nextEventTime = 1000000000;
            for (CPU c : procesory) {
                int cpuEvent = c.getNextEventTime();
                if (cpuEvent < nextEventTime)
                    nextEventTime = cpuEvent;
            }
            if (nextEventTime < nextProcess.appearTime) {
                if (time < nextEventTime) time = nextEventTime;
                for (CPU c : procesory) {
                    c.updateTime(time);
                }
            } else {
                if (time < nextProcess.appearTime) time = nextProcess.appearTime;
                for (CPU c : procesory) {
                    c.updateTime(time);
                }
//                boolean good = false;
//                while (!good) {
//                    nextEventTime = 1000000000;
//                    for (CPU cpu : cpus) {
//                        if (cpu.load < 1.0){
//                            good = true;
//                            nextProcess.cpuId = cpu.CPUid;
//                        }
//                        int cpuEvent = cpu.getNextEventTime();
//                        if (cpuEvent < nextEventTime)
//                            nextEventTime = cpuEvent;
//                    }
//                    if (!good) {
//                        time = nextEventTime;
//                        for (CPU c : cpus) {
//                            c.updateTime(time);
//                        }
//                    }
//                }
                procesory.get(nextProcess.cpuId).loadProcess(nextProcess);
                newProcessIncomming(nextProcess, procesory.get(nextProcess.cpuId), procesory);
                index++;
            }
        }
        int nextEventTime = 0;
        while (nextEventTime < 1000000000){
            nextEventTime = 1000000000;
            for (CPU c : procesory) {
                int cpuEvent = c.getNextEventTime();
                if (cpuEvent < nextEventTime)
                    nextEventTime = cpuEvent;
            }

            if (nextEventTime < 1000000000) {
                sumaCzasu = nextEventTime;
                for (CPU c : procesory) {
                    c.updateTime(nextEventTime);
                }
            }
        }
    }

    public ArrayList<CPU> getCpus() {
        return procesory;
    }
    protected void incrementMigrations() {
        liczenieMigracji++;
    }
    public abstract void newProcessIncomming(SingleProcess proc, CPU assignedTo, ArrayList<CPU> allCpus);
    public static class Strategy1 extends Simulations {
        private final double p;
        private final double z;
        private final Random random;
        public Strategy1(ArrayList<SingleProcess> taskList, int cpuCount, double p, double z) {
            super(taskList, cpuCount, p);
            random = new Random();
            this.p = p;
            this.z = z;
        }
        public void newProcessIncomming(SingleProcess proc, CPU assignedTo, ArrayList<CPU> allCpus) {
            for (int i = 0; i < z; i++) {
                int cpuToAsk = random.nextInt(allCpus.size());
                CPU dest = allCpus.get(cpuToAsk);
                if (dest.ifOverloaded()) {
                    if (dest != assignedTo)
                        migrateProcess(proc, dest);
                    break;
                }
            }
        }
    }
    public static class Strategy2 extends Simulations {
        private final Random random;
        private final double p;
        private final double z;
        public Strategy2(ArrayList<SingleProcess> taskList, int cpuCount, double p, double z) {
            super(taskList, cpuCount, p);
            this.random = new Random();
            this.p = p;
            this.z = z;
        }
        @Override
        public void newProcessIncomming(SingleProcess proc, CPU assignedTo, ArrayList<CPU> allCpus) {
            if (assignedTo.ifOverloaded()) {
                for (int i = 0; i < z; i++) {
                    int cpuToAsk = random.nextInt(allCpus.size());
                    CPU dest = allCpus.get(cpuToAsk);

                    if (dest.ifOverloaded()) {
                        migrateProcess(proc, dest);
                        break;
                    }
                }
            }
        }
    }
    public static class Strategy3 extends Simulations {
        private final Random random;
        private final double p, r;

        public Strategy3(ArrayList<SingleProcess> taskList, int cpuCount, double p, double r) {
            super(taskList, cpuCount, p);

            this.random = new Random();
            this.p = p;
            this.r = r;
        }

        @Override
        public void newProcessIncomming(SingleProcess proc, CPU assignedTo, ArrayList<CPU> allCpus) {
            for (CPU cpu : allCpus) {
                if (cpu.ifOverloaded()) {
                    int selectedId = random.nextInt(allCpus.size());
                    CPU selected = allCpus.get(selectedId);
                    if (selected.ifOverloaded()) {
                        ArrayList<SingleProcess> releasedTasks = selected.releaseTasks();
                        for (SingleProcess p : releasedTasks) {
                            cpu.loadProcess(p);
                            incrementMigrations();
                        }
                    }
                }
            }
        }
    }
}