package my.jav41assignment;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

public class Field {
    private Integer startValue;
    private FieldFormat formatter;
    private Label label;
    public Field( Integer startValue, Label label ){
       this.startValue = startValue;
       this.label = label;

       this.formatter = new FieldFormat(this.startValue);

        this.label.setText(this.startValue == 0 ? "" : this.startValue.toString() );
        this.label.setBackground(new Background(new BackgroundFill(this.formatter.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        this.label.setMaxWidth(Double.MAX_VALUE);
        this.label.setAlignment(Pos.CENTER);
    }

    public void move(){

    }
}
