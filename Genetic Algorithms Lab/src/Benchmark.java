public class Benchmark {
    static double mutRate =0.05;
    static int partCount = 2;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        Sequence seq = new Sequence(Examples.SEQ50);
        Population pop = new Population(seq, 4000);
        pop.rate();
        for (int g = 0; g<15; g++) {
            mutRate =0.05;
            partCount = 2;
            for (int i = 0; i < 200; i++) {
                if (i % 100 == 0 && partCount < 6) {
                    partCount++;
                }
                if (mutRate < 0.12)
                    mutRate = mutRate + 0.0006;
                doTournamentStrategy(pop);
            }
            pop.flushLog();
            seq = new Sequence(Examples.SEQ50);
            Population pop2 = new Population(seq, 4000);
            pop2.rate();
            mutRate =0.05;
            partCount = 2;
            for (int v = 0; v < 200; v++) {
                if (v % 100 == 0 && partCount < 6) {
                    partCount++;
                }
                if (mutRate < 0.12)
                    mutRate = mutRate + 0.0006;
                doProportSelectionStrategy(pop2);
            }
            pop2.flushLog();
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println(elapsedTime);
    }
    private static void doProportSelectionStrategy(Population pop){
        doMutation(pop);
        pop.select();
    }

    private static void doTournamentStrategy(Population pop){
        doMutation(pop);
        pop.turnamentSelection(partCount, 0.01);
    }
    private static void doMutation(Population pop) {
        pop.setMutationRate(mutRate);
        pop.setCrossOverRate(mutRate);
        pop.doPointMutation();
        pop.doCrossOver();
        pop.rate();
        pop.log();
    }
}