public class DirectionControl {
    public enum Direction {
        TOP, RIGHT, BOT, LEFT
    }

    private Direction direction = Direction.RIGHT;

    public Direction getDirection() {
        return direction;
    }
    public Tuple getDirectionVector(){
        switch (direction){
            case TOP: return new Tuple(0,1);
            case RIGHT: return new Tuple(1,0);
            case BOT: return new Tuple(0,-1);
            case LEFT: return new Tuple(-1, 0);
            default: return new Tuple(0, 0);
        }
    }

    public void setDirection(Direction value){
        this.direction = value;
    }

    public void rotate(FoldDirection rotation){
        if (rotation == FoldDirection.right){
            switch (direction){
                case TOP: direction = Direction.RIGHT;
                    break;
                case RIGHT: direction = Direction.BOT;
                    break;

                case BOT: direction = Direction.LEFT;
                    break;
                case LEFT: direction = Direction.TOP;
                    break;
            }
        }
        if (rotation == FoldDirection.left){
            switch (direction){
                case TOP: direction = Direction.LEFT;
                    break;
                case RIGHT: direction = Direction.TOP;
                    break;
                case BOT: direction = Direction.RIGHT;
                    break;
                case LEFT: direction = Direction.BOT;
                    break;
            }
        }
    }
}
