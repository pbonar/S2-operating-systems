package Adds;

import java.util.ArrayList;
import java.util.Comparator;

public class RealTimeRequest {
    private int deadline;
    private int sector;
    private int arrivalTime;

    public RealTimeRequest(int deadline, int sector, int arrivalTime) {
        this.deadline = deadline;
        this.sector = sector;
    }

    public int getDeadline() {
        return deadline;
    }

    public int getSector() {
        return sector;
    }

    public static Comparator<RealTimeRequest> getComparator() {
        Comparator<RealTimeRequest> comparator = new Comparator<RealTimeRequest>() {
            @Override
            public int compare(RealTimeRequest r1, RealTimeRequest r2) {
                return Integer.compare(r1.deadline, r2.deadline);
            }
        };
        return comparator;
    }

    public int getAppearingTime() {
        return arrivalTime;
    }
}
