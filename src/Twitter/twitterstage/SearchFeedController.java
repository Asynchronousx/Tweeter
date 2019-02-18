package Twitter.twitterstage;

import Twitter.DatabaseUtil.DataLoader;
import Twitter.GlobalSingleton;
import Twitter.Navigation.*;
import Twitter.SceneNavigator.WindowSingleton;
import Twitter.Util.Attributes;
import Twitter.Util.MessageHandler;
import Twitter.Util.Methods;
import Twitter.twitterstage.CellManager.CellLoaderStrategy;
import Twitter.twitterstage.CellManager.PersonCellLoader;
import Twitter.twitterstage.CellManager.TweetCellLoader;
import Twitter.twitterstage.model.Person;
import Twitter.twitterstage.model.Post;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//Controller that will manage the search view.
public class SearchFeedController implements Initializable {

    @Attributes(value="FXML")
    @FXML Button focusButton;
    @FXML Button homeButton;
    @FXML Button meButton;
    @FXML Button searchButton;
    @FXML Button logoutButton;
    @FXML Button refreshButton;
    @FXML Circle circledProfileIMG;
    @FXML TextField searchTextField;
    @FXML Label nameLabel;
    @FXML Label foundLabel;
    @FXML Label smallNameLabel;
    @FXML Label followerNumberLabel;
    @FXML Label followingNumberLabel;
    @FXML Label tweetsNumberLabel;
    @FXML Label communicationLabel;
    @FXML Label searchedElementLabel;
    @FXML Label smallSearchedElementLabel;
    @FXML ListView searchListView;

    @Attributes
    //Observables list to take track of post and person searched
    private ObservableList<Post> observablePostList = FXCollections.observableArrayList();
    private ObservableList<Person> observablePersonList = FXCollections.observableArrayList();
    private MessageHandler handler = MessageHandler.getMessageHandler();

    @Override
    //Method that will be called at the presentation of the scene, initializing the person and the loader strategy based on the query
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Init person, result and cell loader
        Person thisPerson = GlobalSingleton.getGlobalSingleton().getThisPerson();
        Integer result;
        CellLoaderStrategy cellLoader;

        //refresh components
        refreshComponents(thisPerson);

        //Check if the search query was for a person or for an hashtag
        String searchQuery = GlobalSingleton.getGlobalSingleton().getSearchQuery();

        //if the query was for an hashtag
        if(searchQuery.charAt(0) == '#') {
            //Case if an hashtag
            //Init the components
            searchedElementLabel.setText(searchQuery);
            smallSearchedElementLabel.setText("Result for hashtag: " + searchQuery);

            //Call the apposite strategy cell loader
            cellLoader = new TweetCellLoader();

            //Result contain the outcome of the operation
            result = cellLoader.loadCell(observablePostList, searchListView);

            //Check result
            checkSearchResult(result);

        }
        //else the query was for a person
        else {
            //case if person
            //init components
            searchedElementLabel.setText(searchQuery);
            smallSearchedElementLabel.setText("Result for user: " + searchQuery);

            //call the apposite strategy cell loader
            cellLoader = new PersonCellLoader();

            //result now contain the outcome of the operation
            result = cellLoader.loadCell(observablePersonList, searchListView);

            //check the result
            checkSearchResult(result);

        }


    }

    @Methods(value="Logic")
    //Method that once called, will refresh the component of the scene
    private void refreshComponents(Person thisPerson) {
        //refresh the components present into the scene
        circledProfileIMG.setFill(new ImagePattern(thisPerson.getImage()));
        nameLabel.setText(thisPerson.getName());
        smallNameLabel.setText("@"+thisPerson.getName());
        followerNumberLabel.setText(Integer.toString(thisPerson.getFollowers().size()));
        followingNumberLabel.setText(Integer.toString(thisPerson.getFollowings().size()));
        tweetsNumberLabel.setText(Integer.toString(thisPerson.getPostList().size()));
        foundLabel.setVisible(false);
    }

    //Method that will check the query search result and display message in special cases
    private void checkSearchResult(Integer result) {
        //Init a message handler to show faded messages.
        MessageHandler handler = MessageHandler.getMessageHandler();

        //Switch the result
        switch (result) {
            //if -1, query was not performed, so we need to tell that something went wrong and display the nothing found label
            case -1:
                //An error occurred. display the message and set the found nothing label
                handler.displayMessage("Something went wrong. Try again.", Color.rgb(183,29,53), communicationLabel);
                foundLabel.setVisible(true);
            //if 0, no result where found for that given query: set the not found label visibleì
            case 0:
                //No value found. set the no found value
                foundLabel.setVisible(true);

        }
    }

    @Methods
    //Method that once performed, will lead the user to another search scene:
    //the user can also search something into the search scene itself, so we need to implement that method here too.
    public void goToSearch() throws IOException {

        //if search textfield is empty, return
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
            //Init the query and reset the text
            GlobalSingleton.getGlobalSingleton().setTmpPersonSearched(searchTextField.getText());
            GlobalSingleton.getGlobalSingleton().setSearchQuery(searchTextField.getText());
            searchTextField.setText("");
        }

        //move to another search scene
        Parent searchForm = FXMLLoader.load(getClass().getResource("searchFeed.fxml"));
        NavigationStrategy goToSearch = new GoToNextScene(NavigationStrategy.WINDOWS.FEED, "Search", 880, 873);
        goToSearch.goToScene(searchForm);
    }

    //Method that once performed, will lead the user to the home root stage
    public void goToHome() throws IOException {
        NavigationStrategy goToHome = new GoToRootScene(NavigationStrategy.WINDOWS.HOME);
        goToHome.goToScene(null);
    }

    //Method that once performed, will lead the user to the personal scene
    public void goToPersonal() throws IOException {
        Parent personalForm = FXMLLoader.load(getClass().getResource("personalFeed.fxml"));
        NavigationStrategy goToPersonal = new GoToNextScene(NavigationStrategy.WINDOWS.FEED, "Personal", 880, 873);
        goToPersonal.goToScene(personalForm);
    }

    //Method that once performed, will lead the user to the login main scene
    public void logOut() throws IOException {
        NavigationStrategy goToLogin = new GoToRootScene(NavigationStrategy.WINDOWS.LOGIN);
        WindowSingleton.getWindowSingleton().getRootTweeterStage().close();
        goToLogin.goToScene(null);
    }

    //Method that once called, will refresh all the component present into the scene
    public void refresh() {
        //call refresh from data loader
        DataLoader dataLoader = DataLoader.getDataLoader();
        dataLoader.refresh();
        refreshComponents(GlobalSingleton.getGlobalSingleton().getThisPerson());

        //fade in refreshed label and reset focus
        handler.displayMessage("Refreshed!", Color.rgb(93,188,210), communicationLabel);
        focusButton.requestFocus();

    }

}
