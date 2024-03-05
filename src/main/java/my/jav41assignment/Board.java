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
    private final List<GameWonListener> gameWonListeners;
    private final List<GameLostListener> gameLostListeners;

    public Board(Randomizer randomizer){
        gameWonListeners = new ArrayList<>();
        gameLostListeners = new ArrayList<>();
        this.randomizer = randomizer;
        initializeFields();
        addNewField();
        addNewField();
    }
    public void attachGameWonEventListener(GameWonListener eventListener)
    {
        this.gameWonListeners.add(eventListener);
    }
    public void attachGameLostEventListener(GameLostListener eventListener)
    {
        this.gameLostListeners.add(eventListener);
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
                        var neighbour = fields[rowCount + movement.getNeighbour().row()]
                                            [columnCount + movement.getNeighbour().column()];
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

                }while(!columnCount.equals(movement.getEndPoint()));

                rowCount = rowCount + movement.getDirection();

            }while(!rowCount.equals(movement.getEndPoint()));
        }
        if(fieldsGotMoved) {
            addNewField();

            if (isGameWon()){
                gameWonListeners.forEach(GameWonListener::onGameWon);
            }
        }else{
            if(isGameOver()) {
                gameLostListeners.forEach(GameLostListener::onGameLost);
            }
        }
        return score;
    }

    private boolean isGameOver() {
        return !isValueInFields(0);
    }

    private boolean isValueInFields(int value) {
        for( var row = 0; row < gameSize; row++){
            for( var column = 0; column < gameSize; column++){
                if (fields[row][column].getValue() == value) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isGameWon() {
        return isValueInFields(gameEndValue);
    }

    public Field[][] getFields(){
        return this.fields;
    }
}
