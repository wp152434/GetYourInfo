package choi.dimigo.address;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

public class Menu implements Initializable {
    
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
    }
	
	public void aboutBtnAction(ActionEvent event){
		try {
            Desktop.getDesktop().browse(new URI("http://github.com/choich"));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
	}
	
	public void closeBtnAction(ActionEvent event){
		Platform.exit();
	}

}
