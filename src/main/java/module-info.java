module org.example.lscinvoicebuilder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires itextpdf;
    requires java.sql;

    opens org.example.lscinvoicebuilder to javafx.fxml;
    exports org.example.lscinvoicebuilder;
}