import java.util.HashMap;

public class FitnessFunction {
    private final double reward = 0.1;
    private final double punishment = 0.11;
    private final double minScore = 0.001;
    private HashMap<Tuple, String> positions;
    private int overlappings;
    private int hHBonds;

    public double getScore() {
        return score;
    }

    private double score;

    public double rate(Folding fold){
        Tuple pos;
        positions = new HashMap<>();
        overlappings = 0;
        hHBonds = 0;
        String seq = fold.getSequence().getSequence();
        pos = new Tuple(0, 0);
        DirectionControl direction = new DirectionControl();
        String curNode;
        for (int i = 0; i < fold.length(); i++){
            curNode = ""+ seq.charAt(i);
            if(positions.containsKey(pos)){
                overlappings++;
                if((positions.get(pos) == "1" && curNode == "0") ||(positions.get(pos) == "0" && curNode == "1")){
                    positions.replace(pos, "x");
                }
                else {
                    positions.replace(pos, curNode);
                }
            }
            positions.put(pos, curNode);
            if (curNode.equals("1")) {
                switch (direction.getDirection()) {
                    case RIGHT:
                        hHBonds += checkTopAndBotNeigbour(pos);
                        break;
                    case LEFT:
                        hHBonds += checkTopAndBotNeigbour(pos);
                        break;
                    case TOP:
                        hHBonds += checkLeftAndRightNeigbour(pos);
                        break;
                    case BOT:
                        hHBonds += checkLeftAndRightNeigbour(pos);
                        break;
                }
            }
            direction.rotate(fold.getFoldingAtPosition(i));
            Tuple directionVector = direction.getDirectionVector();
            pos = new Tuple(pos.x + directionVector.x, pos.y + directionVector.y);
        }
        //add a HHbond if ahead of the tail a black cell exists
        if (positions.containsKey(pos) && (positions.get(pos) == "1")){
            hHBonds++;
        }
        fold.setHhBonds(hHBonds);
        fold.setOverlappings(overlappings);
        double overlappingsSquared = (this.overlappings + 1) * (this.overlappings +1);
        double hbondsPlusOne = this.hHBonds + 1;
        this.score = hbondsPlusOne / overlappingsSquared;

        return this.score;
    }

    private boolean exists(Tuple position){
        return positions.containsKey(position);
    }
    private double checkTopAndBotNeigbour(Tuple pos){
        Tuple above = new Tuple(pos.x, pos.y +1);
        double count = 0;
        if(exists(above) && positions.get(above).equals("1")){
            count++;
        }
        Tuple below = new Tuple(pos.x, pos.y -1);
        if(exists(below) && positions.get(below).equals("1")){
            count++;
        }
        return  count;
    }
    private double checkLeftAndRightNeigbour(Tuple pos){
        Tuple right = new Tuple(pos.x +1, pos.y);
        double count = 0;
        if(exists(right) && positions.get(right).equals("1")){
            count++;
        }
        Tuple left = new Tuple(pos.x -1, pos.y);
        if(exists(left) && positions.get(left).equals("1")){
            count++;
        }
        return count;
    }
}
