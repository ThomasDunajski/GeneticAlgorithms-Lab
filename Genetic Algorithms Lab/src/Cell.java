import java.util.ArrayList;

public class Cell {
    private int xPos,yPos;
    private Grid grid;
    public Cell(Grid grid, int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
        this.grid = grid;
    }
    private ArrayList<CellContent> content = new ArrayList<CellContent>();

    public void add(CellContent value){
        content.add(value);
    }
    public int getElementCount(){
        return content.size();
    }

    public ArrayList<CellContent> getContent(){
        return content;
    }


    public ArrayList<Cell> getNeigbours(){
        ArrayList<Cell> result = new ArrayList<>();
        //left
        if (validCell(this.xPos - 1, this.yPos)){
            result.add(grid.getCells()[xPos - 1][yPos]);
        }
        //right
        if (validCell(this.xPos + 1, this.yPos)){
            result.add(grid.getCells()[xPos +1][yPos]);
        }
        //top
        if (validCell(this.xPos, this.yPos + 1)){
            result.add(grid.getCells()[xPos][yPos + 1]);
        }
        //bot
        if (validCell(this.xPos , this.yPos - 1)){
            result.add(grid.getCells()[xPos][yPos - 1]);
        }
        return result;
    }
    private boolean validCell(int x, int y){
        if (x > this.grid.getDimenstions() || y > this.grid.getDimenstions() ||x < 0 || y < 0){
            return false;
        }
        return true;
    }
}
