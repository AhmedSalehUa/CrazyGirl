//package assets.classes;
//
//import java.util.stream.Stream;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.Event;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.Label;
//import javafx.scene.control.ListCell;
//import javafx.scene.control.Tooltip;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.layout.ColumnConstraints;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Window;
//import javafx.util.StringConverter; 
//import screens.clients.assets.Clients;
//
//public class ComboBoxAutoComplete {
//
//    private ComboBox<Clients> cmb;
//    String filter = "";
//    private ObservableList<Clients> originalItems;
//
//    public ComboBoxAutoComplete(ComboBox<Clients> cmb) {
//        this.cmb = cmb;
//        this.cmb.setConverter(new StringConverter<Clients>() {
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
//        this.cmb.setCellFactory(cell -> new ListCell<Clients>() {
//
//            // Create our layout here to be reused for each ListCell
//            GridPane gridPane = new GridPane();
//            Label lblid = new Label();
//            Label lblName = new Label();
//            Label lblAge = new Label();
//            Label lblTele = new Label();
//
//              {
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
//        originalItems = FXCollections.observableArrayList(cmb.getItems());
//        cmb.setTooltip(new Tooltip());
//        cmb.setOnKeyPressed(this::handleOnKeyPressed);
//        cmb.setOnHidden(this::handleOnHiding);
//    }
//
//    public void handleOnKeyPressed(KeyEvent e) {
//        ObservableList<Clients> filteredList = FXCollections.observableArrayList();
//        KeyCode code = e.getCode();
//
//       
//        filter += e.getText();
//
//        if (code == KeyCode.BACK_SPACE && filter.length() > 0) {
//            filter = filter.substring(0, filter.length() - 1);
//            cmb.getItems().setAll(originalItems);
//        }
//        if (code == KeyCode.ESCAPE) {
//            filter = "";
//        }
//        if (filter.length() == 0) {
//            filteredList = originalItems;
//            cmb.getTooltip().hide();
//        } else {
//            String txtUsr = filter;
////            System.out.println(txtUsr);
//            ObservableList<Clients> items1 = cmb.getItems();
//            for (Clients a : items1) {
//                if (a.getName().contains(txtUsr)) {
//                    filteredList.add(a);
//                }
//            }
//
//            cmb.getTooltip().setText(txtUsr);
//            Window stage = cmb.getScene().getWindow();
//            double posX = stage.getX() + cmb.getBoundsInParent().getMinX();
//            double posY = stage.getY() + cmb.getBoundsInParent().getMinY();
//            cmb.getTooltip().show(stage, posX, posY);
//            cmb.show();
//        }
//        cmb.getItems().setAll(filteredList);
//    }
//
//    public void handleOnHiding(Event e) {
//        filter = "";
//        cmb.getTooltip().hide();
//        Clients s = cmb.getSelectionModel().getSelectedItem();
//        cmb.getItems().setAll(originalItems);
//        cmb.getSelectionModel().select(s);
//    }
//
//}
