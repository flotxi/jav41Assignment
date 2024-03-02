package my.jav41assignment;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static Integer gameSize = 4;
    public static Integer gameEndValue = 2048;
    private final Randomizer randomizer;
    private final Field[][] fields = new Field[gameSize][gameSize];
    private Integer score = 0;
    private List<GameEventListener> eventListeners;

    public Board(Randomizer randomizer){
        eventListeners = new ArrayList<GameEventListener>();
        this.randomizer = randomizer;
        initializeFields();
        addNewField();
        addNewField();
    }
    public void addGameEventListener(GameEventListener eventListener)
    {
        this.eventListeners.add(eventListener);
    }
    private void initializeFields() {
        for( int row = 0; row < gameSize; row++){
            for (int column = 0; column < gameSize; column++ ){
                fields[row][column] = new Field( 0 );
            }
        }
    }

    private void addNewField() {
        FieldCoordinates fieldCoordinates;
        int tries = 0;
        int maxTries = Board.gameSize * Board.gameSize;
        do{
            fieldCoordinates = randomizer.getNextFieldCoordinates();
            tries++;
            if(tries == maxTries){
                break;
            }
        }
        while (fields[fieldCoordinates.row()][fieldCoordinates.column()].getValue() != 0);

        if (fields[fieldCoordinates.row()][fieldCoordinates.column()].getValue() == 0) {
            fields[fieldCoordinates.row()][fieldCoordinates.column()].setValue(this.randomizer.getNextNumber());
        }
    }

    public Integer move(KeyCode key) {
        var movement = new Movement(key);
        var fieldsGotMoved = false;
        for(int i = 0; i < gameSize; i++){
            var rowCount = movement.getStartPoint();
            do{
                var columnCount = movement.getStartPoint();
                do{
                    var field = fields[rowCount][columnCount];
                    try{
                        var neighbour = switch (key){
                            case KeyCode.LEFT -> fields[rowCount][columnCount + 1];
                            case KeyCode.UP-> fields[rowCount + 1][columnCount];
                            case KeyCode.DOWN -> fields[rowCount - 1][columnCount];
                            case KeyCode.RIGHT -> fields[rowCount][columnCount - 1];
                            default-> throw new RuntimeException() ;
                        };
                        if (field.getValue().equals(neighbour.getValue() )|| field.getValue() == 0){
                            field.setValue(neighbour.getValue() + field.getValue());
                            score = score + neighbour.getValue();
                            neighbour.setValue(0);
                            fieldsGotMoved = true;
                        }

                    }catch (ArrayIndexOutOfBoundsException e){
                    // do nothing
                    }

                    columnCount = columnCount + movement.getDirection();

                }while(columnCount != movement.getEndPoint() );

                rowCount = rowCount + movement.getDirection();

            }while(rowCount != movement.getEndPoint() );
        }
        if(fieldsGotMoved) {
            addNewField();

            if (isGameWon()){
                eventListeners.forEach((listener) -> listener.onGameWon());
            }
        }else{
            if(isGameOver()) {
                eventListeners.forEach((listener) -> listener.onGameLost());
            }
        }
        return score;
    }

    private boolean isGameOver() {
        return false;
    }

    private boolean isGameWon() {
        for( var row = 0; row < gameSize; row++){
            for( var column = 0; column < gameSize; column++){
                if(fields[row][column].getValue().equals(gameEndValue)){
                    return true;
                }
            }
        }
        return false;
    }

    public Field[][] getFields(){
        return this.fields;
    }
}
