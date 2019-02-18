package Twitter.twitterstage;

import Twitter.DatabaseUtil.DataLoader;
import Twitter.GlobalSingleton;
import Twitter.Navigation.*;
import Twitter.SceneNavigator.WindowSingleton;
import Twitter.Util.Attributes;
import Twitter.Util.MessageHandler;
import Twitter.Util.Methods;
import Twitter.twitterstage.CellManager.TweetListViewCellController;
import Twitter.twitterstage.CellManager.UserListViewCellController;
import Twitter.twitterstage.model.Person;
import Twitter.twitterstage.model.Post;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

//Controller that manages the personal view. It will hold information about tweets, following and follower of a given user,
//Giving him the possibility of interact with those components.
public class PersonalFeedController implements Initializable {

    @Attributes(value="FXML")
    @FXML Button focusButton;
    @FXML Button homeButton;
    @FXML Button meButton;
    @FXML Button searchButton;
    @FXML Button logoutButton;
    @FXML Button refreshButton;
    @FXML Button changePasswordButton;
    @FXML Button showTweetButton;
    @FXML Button showFollowerButton;
    @FXML Button showFollowingButton;
    @FXML Button changeImageButton;
    @FXML Circle circledProfileIMG;
    @FXML TextField searchTextField;
    @FXML Label followerNumberLabel;
    @FXML Label communicationLabel;
    @FXML Label foundLabel;
    @FXML Label followingNumberLabel;
    @FXML Label tweetsNumberLabel;
    @FXML Label nameLabel;
    @FXML Label smallNameLabel;
    @FXML Label dateJoinedLabel;
    @FXML ListView<Person> personListView;
    @FXML ListView<Post> postListView;

    @Attributes
    //Observable lists to take track of posts and person into the relative lists view.
    private ObservableList<Post> observablePostList = FXCollections.observableArrayList();
    private ObservableList<Person> observableFollowList = FXCollections.observableArrayList();
    private MessageHandler handler = MessageHandler.getMessageHandler();


    @Override
    //We're calling initialize method to initialize components and setting the post list view
    //as first showing list view.
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Person thisPerson = GlobalSingleton.getGlobalSingleton().getThisPerson();

        //if the personal post list is empty, display nothing found label
        if(thisPerson.getPostList().isEmpty()) {
            foundLabel.setVisible(true);

        }

        //refresh all the components when loading the view
        refreshComponents(thisPerson);

        //make the person list view invisible
        personListView.setVisible(false);

