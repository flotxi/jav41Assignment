package my.jav41assignment;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class BoardController {
    @FXML
    private Label leftTop;
    @FXML
    private Label left;
    @FXML
    private Label leftBottom;
    @FXML
    private Label middleTop;
    @FXML
    private Label middle;
    @FXML
    private Label middleBottom;
    @FXML
    private Label rightTop;
    @FXML
    private Label right;
    @FXML
    private Label rightBottom;

    private Field[][] Board = new Field[3][3];

    private boolean isBoardInitialized = false;
    public BoardController(){
    }
    @FXML
    protected void onUserInput(KeyEvent keyEvent) {
        if(!this.isBoardInitialized){
            this.initializeBoard();
            return;
        }
        try {
            this.move(keyEvent.getCode());
        } catch ( Exception  wrongKey ){
            // do nothing
        }
    }

    private void move(KeyCode key) {

        var direction = switch (key){
            case KeyCode.LEFT, KeyCode.UP -> "-";
            case KeyCode.RIGHT, KeyCode.DOWN -> "+";
            default -> throw new RuntimeException() ;
        };

        for( var row : this.Board){
            for (var field : row ){
                field.move();
            }
        }
    }

    private void initializeBoard() {
        Board[0][0] = new Field( 2, this.leftTop );
        Board[0][1] = new Field( 0, this.middleTop );
        Board[0][2] = new Field( 0, this.rightTop );
        Board[1][0] = new Field( 0, this.left );
        Board[1][1] = new Field( 0, this.middle );
        Board[1][2] = new Field( 0, this.right );
        Board[2][0] = new Field( 4, this.leftBottom );
        Board[2][1] = new Field( 0, this.middleBottom );
        Board[2][2] = new Field( 0, this.rightBottom );
        this.isBoardInitialized = true;
    }
}