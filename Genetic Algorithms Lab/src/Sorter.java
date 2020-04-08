import java.util.Comparator;

public class Sorter implements Comparator<Folding>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(Folding a, Folding b)
    {
        double value = b.getRating() - a.getRating();
        if (value < 0) {
            return -1;
        }
        if (value == 0) {
            return 0;
        }
        return 1;
    }
}

