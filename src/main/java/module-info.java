module my.jav41assignment {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens my.jav41assignment to javafx.fxml;
    exports my.jav41assignment;
}