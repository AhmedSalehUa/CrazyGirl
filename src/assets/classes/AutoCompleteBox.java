//package assets.classes;
//
//import javafx.application.Platform;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.Event;
//import javafx.event.EventHandler;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.Label;
//import javafx.scene.control.ListCell;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.MouseButton;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.ColumnConstraints;
//import javafx.scene.layout.GridPane;
//import javafx.util.StringConverter; 
//import screens.clients.assets.Clients;
//
//public class AutoCompleteBox implements EventHandler<KeyEvent> {
//
//    private ComboBox comboBox;
//    final private ObservableList<Clients> data;
//    private Integer sid;
//
//    public AutoCompleteBox(final ComboBox comboBox) {
//        this.comboBox = comboBox;
//        this.data = comboBox.getItems();
//
//        this.doAutoCompleteBox();
//
//        this.comboBox.setConverter(new StringConverter<Clients>() {
//            @Override
//            public String toString(Clients patient) {
//                return patient.getName();
//            }
//
//            @Override
//            public Clients fromString(String string) {
//                return null;
//            }
//        });
//        setConverter();
//    }
//
//    private void setConverter() {
//        this.comboBox.setCellFactory(cell -> new ListCell<Clients>() {
//
//            // Create our layout here to be reused for each ListCell
//            GridPane gridPane = new GridPane();
//            Label lblid = new Label();
//            Label lblName = new Label();
//            Label lblAge = new Label();
//            Label lblTele = new Label();
//
//            // Static block to configure our layout
//            {
//                // Ensure all our column widths are constant
//                gridPane.getColumnConstraints().addAll(
//                        new ColumnConstraints(100, 100, 100),
//                        new ColumnConstraints(100, 100, 100),
//                        new ColumnConstraints(100, 100, 100),
//                        new ColumnConstraints(100, 100, 100)
//                );
//
//                gridPane.add(lblid, 0, 1);
//                gridPane.add(lblName, 1, 1);
//                gridPane.add(lblAge, 2, 1);
//                gridPane.add(lblTele, 3, 1);
//
//            }
//
//            // We override the updateItem() method in order to provide our own layout for this Cell's graphicProperty
//            @Override
//            protected void updateItem(Clients person, boolean empty) {
//                super.updateItem(person, empty);
//
//                if (!empty && person != null) {
//
//                    // Update our Labels
//                    lblid.setText("م: " + Integer.toString(person.getId()));
//                    lblName.setText("الاسم: " + person.getName()); 
//                    lblTele.setText("الموبايل: " + person.getTele1());
//
//                    // Set this ListCell's graphicProperty to display our GridPane
//                    setGraphic(gridPane);
//                } else {
//                    // Nothing to display here
//                    setGraphic(null);
//                }
//            }
//        });
//    }
//
//    public AutoCompleteBox(final ComboBox comboBox, Integer sid) {
//        this.comboBox = comboBox;
//        this.data = comboBox.getItems();
//        this.sid = sid;
//
//        this.doAutoCompleteBox();
//
//        this.comboBox.setConverter(new StringConverter<Clients>() {
//            @Override
//            public String toString(Clients patient) {
//                return patient.getName();
//            }
//
//            @Override
//            public Clients fromString(String string) {
//                return null;
//            }
//        });
//        setConverter();
//    }
//
//    private void doAutoCompleteBox() {
//        this.comboBox.setEditable(true);
//        this.comboBox.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue) {//mean onfocus
//                this.comboBox.show();
//            }
//        });
//
//        this.comboBox.getEditor().setOnMouseClicked(event -> {
//            if (event.getButton().equals(MouseButton.PRIMARY)) {
//                if (event.getClickCount() == 2) {
//                    return;
//                }
//            }
//            this.comboBox.show();
//            setConverter();
//        });
//
//        this.comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
//            moveCaret(this.comboBox.getEditor().getText().length());
//        });
//
//        this.comboBox.setOnKeyPressed(t -> comboBox.hide());
//
//        this.comboBox.setOnKeyReleased(AutoCompleteBox.this);
//
//        if (this.sid != null) {
//            this.comboBox.getSelectionModel().select(this.sid);
//        }
//    }
//
//    @Override
//    public void handle(KeyEvent event) {
//        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN
//                || event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
//                || event.getCode() == KeyCode.HOME
//                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
//            return;
//        }
//
//        if (event.getCode() == KeyCode.BACK_SPACE) {
//            String str = this.comboBox.getEditor().getText();
//            if (str != null && str.length() > 0) {
//                str = str.substring(0, str.length() - 1);
//            }
//            if (str != null) {
//                this.comboBox.getEditor().setText(str);
//                moveCaret(str.length());
//            }
//            this.comboBox.getSelectionModel().clearSelection();
//        }
//
//        if (event.getCode() == KeyCode.ENTER && comboBox.getSelectionModel().getSelectedIndex() > -1) {
//            return;
//        }
//
//        setItems();
//    }
//
//    private void setItems() {
//        ObservableList<Clients> list = FXCollections.observableArrayList();
//
//        for (Clients datum : this.data) {
//            String s = this.comboBox.getEditor().getText().toLowerCase();
//            if (datum.getName().toLowerCase().contains(s.toLowerCase())) {
//                list.add(datum);
//            }
//        }
//
//        if (list.isEmpty()) {
//            this.comboBox.hide();
//        }
//
//        this.comboBox.setItems(list);
//        this.comboBox.show();
//        setConverter();
//    }
//
//    private void moveCaret(int textLength) {
////        this.comboBox.getEditor().positionCaret(textLength);
//    }
//
//}
