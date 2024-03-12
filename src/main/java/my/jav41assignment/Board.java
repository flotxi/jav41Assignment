package my.jav41assignment;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final Integer GAME_SIZE = 4;
    private Integer gameEndValue = 2048;
    private final Randomizer randomizer;
    private final Field[][] fields = new Field[GAME_SIZE][GAME_SIZE];
    private Integer score = 0;
    private final List<GameWonListener> gameWonListeners;
    private final List<GameLostListener> gameLostListeners;

    public Board(Randomizer randomizer) {
        gameWonListeners = new ArrayList<>();
        gameLostListeners = new ArrayList<>();
        this.randomizer = randomizer;
        initializeFields();
        addNewField();
        addNewField();
    }
    public Integer getScore() {
        return score;
    }
    public void keepPlaying() {
        gameEndValue = gameEndValue * 2;
    }
    public void attachGameWonEventListener(GameWonListener eventListener) {
        this.gameWonListeners.add(eventListener);
    }

    public void attachGameLostEventListener(GameLostListener eventListener) {
        this.gameLostListeners.add(eventListener);
    }

    private void initializeFields() {
        for (int row = 0; row < GAME_SIZE; row++) {
            for (int column = 0; column < GAME_SIZE; column++) {
                fields[row][column] = new Field(0);
            }
        }
    }

    private void addNewField() {
        FieldCoordinates fieldCoordinates;
        int tries = 0;
        int maxTries = Board.GAME_SIZE * Board.GAME_SIZE;
        do {
            fieldCoordinates = randomizer.getNextFieldCoordinates();
            tries++;
            if (tries == maxTries) {
                fieldCoordinates = justTryToGetOneEmptyField();
                break;
            }
        }
        while (fields[fieldCoordinates.row()][fieldCoordinates.column()].getValue() != 0);

        if (fields[fieldCoordinates.row()][fieldCoordinates.column()].getValue() == 0) {
            fields[fieldCoordinates.row()][fieldCoordinates.column()].setValue(this.randomizer.getNextNumber());
        }
    }

    private FieldCoordinates justTryToGetOneEmptyField() {
        for (var row = 0; row < GAME_SIZE; row++){
            for (var column = 0; column < GAME_SIZE; column++){
               var field = fields[row][column];
               if(field.getValue() == 0){
                   return new FieldCoordinates(row,column);
               }
            }
        }
        return new FieldCoordinates(0,0);
    }


    public void move(KeyCode key) {
        var fieldsGotMoved = tryMovement(key, false);

        if (fieldsGotMoved) {
            addNewField();
        }

        if (isGameWon()) {
            gameWonListeners.forEach(GameWonListener::onGameWon);
        } else if (isGameOver()) {
            score = 0;
            gameLostListeners.forEach(GameLostListener::onGameLost);
        }
    }

    private boolean tryMovement(KeyCode key, boolean justSimulate) {
        boolean fieldsGotMoved = false;
        var movement = new Movement(key);
        for (int i = 0; i < GAME_SIZE; i++) {
            var rowCount = movement.getStartPoint();
            do {
                var columnCount = movement.getStartPoint();
                do {
                    var field = fields[rowCount][columnCount];
                    try {
                        var neighbour = fields[rowCount + movement.getNeighbour().row()]
                                [columnCount + movement.getNeighbour().column()];
                        if (field.getValue().equals(neighbour.getValue()) || field.getValue() == 0) {
                            fieldsGotMoved = true;
                            if (justSimulate) {
                                return true;
                            }
                            field.setValue(neighbour.getValue() + field.getValue());
                            score = score + neighbour.getValue();
                            neighbour.setValue(0);
                        }

                    } catch (ArrayIndexOutOfBoundsException e) {
                        // do nothing
                    }

                    columnCount += movement.getDirection();


                } while (!columnCount.equals(movement.getEndPoint()));

                rowCount += movement.getDirection();

            } while (!rowCount.equals(movement.getEndPoint()));
        }
        return fieldsGotMoved;
    }

    // Game is only over when the board filled is and there is no more movement possible
    private boolean isGameOver() {
        if (isValueInFields(0)) return false;
        return !isAnyMovementPossible();
    }

    private boolean isAnyMovementPossible() {
        return  tryMovement(KeyCode.LEFT, true) ||
                tryMovement(KeyCode.UP, true) ||
                tryMovement(KeyCode.DOWN, true) ||
                tryMovement(KeyCode.RIGHT, true);
    }

    private boolean isValueInFields(int value) {
        for (var row = 0; row < GAME_SIZE; row++) {
            for (var column = 0; column < GAME_SIZE; column++) {
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

    public Field[][] getFields() {
        return this.fields;
    }
}
