import core.CPU;
import core.SingleProcess;

import java.util.ArrayList;

public class Main {
    static int maxExecutionTime = 100;
    static int maxAppearTime = 100;
    static int numberOfProcesses = 1000;
    static int N = 10;
    static double P = 0.7;
    static double R = 0.4;
    static double Z = 20;
    static double maxOneTask = 0.03;

    private static void testowanieStrategii(Simulations symulacja) {
        symulacja.start();
        double sredniLoad = 0.0;
        for (CPU cpu : symulacja.getCpus()) {
            sredniLoad += cpu.getAvgLoad();
        }
        sredniLoad /= symulacja.getCpus().size();
        System.out.println("srednie obciazenie calosci: " + sredniLoad * 100.0);
        double odchylenieSTD = 0.0;
        int iloscRequestow = 0;
        for (CPU cpu : symulacja.getCpus()) {
            odchylenieSTD += Math.pow(cpu.getAvgLoad() - sredniLoad, 2.0);
            iloscRequestow += cpu.getLoadRequests();
        }
        odchylenieSTD = odchylenieSTD/symulacja.getCpus().size();
        odchylenieSTD = Math.sqrt(odchylenieSTD);
        System.out.println("odchylenie standardowe:     "+ odchylenieSTD * 100.0);
        System.out.println("zapytania:                  "+ iloscRequestow);
        System.out.println("migracje:                   "+ symulacja.getMigrationCount());
        System.out.println();
    }

    public static void main(String[] args) {
        SingleProcess.Generate g = new SingleProcess.Generate();
        ArrayList<SingleProcess> generated = g.generateProcessList(N, maxAppearTime, maxExecutionTime, numberOfProcesses, maxOneTask);
        System.out.println("Strategia 1");
        testowanieStrategii(new Simulations.Strategy1(cloneList(generated), N, P, Z));
        System.out.println("Strategia 2");
        testowanieStrategii(new Simulations.Strategy2(cloneList(generated), N, P, Z));
        System.out.println("Strategia 3");
        testowanieStrategii(new Simulations.Strategy3(cloneList(generated), N, P, R));

    }

    private static ArrayList<SingleProcess> cloneList(ArrayList<SingleProcess> generated) {
        ArrayList<SingleProcess> toReturn = new ArrayList<>();
        for (SingleProcess s : generated)
            toReturn.add(s.clone());
        return toReturn;
    }
}
    