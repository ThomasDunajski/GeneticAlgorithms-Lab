public class Tuple {
    public int x;
    public int y;
    public Tuple(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public boolean equals(Object obj) {
        Tuple object = (Tuple) obj;
        return (this.x == object.x && this.y == object.y);
    }
    @Override
    public int hashCode() {
        return (int)this.x *100000 + (int)this.y;
    }
}