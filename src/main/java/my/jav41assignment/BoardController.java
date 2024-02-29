package my.jav41assignment;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

public class BoardController {
    @FXML
    private VBox vbox;
    private Board board;
    private final Label[][] labels = new Label[Board.gameSize][Board.gameSize];
    @FXML
    void initialize(){
        this.initLabels();
        this.initializeBoard();
        this.updateLabels();
    }

    private void initLabels(){

        var gridPane = new GridPane();

        for( int size = 0; size < Board.gameSize; size++){
            var column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            column.setPercentWidth(100.0 / Board.gameSize);
            gridPane.getColumnConstraints().add(column);

            var row = new RowConstraints();
            row.setPercentHeight(100.0 / Board.gameSize);
            gridPane.getRowConstraints().add(row);
        }


        for( int row = 0; row < Board.gameSize; row++){
            for (int column = 0; column < Board.gameSize; column++ ){
                var label = new Label();
                label.setPrefSize(100,100);
                gridPane.add(label, column, row);
                this.labels[row][column] = label;
            }
        }
        gridPane.setGridLinesVisible(true);
        vbox.getChildren().add(gridPane);

    }

    @FXML
    protected void onUserInput(KeyEvent keyEvent) {
        this.moveBoard(keyEvent);
        this.updateLabels();
    }

    private void updateLabels() {
        var fields = this.board.getFields();

        for( int row = 0; row < Board.gameSize; row++){
            for (int column = 0; column < Board.gameSize; column++ ){
                var field = fields[row][column];
                var label = this.labels[row][column];

                label.setText(field.getText() );
                label.setBackground(new Background(new BackgroundFill(field.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
                label.setMaxWidth(Double.MAX_VALUE);
                label.setAlignment(Pos.CENTER);
            }
        }
    }

    private void moveBoard(KeyEvent keyEvent) {
        try {
            this.board.move(keyEvent.getCode());
        } catch ( Exception  wrongKey ){
            // do nothing
        }
    }


    private void initializeBoard() {
        this.board = new Board( new ValueRandomizer());
    }
}