package Twitter.adminstage;

import Twitter.GlobalSingleton;
import Twitter.Navigation.GoToRootScene;
import Twitter.Navigation.NavigationStrategy;
import Twitter.SceneNavigator.WindowSingleton;
import Twitter.Util.Attributes;
import Twitter.Util.Methods;
import Twitter.twitterstage.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @Attributes(value="FXML")
    @FXML Button refreshButton;
    @FXML Button focusButton;
    @FXML Button logoutButton;
    @FXML Button searchButton;
    @FXML TextField searchTextField;
    @FXML ListView<Person> userListView;

    @Attributes
    private ObservableList<Person> observablePersonList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //At stage initialize, call the refresh method to show all the component
        refresh();
        focusButton.requestFocus();
    }

    @Methods
    public void logOut() throws IOException {
        NavigationStrategy goToLogin = new GoToRootScene(NavigationStrategy.WINDOWS.LOGIN);
        WindowSingleton.getWindowSingleton().getRootAdminStage().close();
        goToLogin.goToScene(null);
    }

    public void refresh() {
        //Init the cell loader and alert
        Alert alert = new Alert(Alert.AlertType.ERROR);
        AdminCellLoader celLoader = new AdminCellLoader();

        //if result is 0, no user was found
        if (celLoader.loadCell(observablePersonList, userListView, AdminCellLoader.Show.ALLUSER).equals(0)) {
            alert.setTitle("No users found");
            alert.setContentText("The database is empty.");
            alert.showAndWait();

        }

        focusButton.requestFocus();

    }

    public void search() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        //If search field empty, prompt a message
        if (searchTextField.getText().equals("")) {
            alert.setTitle("Empty fields");
            alert.setContentText("Search field can't be empty.");
            alert.showAndWait();
            return;

        }

        //otherwise, go search the user
        AdminCellLoader cellLoader = new AdminCellLoader();
        GlobalSingleton.getGlobalSingleton().setSearchQuery(searchTextField.getText());

        //if result is 0, no user was found
        if (cellLoader.loadCell(observablePersonList, userListView, AdminCellLoader.Show.SINGLEUSER).equals(0)) {
            alert.setTitle("No users found");
            alert.setContentText("The searched user doesn't exists.");
            alert.showAndWait();
            refresh();

        }

    }

}
