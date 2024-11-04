import Adds.RealTimeRequest;

import java.util.*;

public class Main {
    //==== ZMIENNE DO OBSLUGI ====
    private static final int STARTING_POINT = 500;
    private static final int MAX_DISK_BLOCKS = 1000;
    private static final int NUM_REQUESTS = 100;
    private static final int NUM_REQUESTS_REALTIME = 50;
    private static final int MAX_DEADLINE = 10000;
    private static final int MAX_ARRIVAL_TIME = 1;

    //==== ZMIENNE STALE ====
    private static final int SCAN_DIRECTION_LEFT = -1;
    private static final int SCAN_DIRECTION_RIGHT = 1;

    //==== POCZATKOWE METODY ====
    private static ArrayList<Integer> generateRequests() {
        Random random = new Random();
        ArrayList<Integer> lista = new ArrayList<>();
        for (int i = 0; i < NUM_REQUESTS; i++){
            lista.add(Math.abs(random.nextInt()%MAX_DISK_BLOCKS));
        }
        return lista;
    }
    public static ArrayList<RealTimeRequest> generateRealTime() {
        ArrayList<RealTimeRequest> realTimeRequests = new ArrayList<>();
        for (int i = 1; i <= NUM_REQUESTS_REALTIME; i++) {
            realTimeRequests.add(new RealTimeRequest(Math.abs(new Random().nextInt()%MAX_DEADLINE), Math.abs(new Random().nextInt()%MAX_DISK_BLOCKS), Math.abs(new Random().nextInt()%MAX_ARRIVAL_TIME)));
        }
        return realTimeRequests;
    }
    private static ArrayList<Integer> copyRequests(ArrayList<Integer> listaP){
        ArrayList<Integer> lista = new ArrayList<>();
        for (int i = 0; i < NUM_REQUESTS; i++){
            lista.add(listaP.get(i));
        }
        return lista;
    }
    private static ArrayList<RealTimeRequest> copyRealTime(List<RealTimeRequest> a) {
        ArrayList<RealTimeRequest> realTimeRequests = new ArrayList<>();
        for (int i = 1; i <= NUM_REQUESTS_REALTIME; i++) {
            realTimeRequests.add(new RealTimeRequest(a.get(i-1).getDeadline(), a.get(i-1).getSector(), 0));
        }
        return realTimeRequests;
    }

    //==== METODY Z ALGORYTMAMI - NIE REAL TIME ====
    public static int runFCFS(List<Integer> requests, int startingPoint, int time, boolean ifCout, int alreadyDone) {
        int currentPosition = startingPoint;
        int totalHeadMovement = 0;

        for (int i = 0; i < requests.size(); i++) {
            int requestedBlock = requests.get(i);

            // Update total head movement
            totalHeadMovement += Math.abs(requestedBlock - currentPosition);

            // Move head to requested block
            currentPosition = requestedBlock;
        }
        if (ifCout) System.out.println("  number of moves: " + (totalHeadMovement+alreadyDone));
        return currentPosition;
    }
    public static int runSSTF(List<Integer> requests, int startingPoint, int time, boolean ifCout, int alreadyDone) {
        int currentPosition = startingPoint;
        int totalHeadMovement = 0;

        while (!requests.isEmpty()) {
            int closestRequestIndex = findClosestRequestIndex(requests, currentPosition);
            int closestRequest = requests.remove(closestRequestIndex);

            // Update total head movement
            totalHeadMovement += Math.abs(closestRequest - currentPosition);

            // Move head to closest request
            currentPosition = closestRequest;
        }
        if (ifCout) System.out.println("  number of moves: " + (totalHeadMovement+alreadyDone));
        return currentPosition;
    }
    public static int runSCAN(List<Integer> requests, int startingPoint, int time, boolean ifCout, int alreadyDone) {
        int currentPosition = startingPoint;
        int totalHeadMovement = 0;
        int direction = SCAN_DIRECTION_RIGHT;

        // Sort requests in ascending order
        Collections.sort(requests);

        while (!requests.isEmpty()) {
            int nextRequestIndex = findNextRequestIndex(requests, currentPosition, direction);
            if (nextRequestIndex == -1) {
                // Change direction when reaching the end
                totalHeadMovement += 2*(MAX_DISK_BLOCKS-currentPosition);
                direction = SCAN_DIRECTION_LEFT;
            } else {
                //System.out.println(requests.get(nextRequestIndex));
                int nextRequest = requests.remove(nextRequestIndex);
                listCheckOutPosition(requests, currentPosition);
                // Update total head movement
                totalHeadMovement += Math.abs(nextRequest - currentPosition);

                // Move head to next request
                currentPosition = nextRequest;
            }
        }
        if (ifCout) System.out.println("  number of moves: " + (totalHeadMovement+alreadyDone));
        return currentPosition;
    }
    public static int runCSCAN(List<Integer> requests, int startingPoint, int time, boolean ifCout, int alreadyDone) {
        int currentPosition = startingPoint;
        int totalHeadMovement = 0;

        // Sort requests in ascending order
        Collections.sort(requests);

        while (!requests.isEmpty()) {
            int nextRequestIndex = findNextRequestIndex(requests, currentPosition, SCAN_DIRECTION_RIGHT);
            if (nextRequestIndex == -1) {
                // Move head to the first request in the queue
                totalHeadMovement += currentPosition + 2*(MAX_DISK_BLOCKS-currentPosition);
                currentPosition = 0;
            } else {
                int nextRequest = requests.remove(nextRequestIndex);
                listCheckOutPosition(requests, currentPosition);
                // Update total head movement
                totalHeadMovement += Math.abs(nextRequest - currentPosition);

                // Move head to next request
                currentPosition = nextRequest;
            }
        }
        if (ifCout) System.out.println("  number of moves: " + (totalHeadMovement+alreadyDone));
        return currentPosition;
    }

