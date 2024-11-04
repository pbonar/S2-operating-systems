package Processor;

public class SingleProcess {
    static int numberOfProcesses = 0;
    int id;
    int appearingMoment;
    int timeDone = 0;
    int timeFull;
    int timeWaiting = 0;

    public SingleProcess(int appearingMoment, int timeFull) {
        id = numberOfProcesses;
        this.appearingMoment = appearingMoment;
        this.timeFull= timeFull;
        numberOfProcesses++;
    }

    public boolean addTimeDone(int timeDonee) {
        this.timeDone += timeDonee;
        if (timeDone >= timeFull)
            return true;
        return false;
}

    public void addTimeWaiting(int timeWaiting) {
        this.timeWaiting += timeWaiting;
    }

    public int getTimeLeft() {
        return timeFull-timeDone;
    }

    public static int getNumberOfProcesses() {
        return numberOfProcesses;
    }

    public int getId() {
        return id;
    }

    public int getAppearingMoment() {
        return appearingMoment;
    }

    public int getTimeDone() {
        return timeDone;
    }

    public int getTimeFull() {
        return timeFull;
    }

    public int getTimeWaiting() {
        return timeWaiting;
    }

    @Override
    public String toString() {
        return "SingleProcess{" +
                "id=" + id +
                ", appearingMoment=" + appearingMoment +
                ", timeDone=" + timeDone +
                ", timeFull=" + timeFull +
                ", timeWaiting=" + timeWaiting +
                '}';
    }
}
