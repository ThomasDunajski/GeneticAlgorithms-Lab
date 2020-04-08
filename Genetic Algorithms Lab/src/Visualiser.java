import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Visualiser {

    private Graphics2D g2;
    private int paddingX = 0;
    private int paddingY = 130;
    private int cellSize = 60;
    private int distance = 10;
    private int border = 3;
    private int height = 1000;
    private int width = 1000;
    private BufferedImage image;
    private int smalestX=1000,smalestY=1000,biggestX=-1000,biggestY=-1000;

    public Visualiser(){
    }
    private void getDimensions(Cell[][] cells){
        for (int i = 0; i < cells.length; i++){
            for (int j = 0; j < cells.length; j++){
                if (cells[i][j] != null){
                    if (i < smalestX) smalestX = i;
                    if (i > biggestX) biggestX = i;
                    if (j < smalestY) smalestY = j;
                    if (j > biggestY) biggestY = j;
                }
            }
        }
        biggestX++;
    }
    private void paintCell(int x, int y){
        g2.fillRect(paddingX + ( cellSize + distance)*(x - smalestX), paddingY + ( cellSize + distance)*(y-smalestY), cellSize, cellSize);
    }

    private void paintCellBlack(int x, int y){
        g2.setColor(Color.black);
        paintCell(x, y);
    }
    private void paintCellWhite(int x, int y){
        g2.setColor(Color.black);
        paintCell(x, y);
        g2.setColor(Color.white);
        g2.fillRect(paddingX + ( cellSize + distance)*(x - smalestX) + border, paddingY + ( cellSize + distance)*(y-smalestY) + border, cellSize - border * 2, cellSize - border * 2);
    }

    private void paintConnection(int x, int y, int x2, int y2){
        g2.setColor(Color.black);
        g2.drawLine(paddingX + (cellSize + distance) * (x - smalestX) + cellSize/2, paddingY + ( cellSize + distance) * (y-smalestY)  + cellSize/2,paddingX + (cellSize + distance) * (x2 - smalestX) + cellSize/2, paddingY + ( cellSize + distance) * (y2-smalestY)  + cellSize/2);
    }

    private void labelCell(String labelString, int x, int y){
        Font font = new Font("Serif", Font.PLAIN, cellSize / 2);
        g2.setFont(font);
        g2.setColor(new Color(127, 55, 85));
        FontMetrics metrics = g2.getFontMetrics();
        int ascent = metrics.getAscent();
        int labelWidth = metrics.stringWidth(labelString);
        g2.drawString(labelString,paddingX + ( cellSize + distance)*(x - smalestX) + cellSize/2 - labelWidth / 2, paddingY + ( cellSize + distance)*(y-smalestY) + cellSize/2 + ascent / 4);
    }
    private String getCellLabel(Cell cell){
        String result = "";
        for (CellContent content :cell.getContent()){
            if (result != ""){
                result += "/";
            }
            result += Integer.toString(content.getNumber());
        }
        return result;
    }
    private void paintStats(Folding folding){
        Font font = new Font("Serif", Font.PLAIN, 25);
        g2.setFont(font);
        g2.setColor(new Color(127, 55, 85));
        FontMetrics metrics = g2.getFontMetrics();
        int ascent = metrics.getAscent();
        g2.drawString("HHBonds: " +Integer.toString(folding.getHhBonds()), 0, ascent);
        g2.drawString("Overlappings: " + Integer.toString(folding.getOverlappings()), 0, ascent * 2);
        g2.drawString("Rating: " + Double.toString(folding.getRating()), 0, ascent * 3);
    }
    public void paintGrid(Folding folding){
        Grid grid = folding.getGridRepresentation();
        Cell[][] cells = grid.getCells();
        getDimensions(cells);
        this.height = 200 + (biggestY - smalestY) * (cellSize +distance +5);
        this.width =(biggestX - smalestX) * (cellSize +distance +5);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        Tuple curPos = null, lastPos=  null;
        for (Tuple pos:grid.getConnections()) {
            lastPos = curPos;
            curPos = pos;
            if (lastPos != null){
                paintConnection(curPos.x, curPos.y, lastPos.x, lastPos.y);
            }
        }
        for (int i = 0; i < cells.length;i++){
            for (int j = 0; j < cells.length;j++){
                if (cells[i][j] != null){
                    if(cells[i][j].getContent().get(0).getValue() == 1){
                        paintCellBlack(i, j);
                    }
                    else{
                        paintCellWhite(i, j);
                    }
                    labelCell(getCellLabel(cells[i][j]), i, j);
                }
            }
        }
        paintStats(folding);

        String folder = "C:\\Users\\Thomas Dunajski\\Documents\\Genetic Algorithms Lab";
        String filename = "bild.png";
        if (new File(folder).exists() == false) new File(folder).mkdirs();

        try {
            ImageIO.write(image, "png", new File(folder + File.separator + filename));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
