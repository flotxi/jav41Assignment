module my.jav41assignment {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;

    opens my.jav41assignment to javafx.fxml;
    exports my.jav41assignment;
}