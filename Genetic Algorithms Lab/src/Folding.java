import java.util.ArrayList;
import java.util.Random;

public class Folding {
    private Sequence sequence;
    private DirectionControl directionControl;
    public double getRating() {
        return Rating;
    }

    public void setRating(double lastRating) {
        this.Rating = lastRating;
    }

    private double Rating;

    public int getHhBonds() {
        return hhBonds;
    }

    public void setHhBonds(int hhBonds) {
        this.hhBonds = hhBonds;
    }

    public int getOverlappings() {
        return overlappings;
    }

    public void setOverlappings(int overlappings) {
        this.overlappings = overlappings;
    }

    private int hhBonds,overlappings;

    public double getLastChance() {
        return lastChance;
    }

    public void setLastChance(double lastChance) {
        this.lastChance = lastChance;
    }

    private double lastChance;
    public ArrayList<FoldDirection> getFolding() {
        return folding;
    }

    private ArrayList<FoldDirection> folding = new ArrayList<FoldDirection>();

    public Folding(Sequence seq){
        sequence = seq;
        this.Rating = -1;
        for (char character: seq.getSequence().toCharArray()) {
            if (character == '0' || character == '1') {
                folding.add(FoldDirection.strait);
            }
            else {
                System.out.println("Invalid Char in sequence: " + seq);
            }
        }
    }

    public Folding(Folding orig){
        this.sequence = orig.getSequence();
        this.folding = new ArrayList<FoldDirection>();
        for (FoldDirection element:orig.getFolding()) {
            this.folding.add(element);
        }
        this.Rating = orig.Rating;
        this.setOverlappings(orig.getOverlappings());
        this.setHhBonds(orig.getHhBonds());
    }

    public int length(){
        return folding.size();
    }

    public Grid getGridRepresentation(){
        return new Grid(this);
    }

    public FoldDirection getFoldingAtPosition(int index) {
        return folding.get(index);
    }

    public Sequence getSequence() {
        return sequence;
    }

    public void foldAt(int index, FoldDirection direction){
        folding.set(index, direction);
    }
    public Folding randomize(){
        directionControl = new DirectionControl();
        FoldDirection cur;
        ArrayList<Tuple> positions = new ArrayList<>();
        Tuple last = new Tuple(0,0);
        positions.add(last);
        for (int i = 0; i < sequence.getSequence().length(); i++ ){
            cur = getRandomDirection();
            directionControl.rotate(cur);
            if (!directionValid(positions, cur, last)){
                cur = FoldDirection.left;
                directionControl.rotate(cur);
                if (!directionValid(positions, cur, last)){
                    cur = FoldDirection.right;
                    directionControl.rotate(cur);
                    if (!directionValid(positions, cur, last)){
                        cur = FoldDirection.strait;
                        directionControl.rotate(cur);
                    }
                }
            }
            last.x = last.x + directionControl.getDirectionVector().x;
            last.y = last.y + directionControl.getDirectionVector().y;
            positions.add(last);
            foldAt(i, cur);
        }
        return this;
    }
    private FoldDirection getRandomDirection(){
        Random random = new Random();
        switch (random.nextInt(4)){
            case 1: return FoldDirection.right;
            case 2: return FoldDirection.left;
            default: return FoldDirection.strait;
        }
    }
    private boolean directionValid(ArrayList<Tuple> positions, FoldDirection direction, Tuple last){
        Tuple dirVector = directionControl.getDirectionVector();
        Tuple candidate = new Tuple(last.x + dirVector.x, last.y+ dirVector.y);
        if (positions.contains(candidate)){
            return false;
        }
        return true;
    }
}
