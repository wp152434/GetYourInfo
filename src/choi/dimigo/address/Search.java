package choi.dimigo.address;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.apache.commons.codec.binary.Base64;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class Search implements Initializable{

	@FXML
	private Button searchBtn;
	@FXML
	private TextField inputText;
    @FXML
    private TableView<TableRowDataModel> tableView;
    @FXML
    private TableColumn<TableRowDataModel, String> kind;
    @FXML
    private TableColumn<TableRowDataModel, String> contents;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		searchBtn.setOnAction(event -> searchBtnAction(event));
		kind.setCellValueFactory(cellData -> cellData.getValue().kindProperty());
		contents.setCellValueFactory(cellData -> cellData.getValue().contentsProperty());
	}
	
	public void searchBtnAction(ActionEvent event) {
		String text = inputText.getText();
		if("".equals(text))
			return;
		else if(text.matches(".*[0-9].*")){
			text = "serial="+text;
		}
		else if(text.matches(".*[0-9].*") == false){
			return;
		}
		String json = crawler(text);
		if(json == null)
			return;
		JSONObject jsonObj = jsonParser(json);
		addTableView(jsonObj);
	}
	
	public void addTableView(JSONObject jsonObj){
		ObservableList<TableRowDataModel> myList = FXCollections.observableArrayList(
				new TableRowDataModel(new SimpleStringProperty("UID"),
									  new SimpleStringProperty(String.valueOf(jsonObj.get("user_id")))),
	            new TableRowDataModel(new SimpleStringProperty("아이디"),
	            					  new SimpleStringProperty((String) jsonObj.get("username"))),
	            new TableRowDataModel(new SimpleStringProperty("이름"),
						 			  new SimpleStringProperty((String) jsonObj.get("name"))),
	    		new TableRowDataModel(new SimpleStringProperty("성별"),
						 			  new SimpleStringProperty("M".equals(((String) jsonObj.get("gender")))?"남자":"여자")),
				new TableRowDataModel(new SimpleStringProperty("학번"),
									  new SimpleStringProperty((String) jsonObj.get("serial"))),
				new TableRowDataModel(new SimpleStringProperty("학년"),
									  new SimpleStringProperty(String.valueOf(jsonObj.get("grade")))),
				new TableRowDataModel(new SimpleStringProperty("반"),
									  new SimpleStringProperty(String.valueOf(jsonObj.get("class")))),
				new TableRowDataModel(new SimpleStringProperty("번호"),
								      new SimpleStringProperty(String.valueOf(jsonObj.get("number")))),
				new TableRowDataModel(new SimpleStringProperty("기숙사"),
									  new SimpleStringProperty((String) jsonObj.get("dormitory")))
	    );
		tableView.setItems(myList);

	}
	
	public String crawler(String text){
		String result = null;
		try {
			String urlStr = "http://api.dimigo.org/v1/user-students/search?"+text;
			
			String name = ""; // DIMIGO REST API id
			String password = ""; // DIMIGO REST API password

			String authString = name + ":" + password;
			System.out.println("auth string: " + authString);
			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			String authStringEnc = new String(authEncBytes);
			System.out.println("Base64 encoded auth string: " + authStringEnc);

			URL url = new URL(urlStr);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			result = sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
    public JSONObject jsonParser(String jsonStr){

    	jsonStr = jsonStr.substring(1, jsonStr.length()-1);
    	
    	try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(jsonStr);
            return jsonObj;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    	return null;
    }
}   
