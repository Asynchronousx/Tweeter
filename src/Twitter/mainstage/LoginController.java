package Twitter.mainstage;

import Twitter.DatabaseUtil.DBConnection;
import Twitter.DatabaseUtil.DBController;
import Twitter.DatabaseUtil.DataLoader;
import Twitter.GlobalSingleton;
import Twitter.Navigation.GoToNextScene;
import Twitter.Navigation.NavigationStrategy;
import Twitter.Util.*;

import Twitter.adminstage.AdminLoader;
import Twitter.twitterstage.TweeterLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.sql.ResultSet;

//Login View Controller. It will aid the user to create a new account, recover a lost password or log into the main application.
public class LoginController {

    @Attributes(value = "FXML Attributes")
    @FXML Button loginButton;
    @FXML Button signButton;
    @FXML Button forgotButton;
    @FXML Button focusButton;
    @FXML TextField usernameTextField;
    @FXML PasswordField passwordTextField;
    @FXML Label connectionResultLabel;

    @Attributes
    MessageHandler handler = MessageHandler.getMessageHandler();

    @Methods(value="Logic")
    //Methods that reset all the fields presents.
    private void resetFields() {
        usernameTextField.setText("");
        passwordTextField.setText("");
        connectionResultLabel.setText("");
        focusButton.requestFocus();
    }


    @Methods
    //Method that perform the navigation to the signup form.
    public void openSignUpForm() throws Exception {
        Parent signUpForm = FXMLLoader.load(getClass().getResource("twitterSignUp.fxml"));
        NavigationStrategy goToSignUp = new GoToNextScene(NavigationStrategy.WINDOWS.MAIN, "Sign Up", 700, 397);
        goToSignUp.goToScene(signUpForm);
    }

    //Method that perform the navigation to the forgot password form
    public void openForgotForm() throws Exception {
        Parent forgotForm = FXMLLoader.load(getClass().getResource("twitterForgotPsw.fxml"));
        NavigationStrategy goToForgot = new GoToNextScene(NavigationStrategy.WINDOWS.MAIN, "Forgot Password", 700, 397);
        goToForgot.goToScene(forgotForm);
    }

    //Method that will log the user into his home feed.
    //This method will user DBConnection and DBController class interface to interrogate the database and retrieve connection.
    //This method will take the user and password and check if they exists into the database, with the specific query
    //that will assure the authenticity of the user. If the user exists, it will save which user is using the application
    //into the global singleton, to help load the data from databases once logged in, and prompt the user into the feed home
    //calling the loader of the twitter stage, that will take care of load the main scene.
    //If not correct, it will display the error to the user.
    public void logIn() {

        //If the user or psw fields was empty, display the error to the client
        if(usernameTextField.getText().equals("") || passwordTextField.getText().equals("")) {
            usernameTextField.requestFocus();
            handler.displayMessage("Fields cannot be empty.", Color.rgb(183,29,53), connectionResultLabel);

        }
        //otherwise, go check the user/psw combo into the database to check if the user exists
        else {
            DataLoader loader = DataLoader.getDataLoader();
            String query = "SELECT * " +
                    "FROM ACCOUNT " +
                    "WHERE USERNAME=\"" + usernameTextField.getText() + "\" " +
                    "AND PASSWORD=\"" + passwordTextField.getText() + "\"";

            ResultSet querySet = loader.genericSearchQuery(query);

            try {
                //if the set is null, something went wrong: notify the client
                if (querySet == null) {
                    handler.displayMessage("Something went wrong. Try again.", Color.rgb(183,29,53), connectionResultLabel);

                }
                //else, if the result set is present, but there is no next value, the user/psw combo was incorrect: notify the client
                else if (!querySet.next()) {
                    handler.displayMessage("Username/Password incorrect. Try again.", Color.rgb(183,29,53), connectionResultLabel);

                }
                //else, the user was found: now check if is an admin or a normal user.
                else {
                    //check if admin
                    if(usernameTextField.getText().toLowerCase().equals("admin")) {
                        //reset fields and load the admin stage through the admin loader class.
                        resetFields();
                        AdminLoader adminLoader = new AdminLoader();
                        adminLoader.loadStage("adminPanel.fxml", 550, 730, 550, 730);

                    }
                    //Check if normal user
                    else {
                        //reset fields, load the name and load the tweeter stage through the tweeter loader class.
                        GlobalSingleton.getGlobalSingleton().setAccountName(usernameTextField.getText());
                        resetFields();
                        TweeterLoader tweeterLoader = new TweeterLoader();
                        tweeterLoader.loadStage("tweeterFeed.fxml", 880, 873, 880, 873);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

}

