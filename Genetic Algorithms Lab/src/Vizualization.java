public class Vizualization {
    static double mutRate =0.5;
    static int partCount = 2;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        Sequence seq = new Sequence(Examples.SEQ48);
        Population pop = new Population(seq, 4000);
        pop.rate();
        for (int i = 0; i < 50; i++) {
            if (i % 100 == 0 && partCount < 6) {
                partCount++;
            }
            if (mutRate < 0.12)
                mutRate = mutRate + 0.0006;
            //doProportSelectionStrategy(pop);
            demonstration(pop);
        }
        //pop.flushLog();

        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println(elapsedTime);
        Visualiser visualiser = new Visualiser();
        visualiser.paintGrid(pop.getBest());
    }
    private static void doMutation(Population pop) {
        pop.setMutationRate(mutRate);
        pop.setCrossOverRate(mutRate);
        pop.doPointMutation();
        pop.doCrossOver();
        pop.rate();
        pop.log();
    }

    private static void doProportSelectionStrategy(Population pop){
        doMutation(pop);
        pop.select();
    }

    private static void doTournamentStrategy(Population pop){
        doMutation(pop);
        pop.turnamentSelection(partCount, 0.01);
    }




    private static void demonstration(Population pop){
        pop.setMutationRate(mutRate);
        pop.setCrossOverRate(mutRate);
        pop.doPointMutation();
        pop.doCrossOver();
        pop.rate();
        pop.turnamentSelection(partCount, 0.01);
    }
}