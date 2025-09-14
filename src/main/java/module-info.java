module com.robothaver.mp3reordergradle {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires mp3agic;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;
    requires java.desktop;
    requires static lombok;

    opens com.robothaver.mp3reordergradle to javafx.fxml;
    exports com.robothaver.mp3reordergradle;

    opens com.robothaver.mp3reordergradle.mp3_viewer to javafx.base;
    opens com.robothaver.mp3reordergradle.mp3_viewer.song.loader to javafx.base;
    opens com.robothaver.mp3reordergradle.mp3_viewer.song.track to javafx.base;
    opens com.robothaver.mp3reordergradle.mp3_viewer.song.domain to javafx.base;
}