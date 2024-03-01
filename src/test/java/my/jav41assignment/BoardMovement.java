package my.jav41assignment;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

record FieldCoordinatesWithValue(Integer rowToCheck, Integer columnToCheck, Integer expectedFieldValue) {
}

class RandomizerMock implements Randomizer {

    public FieldCoordinatesWithValue[] mockFields = new FieldCoordinatesWithValue[Board.gameSize * Board.gameSize];

    private Integer currentIndex = 0;

    @Override
    public Integer getNextNumber() {
        Integer result;
        try{
            result = mockFields[currentIndex].expectedFieldValue();
            currentIndex++;
        }catch (ArrayIndexOutOfBoundsException e){
            result = 0;
        }

        return result;
    }

    @Override
    public FieldCoordinates getNextFieldCoordinates() {
        try {
            var coordinatesWithValues = mockFields[currentIndex];
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
        for (int i = 0; i < fieldCoordinatesWithValue.length; i++) {
            Assertions.assertEquals(fieldCoordinatesWithValue[i].expectedFieldValue().toString(),
                    fields[fieldCoordinatesWithValue[i].rowToCheck()][fieldCoordinatesWithValue[i].columnToCheck()].getText());

        }
    }
    protected void initializeBoardWithMocker(FieldCoordinatesWithValue... fieldCoordinatesWithValue) {
        mock = new RandomizerMock();
        mock.mockFields = fieldCoordinatesWithValue;
        board = new Board(mock);
    }
}

class BoardMovement extends TestHelper {

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

class BoardRules extends TestHelper {

    @Test
    void game_should_be_over() {
        initializeBoardWithMocker(new FieldCoordinatesWithValue(0, 0, 2),
                new FieldCoordinatesWithValue(1, 1, 4));

        board.move(KeyCode.DOWN);

        fieldsAreEqualTo(new FieldCoordinatesWithValue(end, 0, 2),
                new FieldCoordinatesWithValue(end, 1, 4));
    }


}
