/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stages;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Valentin
 */
public class ChartsStage {

    public ChartsStage() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/views/Charts.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Панель формирования графиков");
        stage.setScene(scene);
        stage.show();
    }
    
}
