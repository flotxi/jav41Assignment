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

    public Board(Randomizer randomizer) {
        gameWonListeners = new ArrayList<>();
        gameLostListeners = new ArrayList<>();
        this.randomizer = randomizer;
        initializeFields();
        addNewField();
        addNewField();
    }

    public void attachGameWonEventListener(GameWonListener eventListener) {
        this.gameWonListeners.add(eventListener);
    }

    public void attachGameLostEventListener(GameLostListener eventListener) {
        this.gameLostListeners.add(eventListener);
    }

    private void initializeFields() {
        for (int row = 0; row < gameSize; row++) {
            for (int column = 0; column < gameSize; column++) {
                fields[row][column] = new Field(0);
            }
        }
    }

    private boolean addNewField() {
        FieldCoordinates fieldCoordinates;
        int tries = 0;
        int maxTries = Board.gameSize * Board.gameSize;
        do {
            fieldCoordinates = randomizer.getNextFieldCoordinates();
            tries++;
            if (tries == maxTries) {
                break;
            }
        }
        while (fields[fieldCoordinates.row()][fieldCoordinates.column()].getValue() != 0);

        if (fields[fieldCoordinates.row()][fieldCoordinates.column()].getValue() == 0) {
            fields[fieldCoordinates.row()][fieldCoordinates.column()].setValue(this.randomizer.getNextNumber());
            return true;
        } else {
            return false;
        }
    }

    public Integer move(KeyCode key) {
        var fieldsGotMoved = tryMovement(key, false);

        if (fieldsGotMoved) {
            addNewField();
        }

        if (isGameWon()) {
            gameWonListeners.forEach(GameWonListener::onGameWon);
        } else if (isGameOver()) {
            gameLostListeners.forEach(GameLostListener::onGameLost);
            score = 0;
        }
        return score;
    }

    private boolean tryMovement(KeyCode key, boolean justSimulate) {
        boolean fieldsGotMoved = false;
        var movement = new Movement(key);
        for (int i = 0; i < gameSize; i++) {
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
                            if (justSimulate == true) {
                                return fieldsGotMoved;
                            }
                            field.setValue(neighbour.getValue() + field.getValue());
                            score = score + neighbour.getValue();
                            neighbour.setValue(0);
                        }

                    } catch (ArrayIndexOutOfBoundsException e) {
                        // do nothing
                    }

                    columnCount = columnCount + movement.getDirection();

                } while (!columnCount.equals(movement.getEndPoint()));

                rowCount = rowCount + movement.getDirection();

            } while (!rowCount.equals(movement.getEndPoint()));
        }
        return fieldsGotMoved;
    }

    // Game is only over when the board filled is and there is no more movement possible
    private boolean isGameOver() {
        if (isValueInFields(0)) return false;
        if (isAnyMovementPossible()) return false;
        return true;
    }

    private boolean isAnyMovementPossible() {
        return  tryMovement(KeyCode.LEFT, true) |
                tryMovement(KeyCode.UP, true) |
                tryMovement(KeyCode.DOWN, true) |
                tryMovement(KeyCode.RIGHT, true);
    }

    private boolean isValueInFields(int value) {
        for (var row = 0; row < gameSize; row++) {
            for (var column = 0; column < gameSize; column++) {
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
