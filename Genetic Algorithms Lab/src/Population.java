import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {
    private ArrayList<Folding> population;
    private double averageFitness;
    private int generationNumber;
    private Folding best;
    private double totalFitness;
    private FileWriter fw;
    private PrintWriter pr;

    public void setMutationRate(double mutationRate) {
        MutationRate = mutationRate;
    }

    public void setCrossOverRate(double crossOverRate) {
        CrossOverRate = crossOverRate;
    }

    private double MutationRate = 0.01;
    private double CrossOverRate = 0.01;
    private final double Cmult = 1.5;

    public Population(Sequence seq, int populationSize){
        population = new ArrayList();
        for (int i = 0; i < populationSize; i++ ){
            population.add(new Folding(seq).randomize());
        }
    }
    public void rate(){
        FitnessFunction fitness = new FitnessFunction();
        totalFitness = 0;
        double curFitness;
        if (best == null ){
            best = new Folding(population.get(0));
        }
        for (Folding folding: this.population) {
            // if invalid rating
            if (folding.getRating() == -1){
                folding.setRating(fitness.rate(folding));
            }
            curFitness = folding.getRating();
            totalFitness += curFitness;
            if (this.best.getRating() < curFitness){
                this.best = new Folding(folding);
            }
        }
        averageFitness = totalFitness / population.size();
    }

    public void log(){
        String logFile = "C:\\Users\\Thomas Dunajski\\Documents\\Genetic Algorithms Lab\\log.csv";
        File f = new File(logFile);
        //if (!f.exists()){
            try {
                f.createNewFile();
            }
            catch (IOException e){
                // TODO catch exception
        //    }
        }
        if (pr == null) {
            try {
                fw = new FileWriter(logFile, true); //the true will append the new data
                pr = new PrintWriter(fw);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        pr.format("%d\t%.16f\t%f\t%f\t%d\t%d%n", this.generationNumber, this.averageFitness, this.best.getRating(), this.best.getRating(), this.best.getHhBonds(), this.best.getOverlappings());
    }

    public void flushLog(){
        try {
            pr.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void doPointMutation(){
        int mutatoinAmount = Math.max(1, (int)(population.size()*MutationRate));
        Random random = new Random();
        Folding fold;
        int direction;
        for (int i = 0; i < mutatoinAmount; i++){
            fold = population.get(random.nextInt(population.size()));
            fold.setRating(-1);
            direction = random.nextInt(3);
            switch (direction){
                case 0: fold.foldAt(random.nextInt(fold.length()), FoldDirection.right);
                case 1: fold.foldAt(random.nextInt(fold.length()), FoldDirection.left);
                case 2: fold.foldAt(random.nextInt(fold.length()), FoldDirection.strait);
            }
        }
    }
    public void doCrossOver(){
        int crossOverAmount = Math.max(1, (int)(population.size() * CrossOverRate));
        Random random = new Random();
        FoldDirection swap;
        int first,second;
        int foldPositon;
        for (int i = 0; i < crossOverAmount; i++){
            first = random.nextInt(population.size());
            second = random.nextInt(population.size());
            population.get(first).setRating(-1);
            population.get(second).setRating(-1);
            foldPositon = random.nextInt(population.get(first).length());
            for (int j = foldPositon; j< population.get(first).length(); j++){
                swap = population.get(first).getFoldingAtPosition(j);
                population.get(first).foldAt(j, population.get(second).getFoldingAtPosition(j));
                population.get(second).foldAt(j, swap);
            }
        }
    }
    public void select(){
        this.generationNumber++;
        ArrayList<Folding> newPopulation = new ArrayList<>();
        newPopulation.add(this.best);
        int populaionSize = population.size();
        double delta = best.getRating() - averageFitness;
        double a = (Cmult - 1) * averageFitness / delta;
        double b = averageFitness * (best.getRating() - Cmult * averageFitness) / delta;
        for (Folding fold: population) {
            fold.setLastChance(b + fold.getRating() * a);
        }
        Random random = new Random();
        ArrayList<Integer> indexes = new ArrayList();
         for (int i = 0; i < populaionSize -1; i++){
            double index = random.nextDouble() * totalFitness;
            double sum = 0;
            int cur = 0;
            while(sum < index ) {
                sum = sum + population.get(cur++).getLastChance();
            }
            Folding temp = population.get(Math.max(0,cur-1));
            newPopulation.add(new Folding(population.get(Math.max(0,cur-1))));
            indexes.add(Math.max(0,cur-1));
        }
        this.population = newPopulation;
    }

    public void turnamentSelection(int participantCount, double luckyLooserChance){
        this.generationNumber++;
        ArrayList<Folding> newPopulation = new ArrayList<>();
        newPopulation.add(this.best);
        Sorter sorter = new Sorter();
        int populationSize = population.size();
        for (int j =0; j < populationSize -1; j++){
            List<Folding> participants = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i<participantCount; i++) {
                participants.add(population.get(random.nextInt(populationSize)));
            }
            participants.sort(sorter);
            Folding winer;
            if (random.nextDouble() < luckyLooserChance){
                winer = new Folding(participants.get(participants.size() -1));
            }
            else {
                winer = new Folding(participants.get(0));
            }
            newPopulation.add(winer);
        }
        this.population = newPopulation;
    }

    public Folding getBest(){
        return best;
    }
}
