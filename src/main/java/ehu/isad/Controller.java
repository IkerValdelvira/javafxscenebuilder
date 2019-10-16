package ehu.isad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

public class Controller implements Initializable {

  // Reference to the main application.
  private Main mainApp;

  @FXML
  private ComboBox bildumak;


  public void setMainApp(Main main) {
    this.mainApp = mainApp;
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {

    Zeharkatu z = null;
    try {
      z = new Zeharkatu();
      HashMap<String,String> bildumaIzenaId = z.bildumakLortu();
      Iterator allIter = bildumaIzenaId.keySet().iterator(); // datu guztiak ditugu. bildumen izenak zeharkatzeko iteratzailea lortu
      ObservableList<String> bildumaList = FXCollections.observableArrayList();
      while(allIter.hasNext()){
        bildumaList.add((String)allIter.next());
      }
      bildumak.setItems(bildumaList);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
