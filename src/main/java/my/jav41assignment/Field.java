package my.jav41assignment;

import javafx.scene.paint.Paint;

public class Field {
    private Integer value;

    public Field( Integer startValue ){
       value = startValue;

    }
    public void setValue(Integer value){
        this.value = value;
    }
    public String getText(){
        return value == 0 ? "" : value.toString();
    }

    public Paint getColor(){
        return new FieldFormat(this.value).getColor();
    }

    public void move(){

    }
}
