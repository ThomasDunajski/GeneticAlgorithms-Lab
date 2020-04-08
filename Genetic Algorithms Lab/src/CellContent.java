public class CellContent {
    public int getValue() {
        return value;
    }

    public int getNumber() {
        return number;
    }

    private int value,number;

    public CellContent(char value, int number){
        if (value == '1'){
            this.value = 1;
        }
        if (value == '0'){
            this.value = 0;
        }
        this.number = number;
    }
}
