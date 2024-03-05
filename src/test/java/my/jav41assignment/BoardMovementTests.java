package my.jav41assignment;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class BoardMovementTests extends TestHelper {

    @BeforeEach
    void setUp() {
        initializeBoardWithMocker(new FieldCoordinatesWithValue(0, 0, 2),
                new FieldCoordinatesWithValue(1, 1, 4));
    }

    @Test
    void board_should_be_initialized_with_2_values() {
        fieldsAreEqualTo(new FieldCoordinatesWithValue(0, 0, 2),
                new FieldCoordinatesWithValue(1, 1, 4));
    }


    @Test
    void board_should_be_moved_right() {
        board.move(KeyCode.RIGHT);

        fieldsAreEqualTo(new FieldCoordinatesWithValue(0, end, 2),
                new FieldCoordinatesWithValue(1, end, 4));
    }

    @Test
    void board_should_be_moved_left() {
        board.move(KeyCode.LEFT);

        fieldsAreEqualTo(new FieldCoordinatesWithValue(0, 0, 2),
                new FieldCoordinatesWithValue(1, 0, 4));
    }

    @Test
    void board_should_be_moved_up() {
        board.move(KeyCode.UP);

        fieldsAreEqualTo(new FieldCoordinatesWithValue(0, 0, 2),
                new FieldCoordinatesWithValue(0, 1, 4));
    }

    @Test
    void board_should_be_moved_down() {
        board.move(KeyCode.DOWN);

        fieldsAreEqualTo(new FieldCoordinatesWithValue(end, 0, 2),
                new FieldCoordinatesWithValue(end, 1, 4));
    }

}

class BoardRulesTests extends TestHelper {

    @Test
    void game_should_be_over() {
        fillBoardCompletely(new FieldCoordinatesWithValue(0, 3, 2));
        mock.currentIndex = mock.currentNumberIndex = 15;
        final Boolean[] gameIsLost = hasEventHappened();

        board.move(KeyCode.DOWN);

        Assertions.assertTrue(gameIsLost[1]);

    }
    @Test
    void game_should_be_won() {
        fillBoardCompletely(new FieldCoordinatesWithValue(0, 3, 2048));
        mock.currentIndex = mock.currentNumberIndex = 15;
        final Boolean[] gameIsWon = hasEventHappened();

        board.move(KeyCode.DOWN);

        Assertions.assertTrue(gameIsWon[0]);
    }

    private Boolean[] hasEventHappened() {
        final Boolean[] result = {false, false};
        board.attachGameWonEventListener(() -> result[0] = true);
        board.attachGameLostEventListener(() -> result[1] = true);
        return result;
    }

    private void fillBoardCompletely(FieldCoordinatesWithValue lastField) {
        initializeBoardWithMocker(
                new FieldCoordinatesWithValue(0, 0, 2),
                new FieldCoordinatesWithValue(0, 1, 4),
                new FieldCoordinatesWithValue(0, 2, 8),
                new FieldCoordinatesWithValue(0, 3, 16),
                new FieldCoordinatesWithValue(1, 0, 32),
                new FieldCoordinatesWithValue(1, 1, 64),
                new FieldCoordinatesWithValue(1, 2, 128),
                new FieldCoordinatesWithValue(1, 3, 64),
                new FieldCoordinatesWithValue(2, 0, 2),
                new FieldCoordinatesWithValue(2, 1, 4),
                new FieldCoordinatesWithValue(2, 2, 8),
                new FieldCoordinatesWithValue(2, 3, 16),
                new FieldCoordinatesWithValue(3, 0, 32),
                new FieldCoordinatesWithValue(3, 1, 64),
                new FieldCoordinatesWithValue(3, 2, 128),
                lastField);

        Method addNewField;
        try {
            addNewField = Board.class.getDeclaredMethod("addNewField");
            addNewField.setAccessible(true);
            addNewField.invoke(board);
            addNewField.invoke(board);
            addNewField.invoke(board);
            addNewField.invoke(board);
            addNewField.invoke(board);
            addNewField.invoke(board);
            addNewField.invoke(board);
            addNewField.invoke(board);
            addNewField.invoke(board);
            addNewField.invoke(board);
            addNewField.invoke(board);
            addNewField.invoke(board);
            addNewField.invoke(board);
            addNewField.invoke(board);
            addNewField.invoke(board);
            addNewField.invoke(board);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}


record FieldCoordinatesWithValue(Integer rowToCheck, Integer columnToCheck, Integer expectedFieldValue) {
}

class RandomizerMock implements Randomizer {

    public FieldCoordinatesWithValue[] mockFields = new FieldCoordinatesWithValue[Board.gameSize * Board.gameSize];

    public Integer currentIndex = 0;
    public Integer currentNumberIndex = 0;

    @Override
    public Integer getNextNumber() {
        Integer result;
        try{
            result = mockFields[currentNumberIndex].expectedFieldValue();
            currentNumberIndex++;
            if (currentNumberIndex == ( Board.gameSize * Board.gameSize - 1 ) ){
                currentNumberIndex = 0;
            }
        }catch (ArrayIndexOutOfBoundsException e){
            result = 0;
        }

        return result;
    }

    @Override
    public FieldCoordinates getNextFieldCoordinates() {
        try {
            var coordinatesWithValues = mockFields[currentIndex];
            currentIndex++;
            if (currentIndex == ( Board.gameSize * Board.gameSize - 1 ) ){
                currentIndex = 0;
            }
            return new FieldCoordinates(coordinatesWithValues.rowToCheck(), coordinatesWithValues.columnToCheck());
        } catch(ArrayIndexOutOfBoundsException e){
            return new FieldCoordinates(2,2);   // fallback
        }
    }
}

class TestHelper {
    int end = Board.gameSize - 1;
    Board board;
    RandomizerMock mock;

    protected void fieldsAreEqualTo(FieldCoordinatesWithValue... fieldCoordinatesWithValue) {
        var fields = board.getFields();
        for (FieldCoordinatesWithValue coordinatesWithValue : fieldCoordinatesWithValue) {
            Assertions.assertEquals(coordinatesWithValue.expectedFieldValue().toString(),
                    fields[coordinatesWithValue.rowToCheck()][coordinatesWithValue.columnToCheck()].getText());

        }
    }
    protected void initializeBoardWithMocker(FieldCoordinatesWithValue... fieldCoordinatesWithValue) {
        mock = new RandomizerMock();
        mock.mockFields = fieldCoordinatesWithValue;
        board = new Board(mock);
    }

}