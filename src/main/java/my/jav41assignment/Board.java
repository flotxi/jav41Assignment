package my.jav41assignment;

import javafx.scene.input.KeyCode;

public class Board {

    public static Integer gameSize = 4;
    private final Randomizer randomizer;
    private final Field[][] fields = new Field[gameSize][gameSize];
    public Board(Randomizer randomizer){
        this.randomizer = randomizer;
        // https://www.jetbrains.com/guide/java/tutorials/marco-codes-junit/introduction/
        // 18 MInuten
        for( int row = 0; row < gameSize; row++){
            for (int column = 0; column < gameSize; column++ ){
                fields[row][column] = new Field( 0 );
            }
        }

        var firstFieldCoordinates = this.randomizer.getNextFieldCoordinates();
        fields[firstFieldCoordinates.row()][firstFieldCoordinates.column()].setValue(this.randomizer.getNextNumber());

        var secondFieldCoordinates = this.randomizer.getNextFieldCoordinates();
        while (secondFieldCoordinates == firstFieldCoordinates){
            secondFieldCoordinates = this.randomizer.getNextFieldCoordinates();
        }
        fields[secondFieldCoordinates.row()][secondFieldCoordinates.column()].setValue(this.randomizer.getNextNumber());
    }

    public void move(KeyCode key) {

        var direction = switch (key){
            case KeyCode.LEFT, KeyCode.UP -> "-";
            case KeyCode.RIGHT, KeyCode.DOWN -> "+";
            default -> throw new RuntimeException() ;
        };

        for( var row : this.fields){
            for (var field : row ){
                field.move();
            }
        }

    }
    public Field[][] getFields(){
        return  this.fields;
    }
}
