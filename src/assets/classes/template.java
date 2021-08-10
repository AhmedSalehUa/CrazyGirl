/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assets.classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Parent node = FXMLLoader.load(getClass().getResource(NoPermission)); if
 * (User.canAccess("accountContracts")) { node =
 * FXMLLoader.load(getClass().getResource(AccountsScreenContract)); } access
 * list accounts accountExpenses accountYields accountContracts
 * accountMedicineCompany
 *
 * MainDataScreenContract MainDataScreenClincs MainDataScreenMedicine
 * MainDataScreenDoctors MainDataScreenPatient
 *
 * AdmissionScreen ReceptionScreenDailyExpenses ReceptionScreenExportToAccounts
 * ReceptionScreenShortsOrder ReceptionScreenGetYields
 *
 *
 *
 *
 *
 */
public class template {

    public template() {
        ObservableList<String> d = FXCollections.observableArrayList();
        /**     
             
         */
        d.add("ALTER TABLE `sl_calls` ADD `sales_id` INT NOT NULL AFTER `details`");
        d.add("ALTER TABLE `sl_client` ADD `sales_id` INT NOT NULL AFTER `tele2`");
        d.add("ALTER TABLE `sl_offers` ADD `sales_id` INT NOT NULL AFTER `doc_ext`");
        d.add("ALTER TABLE `sl_offers` ADD `cost` VARCHAR(700) NOT NULL AFTER `date`, ADD `discount` VARCHAR(700) NOT NULL AFTER `cost`, ADD `discount_percent` VARCHAR(700) NOT NULL AFTER `discount`");
        d.add("ALTER TABLE `sl_offers` ADD `notes` VARCHAR(1400) NOT NULL AFTER `sales_id`");
        d.add("ALTER TABLE `sl_offers` CHANGE `id` `id` INT(11) NOT NULL AUTO_INCREMENT, CHANGE `discount` `discount` VARCHAR(700) CHARACTER SET utf8 COLLATE utf8_general_ci NULL, CHANGE `discount_percent` `discount_percent` VARCHAR(700) CHARACTER SET utf8 COLLATE utf8_general_ci NULL, CHANGE `total_cost` `total_cost` VARCHAR(700) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL, CHANGE `doc` `doc` LONGBLOB NULL, CHANGE `doc_ext` `doc_ext` VARCHAR(700) CHARACTER SET utf8 COLLATE utf8_general_ci NULL, CHANGE `notes` `notes` VARCHAR(1400) CHARACTER SET utf8 COLLATE  utf8_general_ci NULL");
        d.add("ALTER TABLE `cli_maintaince` CHANGE `date` `date` DATE NOT NULL;");
        d.add("ALTER TABLE `cli_contracts` CHANGE `num_of_visits` `num_of_visits` VARCHAR(700) NOT NULL, CHANGE `cost` `cost` VARCHAR(700) NOT NULL, CHANGE `due_after` `due_after` VARCHAR(700) NOT NULL;");
        d.add("ALTER TABLE `st_stores` CHANGE `name` `name` VARCHAR(700) NOT NULL;");

    }
    
//    public boolean Add() throws Exception {PreparedStatement ps = db.get.Prepare("");ps.execute();return true;
//    }
//
//    public boolean Edite() throws Exception {PreparedStatement ps = db.get.Prepare("");ps.execute();return true;
//    }
//
//    public boolean Delete() throws Exception {PreparedStatement ps = db.get.Prepare("");ps.execute();return true;
//    }
//
//    public static ObservableList<> getData() throws Exception {
//    ObservableList<> data = FXCollections.observableArrayList();
//        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("");
//        while(rs.next()){
//            data.add();
//        }
//        return data;
//    }
//
//    public static String getAutoNum() throws Exception {
//     return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM ``").getValueAt(0, 0).toString();
//    }
    /*
    progress.setVisible(true); 
        Service<Void> service = new Service<Void>() {boolean ok =true;
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    

                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);ok=false;
                                } finally {
                                    latch.countDown();
                                }
                            }

                        });
                        latch.await();

                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);if(ok){
               clear();getData();}
                super.succeeded();
            }
        };
        service.start();
    
    // no runnable
    progress.setVisible(true); 
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                           try {
                                    

                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                }  
                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);
               clear();getData();
                super.succeeded();
            }
        };
        service.start();
    
     
    
         .setCellValueFactory(new PropertyValueFactory<>(""));
    
    
    
    FilteredList<> filteredData = new FilteredList<>(items, p -> true);

        filteredData.setPredicate(pa -> {

            if (search.getText() == null || search.getText().isEmpty()) {
                return true;
            }

            String lowerCaseFilter = search.getText().toLowerCase();

            return (pa.getName().contains(lowerCaseFilter)
                    || pa.getAge().contains(lowerCaseFilter) 
                    || pa.getQualification_name().contains(lowerCaseFilter)
                    || pa.getTele1().contains(lowerCaseFilter)
                    || pa.getTele2().contains(lowerCaseFilter));

        });

        SortedList< > sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(.comparatorProperty());
        .setItems(sortedData);
    
     DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
     date.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null) {
                    return "";
                }
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });
    
     Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setTitle("Deleting Editing  ");
                                        alert.setHeaderText("سيتم حذف  تعديل ");
                                        alert.setContentText("هل انت متاكد؟");

                                        Optional<ButtonType> result = alert.showAndWait();
                                        if (result.get() == ButtonType.OK) {}
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setTitle("Editing  ");
                                        alert.setHeaderText("سيتم تعديل ");
                                        alert.setContentText("هل انت متاكد؟");

                                        Optional<ButtonType> result = alert.showAndWait();
                                        if (result.get() == ButtonType.OK) {}
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    clear();
                                    intialColumn();
                                    getData();
                                    fillCombo();

                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                } finally {
                                    latch.countDown();
                                }
                            }

                        });
                        latch.await();

                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);

                super.succeeded();
            }
        };
        service.start();
    }
    private void intialColumn() {

    }
    private void clear() {
        getAutoNum();
     formNew.setDisable(true);

            formDelete.setDisable(true);

            formEdite.setDisable(true);

            formAdd.setDisable(false);
    }

    private void getAutoNum() {

    }

    private void getData() {

    }

    private void fillCombo() {

    }
    
    deptTable.setOnMouseClicked((e) -> {
            if (deptTable.getSelectionModel().getSelectedIndex() == -1) {
            } else {
                formNew.setDisable(false);
                
                formDelete.setDisable(false);
                
                formEdite.setDisable(false);
                
                formAdd.setDisable(true);
                
                  selected = deptTable.getSelectionModel().getSelectedItem();
                deptId.setText(Integer.toString(selected.getId()));
                deptName.setText(selected.getName());
                
                ObservableList<Organization> items1 = deptOrg.getItems();
                for (Organization a : items1) {
                    if (a.getName().equals(selected.getOrganization())) {
                        deptOrg.getSelectionModel().select(a);
                    }
                }
            }
        });
    
    
            servicesName.setItems(ContractServicesName.getData());
            servicesName.setConverter(new StringConverter<ContractServicesName>() {
                @Override
                public String toString(ContractServicesName patient) {
                    return patient.getName();
                }

                @Override
                public ContractServicesName fromString(String string) {
                    return null;
                }
            });
            servicesName.setCellFactory(cell -> new ListCell<ContractServicesName>() {

                 GridPane gridPane = new GridPane();
                Label lblid = new Label();
                Label lblName = new Label();

                 {
                     gridPane.getColumnConstraints().addAll(
                            new ColumnConstraints(100, 100, 100),
                            new ColumnConstraints(100, 100, 100)
                    );

                    gridPane.add(lblid, 0, 1);
                    gridPane.add(lblName, 1, 1);

                }

                  @Override
                protected void updateItem(ContractServicesName person, boolean empty) {
                    super.updateItem(person, empty);

                    if (!empty && person != null) {

                         lblid.setText("م: " + Integer.toString(person.getId()));
                        lblName.setText("الاسم: " + person.getName());

                         setGraphic(gridPane);
                    } else {
                         setGraphic(null);
                    }
                }
            });
    
    
    
       TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Add Cat Name");
        dialog.setHeaderText("اضافة تصنيف جديد");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().isEmpty() || result.get() == null) {
                AlertDialogs.showError("خطا!! يرجي ادخال اسم نوع");
            } else {
                final String results = result.get();
                try {
                    Service service = new Service() {
                        @Override
                        protected Task createTask() {
                            return new Task() {
                                @Override
                                protected Object call() throws Exception {
                                    final CountDownLatch latch = new CountDownLatch(1);
                                    Platform.runLater(() -> {
                                        try {
                                           
                                        } catch (Exception ex) {
                                            AlertDialogs.showErrors(ex);
                                        } finally {
                                            latch.countDown();
                                        }
                                    });
                                    latch.await();

                                    return null;
                                }

                                @Override
                                protected void succeeded() {
                                    try {
                                      fillCombo();
                                    } catch (Exception ex) {
                                        AlertDialogs.showErrors(ex);
                                    }
                                }
                            };
                        }
                    };
                    service.start();

                } catch (Exception ex) {
                    AlertDialogs.showErrors(ex);
                }
            }
        }
  try {
            Parent mainMember = FXMLLoader.load(getClass().getResource(HrScreenEmployes));
            mainMember.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
            Scene sc = new Scene(mainMember);
            Stage st = new Stage();
            st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
            st.setTitle("Acapy Trade (الموظفين)");
            st.setScene(sc);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            st.setX((screenBounds.getWidth() - 1360) / 2);
            st.setY((screenBounds.getHeight() - 760) / 2);
            st.show();
        } catch (IOException ex) {
            AlertDialogs.showErrors(ex);
        }*/
}
