package Twitter.mainstage;


import Twitter.DatabaseUtil.DataLoader;
import Twitter.Navigation.NavigationStrategy;
import Twitter.Navigation.GoToRootScene;
import Twitter.Util.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

//Controller class for the signup view. It manages the account creation.
public class SignupController implements MainStage, Initializable {

    @Attributes(value = "FXML Attributes")
    @FXML Button createButton;
    @FXML Button backButton;
    @FXML Button focusButton;
    @FXML TextField newUserTextField;
    @FXML TextField newPassTextField;
    @FXML TextField newSecurityTextField;
    @FXML Label placeholderLabel;
    @FXML Label resultCreationLabel;

    //List that memorize security questions.
    @Attributes
    MessageHandler handler = MessageHandler.getMessageHandler();
    private List<String> securityQuestion;

    @Methods(value="Logic")
    //Method that load the question into the array.
    private void loadSecurityQuestion() {
        securityQuestion = new ArrayList<>();
        securityQuestion.add("Name of your first pet?");
        securityQuestion.add("What is your favorite movie?");
        securityQuestion.add("Who is your childhood hero?");
        securityQuestion.add("The town of your first job?");
        securityQuestion.add("Your best friend's last name?");
        securityQuestion.add("What is your favourite dish?");
        securityQuestion.add("The name of your first school?");
        securityQuestion.add("In which city you were born?");
    }


    @Override
    //We're calling initialize method to initialize security question to show to the user at the moment of registration.
    //It will show a random question from the list.
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Init the security question
        loadSecurityQuestion();
        //Get a random security question to display
        placeholderLabel.setText(securityQuestion.get((int)(Math.random() * securityQuestion.size())));

    }

    @Override
    //Action that once performed, will lead back to the root stage view (the login form).
    public void goBack() throws IOException {
        NavigationStrategy goToLogin = new GoToRootScene(NavigationStrategy.WINDOWS.LOGIN);
        goToLogin.goToScene(null);
    }

    @Methods
    //Method that create a new account of a new user.
    //It firsts check if the fields are empty, if so it will prompt an error.
    //If not, with the help of DBConnection and DBController class it will query the database and check if a user with
    //this username does not exists. If exists, the user will need to choose another username, if not, and the update query was successful,
    //It will create the account which the user will use to log into the application, and display the success of the operation.
    //In addition to that, it will create the entry into the PERSON entity into the database, which will be the core of data manipulation.
    public void createAccount() {

        //If some fields of the account creation are empty, notify the client
        if(newUserTextField.getText().equals("") || newPassTextField.getText().equals("") || newSecurityTextField.getText().equals("")) {
            handler.displayMessage("Fields can't be empty.", Color.rgb(183,29,53), resultCreationLabel);
            focusButton.requestFocus();

        }
        //else, go query the database with the inserted user info
        else {
            DataLoader loader = DataLoader.getDataLoader();
            Date creationDate = new Date();
            SimpleDateFormat dataformat = new SimpleDateFormat("yyyy-MM-dd");
            String query = "INSERT INTO ACCOUNT VALUES" +
                    "(\"" + newUserTextField.getText() + "\" " +
                    ",\"" + newPassTextField.getText() + "\" " +
                    ",\"" + newSecurityTextField.getText() + "\" " +
                    ",\"" + dataformat.format(creationDate) + "\");";

            //Do the query
            Boolean result = loader.genericUpdateQuery(query);

            //if the result is not false, the query was submitted
            if(result) {
                //Insert the data into a new person, but first fetch the formatted data
                String formattedDate = "";
                query = "SELECT DATE_FORMAT(DATECREATED,'%M %Y') AS DATE FROM ACCOUNT WHERE USERNAME=\""+newUserTextField.getText()+ "\";";
                ResultSet resultSet = loader.genericSearchQuery(query);

                //try to fetch the date
                try {
                    resultSet.next();
                    formattedDate = resultSet.getString("DATE");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //insert into person the new values
                query = "INSERT INTO PERSON VALUES(" +
                        "\"" + newUserTextField.getText() + "\"" +
                        ",\"" + formattedDate + "\"" +
                        ",NULL" + ");";

                //get the result
                result = loader.genericUpdateQuery(query);

                //If the query was good
                if(result) {
                    //tell the user the process was good
                    handler.displayMessage("Account successfully created.", Color.rgb(93,188,210), resultCreationLabel);
                    focusButton.requestFocus();

                } else {
                    //else delete the inserted account and let the user try again, displaying the error message.
                    loader.genericUpdateQuery("DELETE FROM ACCOUNT WHERE USERNAME =\"" + newUserTextField.getText() + "\";");
                    handler.displayMessage("Something went wrong. Try again.", Color.rgb(183,29,53), resultCreationLabel);
                    focusButton.requestFocus();

                }
            }
            //else, an account with that name already exists: notify the client
            else {
                handler.displayMessage("An account with this name already exists.", Color.rgb(183,29,53), resultCreationLabel);
                focusButton.requestFocus();

            }
        }
    }

}