    //==== DODATKOWE POTRZEBNE METODY ====
    private static int findClosestRequestIndex(List<Integer> requests, int currentPosition) {
        int curindex = 0;
        int ind = 0;
        for (int i : requests) {
            if (Math.abs(i - currentPosition) < Math.abs(requests.get(curindex) - currentPosition)) {
                curindex = ind;
            }
            ind++;
        }
        return curindex;
    }
    public static int findNextRequestIndex(List<Integer> requests, int currentPosition, int direction) {
        int nextRequestIndex = -1;
        int distance = Integer.MAX_VALUE;

        // Find the request with the closest distance in the current direction
        for (int i = 0; i < requests.size(); i++) {
            int currentDistance = Math.abs(requests.get(i) - currentPosition);
            if (requests.get(i) > currentPosition && direction == 1) {
                if (currentDistance < distance) {
                    nextRequestIndex = i;
                    distance = currentDistance;
                }
            } else if (requests.get(i) < currentPosition && direction == -1) {
                if (currentDistance < distance) {
                    nextRequestIndex = i;
                    distance = currentDistance;
                }
            }
        }
        return nextRequestIndex;
    }
    public static int listCheckOutPosition(List<Integer> requests, int currentPosition){
        int howMuch = 0;
        for(int i = 0; i < requests.size(); i++){
            if (requests.get(i) == currentPosition){
                howMuch++;
                requests.remove(i);
                i--;
            }
        }
        return howMuch;
    }

    //==== METODY Z ALGORYTMAMI - REAL TIME ====
    public static int runEDF(List<Integer> requests, List<RealTimeRequest> realTimeRequests, int typee) {
        //typee 1- FCFS, typee 2- SSTF, typee 3- SCAN, typee 4-C-SCAN
        int currentTime = 0;
        int currentHeadPosition = STARTING_POINT;
        int totalHeadMovement = 0;

        PriorityQueue<RealTimeRequest> queue = new PriorityQueue<>(Comparator.comparingInt(RealTimeRequest::getDeadline));
        //requests.clear();
        while (!realTimeRequests.isEmpty() || !queue.isEmpty()) {
            // Add real-time requests to the queue that have appeared by the current time
            while (!realTimeRequests.isEmpty() && realTimeRequests.get(0).getAppearingTime() <= currentTime) {
                queue.add(realTimeRequests.remove(0));
            }

            // If there are real-time requests in the queue, process the one with earliest deadline
            if (!queue.isEmpty()) {
                RealTimeRequest request = queue.poll();
                int requestPosition = request.getSector();
                int distance = Math.abs(currentHeadPosition - requestPosition);
                //totalHeadMovement += distance;
                currentTime+=distance;
                currentHeadPosition = requestPosition;
            } else {
//                If there are no real-time requests, process the next request in the list
                int time = realTimeRequests.get(0).getAppearingTime() - currentTime;
                if (typee == 1) {
                    currentHeadPosition = runFCFS(requests, currentHeadPosition, time, false, currentTime);
                } else if (typee == 2) {
                    currentHeadPosition = runSSTF(requests, currentHeadPosition, time, false, currentTime);
                } else if (typee == 3) {
                    currentHeadPosition = runSCAN(requests, currentHeadPosition, time, false, currentTime);
                } else {
                    currentHeadPosition = runCSCAN(requests, currentHeadPosition, time, false, currentTime);
                }
                currentTime += time;
            }
            //currentTime++;
        }
        if (typee == 1) {
            currentHeadPosition = runFCFS(requests, currentHeadPosition, Integer.MAX_VALUE, true, currentTime);
        } else if (typee == 2) {
            currentHeadPosition = runSSTF(requests, currentHeadPosition, Integer.MAX_VALUE, true, currentTime);
        } else if (typee == 3) {
            currentHeadPosition = runSCAN(requests, currentHeadPosition, Integer.MAX_VALUE, true, currentTime);
        } else {
            currentHeadPosition = runCSCAN(requests, currentHeadPosition, Integer.MAX_VALUE, true, currentTime);
        }
        //System.out.println(currentTime);
        return totalHeadMovement;
    }

    //==== MAIN ====
    public static void main(String[] args) {

        ArrayList<Integer> requests = generateRequests();
        List<RealTimeRequest> realTimeRequests = generateRealTime();

        System.out.println("==== WITHOUT REALTIME REQUESTS, start point => " + STARTING_POINT + " ====");
        System.out.println("---- FCFS algorithm ----");
        runFCFS(copyRequests(requests), STARTING_POINT, Integer.MAX_VALUE, true, 0);
        System.out.println("---- SSTF algorithm ----");
        runSSTF(copyRequests(requests), STARTING_POINT, Integer.MAX_VALUE, true, 0);
        System.out.println("---- SCAN algorithm ----");
        runSCAN(copyRequests(requests), STARTING_POINT, Integer.MAX_VALUE, true, 0);
        System.out.println("---- C-SCAN algorithm ----");
        runCSCAN(copyRequests(requests), STARTING_POINT, Integer.MAX_VALUE, true, 0);
        System.out.println("==== EDF algorithm ====");
        System.out.println("---- WITH FCFS ----");
        runEDF(copyRequests(requests), copyRealTime(realTimeRequests), 1);
        System.out.println("---- WITH SSTF ----");
        runEDF(copyRequests(requests), copyRealTime(realTimeRequests), 2);
        System.out.println("---- WITH SCAN ----");
        runEDF(copyRequests(requests), copyRealTime(realTimeRequests), 3);
        System.out.println("---- WITH C-SCAN ----");
        runEDF(copyRequests(requests), copyRealTime(realTimeRequests), 4);
    }
}