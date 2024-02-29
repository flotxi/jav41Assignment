package my.jav41assignment;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RandomizerMock implements Randomizer{

    private Integer callCount = 0;
    @Override
    public Integer getNextNumber() {
        if (callCount == 0){
            callCount++;
            return 2;
        }else{
            return 4;
        }
    }

    @Override
    public FieldCoordinates getNextFieldCoordinates() {
        if (callCount == 0){
            return new FieldCoordinates(0,0);
        }else{
            return new FieldCoordinates(1,1);
        }
    }
}

class BoardTest {
    Board board;
    RandomizerMock mock;
    @BeforeEach
    void setUp() {
        mock = new RandomizerMock();
        board = new Board(mock);
    }

    @Test
    void board_should_be_initialized_with_2_values(){
        var fields = board.getFields();
        Assertions.assertEquals("2", fields[0][0].getText());
        Assertions.assertEquals("4", fields[1][1].getText());
    }
    @Test
    void board_should_be_moved_right(){
        board.move( KeyCode.RIGHT);
        var fields = board.getFields();
        Assertions.assertEquals("2", fields[0][2].getText());
        Assertions.assertEquals("4", fields[1][2].getText());
    }
}
