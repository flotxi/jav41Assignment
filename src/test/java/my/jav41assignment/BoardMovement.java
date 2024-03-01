package my.jav41assignment;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

record FieldCoordinatesWithValue(Integer rowToCheck, Integer columnToCheck, String expectedFieldValue) {
}

class RandomizerMock implements Randomizer {

    public FieldCoordinatesWithValue[] mockFields;

    private Integer callCount = 0;

    @Override
    public Integer getNextNumber() {
        if (callCount == 0) {
            callCount++;
            return 2;
        } else {
            return 4;
        }
    }

    @Override
    public FieldCoordinates getNextFieldCoordinates() {
        if (callCount == 0) {
            return new FieldCoordinates(0, 0);
        } else {
            return new FieldCoordinates(1, 1);
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
            Assertions.assertEquals(fieldCoordinatesWithValue[i].expectedFieldValue(),
                    fields[fieldCoordinatesWithValue[i].rowToCheck()][fieldCoordinatesWithValue[i].columnToCheck()].getText());

        }
    }
}

class BoardMovement extends TestHelper {

    @BeforeEach
    void setUp() {
        mock = new RandomizerMock();
        board = new Board(mock);
    }

    @Test
    void board_should_be_initialized_with_2_values() {
        fieldsAreEqualTo(new FieldCoordinatesWithValue(0, 0, "2"),
                new FieldCoordinatesWithValue(1, 1, "4"));
    }


    @Test
    void board_should_be_moved_right() {
        board.move(KeyCode.RIGHT);
        fieldsAreEqualTo(new FieldCoordinatesWithValue(0, end, "2"),
                new FieldCoordinatesWithValue(1, end, "4"));
    }

    @Test
    void board_should_be_moved_left() {
        board.move(KeyCode.LEFT);
        fieldsAreEqualTo(new FieldCoordinatesWithValue(0, 0, "2"),
                new FieldCoordinatesWithValue(1, 1, "4"));
    }

    @Test
    void board_should_be_moved_up() {
        board.move(KeyCode.UP);
        fieldsAreEqualTo(new FieldCoordinatesWithValue(0, 0, "2"),
                new FieldCoordinatesWithValue(0, 1, "4"));
    }

    @Test
    void board_should_be_moved_down() {
        board.move(KeyCode.DOWN);
        fieldsAreEqualTo(new FieldCoordinatesWithValue(end, 0, "2"),
                new FieldCoordinatesWithValue(end, 1, "4"));
    }

}

class BoardRules extends TestHelper {
    @BeforeEach
    void setUp() {
        mock = new RandomizerMock();
    }

    @Test
    void game_should_be_over() {
        board = new Board(mock);
        board.move(KeyCode.DOWN);
        var fields = board.getFields();
        Assertions.assertEquals("2", fields[end][0].getText());
        Assertions.assertEquals("4", fields[end][1].getText());
    }
}