        //set the tweet button disabled
        showTweetButton.setDisable(true);
        focusButton.requestFocus();

    }


    @Methods(value="Logic")
    //Method that refresh all the components present into the scene.
    private void refreshComponents(Person thisPerson) {
        //Refresh the component
        circledProfileIMG.setFill(new ImagePattern(thisPerson.getImage()));
        followerNumberLabel.setText(Integer.toString(thisPerson.getFollowers().size()));
        followingNumberLabel.setText((Integer.toString(thisPerson.getFollowings().size())));
        tweetsNumberLabel.setText(Integer.toString(thisPerson.getPostList().size()));
        nameLabel.setText(thisPerson.getName());
        smallNameLabel.setText("@"+thisPerson.getName());
        dateJoinedLabel.setText(thisPerson.getJoinedDate());

        //refresh list view
        refreshPostListView(thisPerson);
        refreshUserListView(thisPerson.getFollowings());
        refreshUserListView(thisPerson.getFollowers());
    }

    //Method that refresh the post list view when the show tweet button is pressed
    private void refreshPostListView(Person thisPerson) {
        //Clear the observable post list and add the new post list fetched from the database
        observablePostList.clear();
        observablePostList.addAll(thisPerson.getPostList());

        //refresh the list view
        postListView.setItems(null);
        postListView.setItems(observablePostList);

        //call the factory to display cells
        postListView.setCellFactory(cell -> new TweetListViewCellController());
    }

    //Method that refresh the post list view when the show follow/follower button is pressed
    private void refreshUserListView(List<Person> personList) {
        //Clear the observable user list and add the new user list fetched from the database
        observableFollowList.clear();
        observableFollowList.addAll(personList);

        //refresh the list view
        personListView.setItems(null);
        personListView.setItems(observableFollowList);

        //call the factory to display cells
        personListView.setCellFactory(cell -> new UserListViewCellController());
    }

    @Methods
    //Method that once performed, will lead the user to the home root stage
    public void goToHome() throws IOException {
        NavigationStrategy goToHome = new GoToRootScene(NavigationStrategy.WINDOWS.HOME);
        goToHome.goToScene(null);
    }

    //Method that once performed, will lead the user to the search scene
    public void goToSearch() throws IOException {

        //if the search textfield is empty, display the message and return
        if(searchTextField.getText().equals("")) {
            handler.displayMessage("Search field cannot be empty.", Color.rgb(183,29,53), communicationLabel);
            return;
        }
        //else, if the searched query is the user itself, display the error message and return
        else if(searchTextField.getText().equals(GlobalSingleton.getGlobalSingleton().getAccountName())) {
            handler.displayMessage("You cannot search yourself.", Color.rgb(183,29,53), communicationLabel);
            return;
        }
        //else init the queries
        else {
            GlobalSingleton.getGlobalSingleton().setTmpPersonSearched(searchTextField.getText());
            GlobalSingleton.getGlobalSingleton().setSearchQuery(searchTextField.getText());
            searchTextField.setText("");
        }

        //move to the search scene
        Parent searchForm = FXMLLoader.load(getClass().getResource("searchFeed.fxml"));
        NavigationStrategy goToSearch = new GoToNextScene(NavigationStrategy.WINDOWS.FEED, "Search", 880, 873);
        goToSearch.goToScene(searchForm);
    }

    //Method that once performed, will lead the user to the login main scene
    public void logOut() throws IOException {
        NavigationStrategy goToLogin = new GoToRootScene(NavigationStrategy.WINDOWS.LOGIN);
        WindowSingleton.getWindowSingleton().getRootTweeterStage().close();
        goToLogin.goToScene(null);
    }

    //Method that once performed, will change the user profile image
    public void changeProfileImage() {
        //Get the dataloader instance
        DataLoader loader = DataLoader.getDataLoader();

        //init a textinput dialog
        TextInputDialog urlTaker = new TextInputDialog("Insert your url here.");
        urlTaker.setTitle("Load image from url!");
        urlTaker.setContentText("Your url:");
        Optional<String> url = urlTaker.showAndWait();

        //if the user inserted something
        if (url.isPresent()) {
            //if the url inserted is an image (verified with the isImage method from dataloader
            if (loader.loadImage(url.get())) {
                //set the user new image
                GlobalSingleton.getGlobalSingleton().getThisPerson().setImage(new javafx.scene.image.Image(url.get(), 90, 90, false, true));

                //refresh the components
                refreshComponents(GlobalSingleton.getGlobalSingleton().getThisPerson());

                //display the message of success image changed
                handler.displayMessage("Profile image changed!", Color.rgb(93,188,210), communicationLabel);

            }
            //else, if the url is not an image or is not valid
            else {
                //display the error alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("An error occurred");
                alert.setContentText("The url was not valid.");
                alert.showAndWait();

            }
        }
        //else, if no url is present
        else {
            //display the error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An error occurred");
            alert.setContentText("You must insert an url.");
            alert.showAndWait();

        }

        focusButton.requestFocus();

    }

    //Method that once called, will display all the personal tweet of the user running the client
    public void showTweet() {
        //fetch this person
        Person thisPerson = GlobalSingleton.getGlobalSingleton().getThisPerson();

        //enable the folllowing button disabled if disabled
        if(showFollowingButton.isDisabled()) {
            showFollowingButton.setDisable(false);

        }
        //enable the follower button is disabled
        else if(showFollowerButton.isDisabled()) {
            showFollowerButton.setDisable(false);

        }

        //refresh the listview with the selected type (tweet)
        //if post list is empty, show the found nothing label and set the follow list view to invisible
        if(thisPerson.getPostList().isEmpty()) {
            foundLabel.setVisible(true);
            personListView.setVisible(false);

        }
        //else, something is found: refresh the component and set the follow list view to invisible.
        else {
            personListView.setVisible(false);
            postListView.setVisible(true);
            foundLabel.setVisible(false);
            refreshPostListView(thisPerson);

        }

        //disable the show tweet button
        showTweetButton.setDisable(true);
        focusButton.requestFocus();

    }

    //Method that once called, will display all the follower of the current user client
    public void showFollower() {
        //fetch this person
        Person thisPerson = GlobalSingleton.getGlobalSingleton().getThisPerson();

        //enable the showtweet button if disabled
        if(showTweetButton.isDisabled()) {
            showTweetButton.setDisable(false);

        }
        //enable the show following button if disabled
        else if(showFollowingButton.isDisabled()) {
            showFollowingButton.setDisable(false);

        }

        //refresh the listview with the selected elements: (follower)
        //if follower's list is empty, show the found nothing label and set the post list view to invisible
        if(thisPerson.getFollowers().isEmpty()) {
            foundLabel.setVisible(true);
            personListView.setVisible(false);
            postListView.setVisible(false);
        }
        //else, something is found: set the tweet list view to invisible and refresh the components
        else {
            postListView.setVisible(false);
            personListView.setVisible(true);
            foundLabel.setVisible(false);
            refreshUserListView(thisPerson.getFollowers());
        }

        //disable the show follower button
        showFollowerButton.setDisable(true);
        focusButton.requestFocus();

    }

    //Method that once called, will display all the followings of the current user client
    public void showFollowing() {
        //fetch this person
        Person thisPerson = GlobalSingleton.getGlobalSingleton().getThisPerson();

        //enable the show follower button if disabled
        if(showFollowerButton.isDisabled()) {
            showFollowerButton.setDisable(false);
        }
        //enable the showtweet button if disabled
        else if(showTweetButton.isDisabled()) {
            showTweetButton.setDisable(false);
        }

        //refresh the listview with the selected elements: (followings)
        //if following's list is empty, show the found nothing label and set the post list view to invisible
        if(thisPerson.getFollowings().isEmpty()) {
            foundLabel.setVisible(true);
            personListView.setVisible(false);
            postListView.setVisible(false);
        }
        //else, something is found: set the tweet list view to invisible and refresh the components
        else {
            postListView.setVisible(false);
            personListView.setVisible(true);
            foundLabel.setVisible(false);;
            refreshUserListView(thisPerson.getFollowings());
        }

        //disable the show following button
        showFollowingButton.setDisable(true);
        focusButton.requestFocus();

    }

    //Method that will refresh all the component into the scene
    public void refresh() {

        //get the dataloader instance and call it's refresh method
        DataLoader dataLoader = DataLoader.getDataLoader();
        dataLoader.refresh();
        refreshComponents(GlobalSingleton.getGlobalSingleton().getThisPerson());

        //fade in refreshed label and reset focus
        handler.displayMessage("Refreshed!", Color.rgb(93,188,210), communicationLabel);

        //show tweet
        showTweet();
        focusButton.requestFocus();

    }

    //Method that will aid the user to change the password into the personal scene
    public void changePassword() {
        //Get the current user and init the textinput dialog
        String thisUsername = GlobalSingleton.getGlobalSingleton().getAccountName();
        TextInputDialog passwordDialog = new TextInputDialog("Password here");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        passwordDialog.setTitle("Change your password");
        passwordDialog.setContentText("Current Password:");

        //get the password from the input field
        Optional<String> password = passwordDialog.showAndWait();


        //if the user inserted a password
        if(password.isPresent()) {
            //query the database to check if the password inserted is good
            DataLoader loader = DataLoader.getDataLoader();
            ResultSet resultSet = loader.genericSearchQuery("SELECT * FROM ACCOUNT WHERE USERNAME=\""+thisUsername+"\" AND PASSWORD=\""+password.get() + "\";");
            try {
                //if there is a next element, that means that the query returned a value: password is valid
                if (resultSet.next()) {
                    //show the set the new password dialog and fetch the new password
                    passwordDialog.setContentText("Your new password:");
                    password = passwordDialog.showAndWait();

                    //if the password inputted is present
                    if(password.isPresent()) {
                        //update the user password with a query and display the success alert
                        if (loader.genericUpdateQuery("UPDATE ACCOUNT SET PASSWORD=\""+password.get()+"\" WHERE USERNAME=\""+thisUsername+"\";")) {
                            alert.setTitle("Success!");
                            alert.setContentText("Your password was successfully changed.");
                            alert.showAndWait();

                        }
                        //else, something went wrong querying the database: notify the user
                        else {
                            alert.setTitle("Bad result");
                            alert.setContentText("Something went wrong. Try again.");
                            alert.showAndWait();

                        }
                    }
                    //else, the new password was not present: notify the client
                    else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Empty fields");
                        alert.setContentText("Your new password can't be empty.");
                        alert.showAndWait();

                    }
                }
                //if no next result, it means that the password inserted was not correct: notify the client
                else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Wrong password");
                    alert.setContentText("Your password is wrong.");
                    alert.showAndWait();

                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        //else, user didn't insert a password. notify the not empty error
        else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty fields");
            alert.setContentText("Your current password can't be empty.");
            alert.showAndWait();

        }
    }

}
