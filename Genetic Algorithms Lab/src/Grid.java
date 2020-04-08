import java.util.ArrayList;

public class Grid {
    private Cell[][] cells;
    private ArrayList<Tuple> connections = new ArrayList<>();
    private Tuple startPos;
    public Grid(Folding folding){
        cells = new Cell[folding.length()*2][folding.length()*2];
        startPos = new Tuple(folding.length() -1, folding.length() -1);
        Tuple pos = startPos;
        DirectionControl direction = new DirectionControl();
        String seq = folding.getSequence().getSequence();
        for (int i = 0; i < folding.length(); i++){
            if (cells[pos.x][pos.y] == null){
                cells[pos.x][pos.y] = new Cell(this, pos.x,pos.y);
            }
            cells[pos.x][pos.y].add(new CellContent(seq.charAt(i), i));
            connections.add(pos);
            direction.rotate(folding.getFoldingAtPosition(i));
            switch (direction.getDirection()){
                case TOP: pos = new Tuple(pos.x, pos.y + 1);
                    break;
                case RIGHT: pos = new Tuple(pos.x + 1, pos.y);
                    break;
                case BOT: pos = new Tuple(pos.x, pos.y - 1);
                    break;
                case LEFT: pos = new Tuple(pos.x - 1, pos.y);
                    break;
            }
        }
    }
    public Cell[][] getCells() {
        return cells;
    }

    public ArrayList<Tuple> getConnections() {
        return connections;
    }
    public int getDimenstions(){
        return cells.length;
    }
}
