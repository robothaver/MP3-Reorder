module com.robothaver.mp3reorder {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires mp3agic;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;
    requires java.desktop;
    requires static lombok;
    requires jdk.compiler;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires java.management;
    requires java.naming;

    opens com.robothaver.mp3reorder to javafx.fxml;
    exports com.robothaver.mp3reorder;

    opens com.robothaver.mp3reorder.mp3 to javafx.base;
    opens com.robothaver.mp3reorder.mp3.song.load to javafx.base;
    opens com.robothaver.mp3reorder.mp3.domain to javafx.base;
    opens com.robothaver.mp3reorder.dialog to javafx.base;
    opens com.robothaver.mp3reorder.mp3.song.track.assigner to javafx.base;
    opens com.robothaver.mp3reorder.dialog.error to javafx.base;
    opens com.robothaver.mp3reorder.dialog.option to javafx.base;
    opens com.robothaver.mp3reorder.mp3.song.task.domain to javafx.base;
    opens com.robothaver.mp3reorder.mp3.controls.search to javafx.base;
    exports com.robothaver.mp3reorder.core.language;
    opens com.robothaver.mp3reorder.core.language to javafx.base, javafx.fxml;
    exports com.robothaver.mp3reorder.core;
    opens com.robothaver.mp3reorder.core to javafx.fxml;
    exports com.robothaver.mp3reorder.core.font;
    opens com.robothaver.mp3reorder.core.font to javafx.fxml;
    opens com.robothaver.mp3reorder.mp3.song.load.task to javafx.base;
}