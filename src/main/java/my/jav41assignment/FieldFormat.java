package my.jav41assignment;

import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class FieldFormat {
    private int value;
    private Paint color;
    public FieldFormat(int value){
        this.value = value;
        this.calculateColor();
    }

    private void calculateColor() {
       this.color =  switch (this.value){
            case 2 -> Color.BLUE;
            case 4 -> Color.RED;
            case 8 -> Color.ALICEBLUE;
            case 16 -> Color.BEIGE;
            case 32 -> Color.BROWN;
            case 64 -> Color.YELLOW;
            case 128 -> Color.ROSYBROWN;
            case 256 -> Color.PALEGREEN;
            case 512 -> Color.ORANGE;
            case 1024 -> Color.FIREBRICK;
            case 2048 -> Color.GREEN;
           default -> Color.WHITE;
        };
    }

    public Paint getColor(){
        return this.color;
    }
}
