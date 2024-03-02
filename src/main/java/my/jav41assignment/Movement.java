package my.jav41assignment;

import javafx.scene.input.KeyCode;

public class Movement {

    private KeyCode key;
    public Movement(KeyCode key){
        this.key = key;
    }
    public Integer getStartPoint(){
        return switch (key){
            case KeyCode.LEFT, KeyCode.DOWN  -> Board.gameSize - 1;
            case KeyCode.UP, KeyCode.RIGHT-> 0;
            default -> throw new RuntimeException() ;
        };
    }
    public Integer getEndPoint(){
        return switch (key){
            case KeyCode.LEFT, KeyCode.DOWN -> -1;
            case  KeyCode.UP,KeyCode.RIGHT -> Board.gameSize ;
            default -> throw new RuntimeException() ;
        };
    }
    public Integer getDirection(){
        return switch (key){
            case KeyCode.LEFT,  KeyCode.DOWN -> - 1;
            case KeyCode.UP, KeyCode.RIGHT -> 1;
            default -> throw new RuntimeException() ;
        };
    }
}
