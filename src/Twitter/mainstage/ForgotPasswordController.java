package Twitter.mainstage;

import Twitter.DatabaseUtil.DataLoader;
import Twitter.Navigation.NavigationStrategy;
import Twitter.Navigation.GoToRootScene;
import Twitter.Util.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.ResultSet;

//Forgot Password View controller. It performs the action of resetting a password to an user,
//With the help of his recovery question.
public class ForgotPasswordController implements MainStage {

    @Attributes(value = "FXML Attributes")
    @FXML Button resetButton;
    @FXML Button backButton;
    @FXML Button focusButton;
    @FXML TextField userTextField;
    @FXML TextField securityTextField;
    @FXML TextField passwordTextField;
    @FXML Label resultResetLabel;

    @Override
    //Action that once performed, will lead back to the root stage view (the login form).
    public void goBack() throws IOException {
        NavigationStrategy goToRoot = new GoToRootScene(NavigationStrategy.WINDOWS.LOGIN);
        goToRoot.goToScene(null);
    }

    @Methods
    //Method to reset the password. This method assure that the fields are not empty, and once done that,
    //It will get the connection to the database through DBConnection. Once retrieved, it will query the DB
    //with the specific information combo (username + security question) to check if going to the reset phase or not.
    //If all data correct, it will perform the reset query with the desired password, otherwise it will notify the client
    //of a bad operation result.
    public void resetPassword() {

        //If fields are empty
        if(userTextField.getText().equals("") || securityTextField.getText().equals("")) {
            //Display the error message
            resultResetLabel.setTextFill(Color.rgb(183,29,53));
            resultResetLabel.setText("Fields cannot be empty.");

        }
        //If not empty, go query the database
        else {
            //Get the instance of the dataLoader
            DataLoader loader = DataLoader.getDataLoader();

            //First query: check if the user combined with security question is valid.
            String query = "SELECT * " +
                    "FROM ACCOUNT " +
                    "WHERE USERNAME=\"" + userTextField.getText() + "\" " +
                    "AND SECURITYQ=\"" + securityTextField.getText() + "\"";
            ResultSet resultSet = loader.genericSearchQuery(query);

            try {
                //If the set is null
                if(resultSet == null) {
                    //Tell the user something went wrong
                    resultResetLabel.setTextFill(Color.rgb(183,29,53));
                    resultResetLabel.setText("Something went wrong. Try again.");

                }
                //If is not null, but with 0 results, tell the user the username/security question was wrong
                else if(!resultSet.next()) {
                    resultResetLabel.setTextFill(Color.rgb(183,29,53));
                    resultResetLabel.setText("Username or security question wrong.");

                }
                //Otherwise, the user/security question combo was found: update the password
                else {
                    query = "UPDATE ACCOUNT " +
                            "SET PASSWORD=\"" + passwordTextField.getText() + "\" " +
                            "WHERE USERNAME=\"" + userTextField.getText() + "\"";
                    Boolean result = loader.genericUpdateQuery(query);

                    //if query was successful
                    if(result) {
                        //communicate that to the user
                        resultResetLabel.setTextFill(Color.rgb(93,188,210));
                        resultResetLabel.setText("Password resetted successfully.");
                        focusButton.requestFocus();

                        //reset the fields
                        userTextField.setText("");
                        passwordTextField.setText("");
                        securityTextField.setText("");

                    }
                    //else, if was not successful
                    else {
                        //tell the user
                        resultResetLabel.setTextFill(Color.rgb(183,29,53));
                        resultResetLabel.setText("Something went wrong. Try again.");
                        focusButton.requestFocus();

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

}
