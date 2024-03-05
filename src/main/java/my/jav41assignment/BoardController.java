package my.jav41assignment;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class BoardController {
    @FXML
    public Label time;
    @FXML
    public Label score ;
    @FXML
    private VBox vbox;
    private Board board;
    private final Label[][] labels = new Label[Board.gameSize][Board.gameSize];
    @FXML
    void initialize(){
        createLabels();
        startGame();
    }

    private void startGame() {
        initializeBoard();
        initializeScore();
        updateLabels();
        startGameTimer();
    }

    private void initializeScore() {
        setScore(0);
    }

    private void setScore(int newScore){
        score.setText("Punktestand: " + newScore);
    }

    private void startGameTimer() {
        var gameStart = LocalDateTime.now();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                LocalDateTime tempDateTime = gameStart;

                long hours = tempDateTime.until(LocalDateTime.now(), ChronoUnit.HOURS);
                tempDateTime = tempDateTime.plusHours(hours);

                long minutes = tempDateTime.until(LocalDateTime.now(), ChronoUnit.MINUTES);
                tempDateTime = tempDateTime.plusMinutes(minutes);

                long seconds = tempDateTime.until(LocalDateTime.now(), ChronoUnit.SECONDS);

                time.setText("Spieldauer: " +
                        String.format("%02d", hours) + ":" +
                        String.format("%02d", minutes) + ":" +
                        String.format("%02d", seconds));
            }
        };
        timer.start();
    }

    private void createLabels(){
        var gridPane = new GridPane();

        for( int size = 0; size < Board.gameSize; size++){
            var column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            column.setPercentWidth(100.0 / Board.gameSize);
            gridPane.getColumnConstraints().add(column);

            var row = new RowConstraints();
            row.setPercentHeight(100.0 / Board.gameSize);
            gridPane.getRowConstraints().add(row);
        }

        for( int row = 0; row < Board.gameSize; row++){
            for (int column = 0; column < Board.gameSize; column++ ){
                var label = new Label();
                label.setPrefSize(100,100);
                gridPane.add(label, column, row);
                this.labels[row][column] = label;
            }
        }
        gridPane.setGridLinesVisible(true);
        vbox.getChildren().add(gridPane);

    }

    @FXML
    protected void onUserInput(KeyEvent keyEvent) {
        moveBoard(keyEvent);
        updateLabels();
    }

    private void updateLabels() {
        var fields = board.getFields();

        for( int row = 0; row < Board.gameSize; row++){
            for (int column = 0; column < Board.gameSize; column++ ){
                var field = fields[row][column];
                var label = labels[row][column];

                label.setText(field.getText() );
                label.setBackground(new Background(new BackgroundFill(field.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
                label.setMaxWidth(Double.MAX_VALUE);
                label.setAlignment(Pos.CENTER);
            }
        }
    }

    private void moveBoard(KeyEvent keyEvent) {
        try {
           var newScore = board.move(keyEvent.getCode());
           setScore(newScore);
        } catch ( Exception  wrongKey ){
            // do nothing
        }
    }

    private void initializeBoard() {
        board = new Board( new ValueRandomizer());
        board.attachGameWonEventListener(this::onGameWon);
        board.attachGameLostEventListener(this::onGameLost);
    }

    public void onGameWon() {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Du hast gewonnen! Möchtest du weiterspielen?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.NO) {
            startGame();
        }else{
            Board.gameEndValue = Board.gameEndValue * 2;
        }
    }

    private void showResult(String message, Object action) {
    }


    public void onGameLost() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Du hast verloren! Möchtest du nochmal von vorn beginnen?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            startGame();
        }else{
            System.exit(0);
        }
    }
}