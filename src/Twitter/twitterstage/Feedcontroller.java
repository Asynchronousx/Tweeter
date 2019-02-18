package Twitter.twitterstage;

import Twitter.DatabaseUtil.DataLoader;
import Twitter.DatabaseUtil.Facade;
import Twitter.DatabaseUtil.PersonLoader;
import Twitter.GlobalSingleton;
import Twitter.Navigation.*;
import Twitter.SceneNavigator.WindowSingleton;
import Twitter.Util.Attributes;
import Twitter.Util.MessageHandler;
import Twitter.Util.Methods;
import Twitter.twitterstage.CellManager.TweetListViewCellController;
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
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


//Controller of the main feed view. It is the core of our application, and will manage show tweet and information about the user.
public class Feedcontroller implements Initializable {

    @Attributes(value="FXML")
    @FXML Button focusButton;
    @FXML Button tweetButton;
    @FXML Button homeButton;
    @FXML Button meButton;
    @FXML Button searchButton;
    @FXML Button logoutButton;
    @FXML Button refreshButton;
    @FXML TextField hashTagTextField;
    @FXML TextField tweetTextField;
    @FXML TextField searchTextField;
    @FXML Circle circledProfileIMG;
    @FXML Circle circlePaneIMG;
    @FXML Label communicationLabel;
    @FXML Label nameLabel;
    @FXML Label foundLabel;
    @FXML Label smallNameLabel;
    @FXML Label tweetsNumberLabel;
    @FXML Label followerNumberLabel;
    @FXML Label followingNumberLabel;
    @FXML ListView<Post> tweetListView;

    @Attributes
    //We're declaring an observable list to take track of the post into the tweet section of the scene.
    private ObservableList<Post> observablePostList = FXCollections.observableArrayList();
    private MessageHandler handler;

    @Override
    //Method that initialize the view once called.
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //load the message handler
        handler = MessageHandler.getMessageHandler();

        //load person info through the person builder, and load those info into the components
        //refreshing them.
        Facade personBuilder = new PersonLoader();
        personBuilder.loadThisPerson();
        Person thisPerson = GlobalSingleton.getGlobalSingleton().getThisPerson();
        refreshComponents(thisPerson);

    }

    @Methods(value="Logic")
    //Method that refresh the components of the scene once called.
    private void refreshComponents(Person thisPerson) {

        //If the personal post or global post list is empty, display the nothing found label
        if(thisPerson.getPostList().isEmpty() && thisPerson.getGlobalPostList().isEmpty()) {
            foundLabel.setVisible(true);

        }
        //Otherwise, hide it
        else {
            foundLabel.setVisible(false);

        }

        //Refresh all the components with the fetched person info
        hashTagTextField.setStyle("-fx-text-inner-color: #1da1f2; -fx-background-color: transparent;");
        circledProfileIMG.setFill(new ImagePattern(thisPerson.getImage()));
        circlePaneIMG.setFill(new ImagePattern(thisPerson.getImage()));
        nameLabel.setText(thisPerson.getName());
        smallNameLabel.setText("@"+thisPerson.getName());
        followerNumberLabel.setText(Integer.toString(thisPerson.getFollowers().size()));
        followingNumberLabel.setText(Integer.toString(thisPerson.getFollowings().size()));
        tweetsNumberLabel.setText(Integer.toString(thisPerson.getPostList().size()));


        //refresh the listview displaying all the tweet with the information of this person
        refreshListView(thisPerson);
    }

    //Method that will refresh the tweet list view with the person info
    private void refreshListView(Person thisPerson) {
        //Clear the observable list and add the new fetched post to id
        observablePostList.clear();
        observablePostList.addAll(thisPerson.getGlobalPostList());

        //set the tweet list view item as null and then update the list adding the new fetched post
        tweetListView.setItems(null);
        tweetListView.setItems(observablePostList);

        //then call the cell factory to create every single cell from the list
        tweetListView.setCellFactory(cell -> new TweetListViewCellController());

    }

    //Method that, once a post is submitted, it will fetch all the hashtags from that post and store them
    //into the database.
    private List<String> fetchHashTag(String rawHashTag) {
        //Declaring a list to contain the hashtags
        List<String> hashTags = new ArrayList<>();
        //and a string that contains the formatted raw hashtags with a regexp
        String formattedRawHashtags = rawHashTag.replaceAll("[^\\\\dA-Za-z-0-9-#]", "");
        Integer counter;


        //remove malicious final hashtags (e.g: #hashtag# <- )
        //While the formatted hashtags end with another hashtags
        while(formattedRawHashtags.endsWith("#")) {
            //remove all the final hashtags
            formattedRawHashtags = formattedRawHashtags.substring(0,formattedRawHashtags.length()-1);
        }

        //format the hashtags into the list
        //for the lenght of the string
        for(int i=0; i<formattedRawHashtags.length(); i++) {
            //counter is i
            counter = i;
            //if the current character is an hashtag
            if(formattedRawHashtags.charAt(i) == '#') {
                //increase the counter to analyze the next char position
                counter++;
                //while the char at the current position is different from an hashtag
                while(formattedRawHashtags.charAt(counter) != '#') {
                    //increase the counter
                    counter++;
                    //if the counter now is equal to the string size
                    if(counter==formattedRawHashtags.length()) {
                        //break the cycle
                        break;

                    }

                }

                //once here, we met another hashtag, so insert into the hashtag list the
                //substring from i to the counter
                hashTags.add(formattedRawHashtags.substring(i, counter));
                //i is now equal to counter-1 to start analyzing from the last hashtag
                i = counter-1;
            }
        }

        //If hashtag size if > 0,
        //      and if hashtags size <= 5, return the hashtags list, otherwise null
        //otherwise hashtags (depending on the state (null, empty, populated) will be performed an action).
        return hashTags.size()> 0? (hashTags.size()<=5? hashTags: null): hashTags;

    }


    @Methods
    //Action that once performed, will lead us to the search scene.
    public void goToSearch() throws IOException {

        //if the search textfield is empty, display the error to the user and return
        if(searchTextField.getText().equals("")) {
            handler.displayMessage("Search field cannot be empty.", Color.rgb(183,29,53), communicationLabel);
            return;

        }
        //else, the user is searching himself: display the error and return
        else if(searchTextField.getText().equals(GlobalSingleton.getGlobalSingleton().getAccountName())) {
            handler.displayMessage("You cannot search yourself.", Color.rgb(183,29,53), communicationLabel);
            return;

        }
        //else the query was correct
        else {
            //Init the query and reset the text
            GlobalSingleton.getGlobalSingleton().setTmpPersonSearched(searchTextField.getText());
            GlobalSingleton.getGlobalSingleton().setSearchQuery(searchTextField.getText());
            searchTextField.setText("");
        }

        Parent searchForm = FXMLLoader.load(getClass().getResource("searchFeed.fxml"));
        NavigationStrategy goToSearch = new GoToNextScene(NavigationStrategy.WINDOWS.FEED, "Search", 880, 873);
        goToSearch.goToScene(searchForm);
    }

    //Action that once performed, will lead us to the personal scene.
    public void goToPersonal() throws IOException {
        Parent personalForm = FXMLLoader.load(getClass().getResource("personalFeed.fxml"));
        NavigationStrategy goToSearch = new GoToNextScene(NavigationStrategy.WINDOWS.FEED, "Personal", 880, 873);
        goToSearch.goToScene(personalForm);
    }

    //Action that once performed, will lead us to the login scene.
    public void logOut() throws IOException {
        NavigationStrategy goToLogin = new GoToRootScene(NavigationStrategy.WINDOWS.LOGIN);
        WindowSingleton.getWindowSingleton().getRootTweeterStage().close();
        goToLogin.goToScene(null);
    }

    //Action that once performed, will post a new tweet into the feed.
    public void tweet() {
        //Fetch all the tweet data
        //Get the person who tweeeted
        Person thisPerson = GlobalSingleton.getGlobalSingleton().getThisPerson();

        //Get the account name of that person
        String whoTweeted = thisPerson.getName();

        //Get the tweet text
        String tweetText = tweetTextField.getText();

        //Fetch the hashtags from the inserted hashtags string
        List<String> hashTags = fetchHashTag(hashTagTextField.getText());

        //Feth the date time formatted in a MySQL flavour
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);

        //tweet id
        Integer tweetId;

        //check if the tweet was empty, if so display the error message and return.
        if(tweetText.isEmpty()) {
            handler.displayMessage("Tweets cannot be empty.", Color.rgb(183,29,53), communicationLabel);
            return;

        }
        //check if the tweettext was > 70 char, if so display the error message and return.
        else if(tweetText.length() > 70 || tweetText.isEmpty()) {
            handler.displayMessage("Tweet length excedeed: Should be minor than 70 words.", Color.rgb(183,29,53), communicationLabel);
            return;

        }
        //check if the hashtag were null, if so display the error message and return
        else if(hashTags == null) {
            handler.displayMessage("Hashtags cannot be more than 5. Try again!", Color.rgb(183,29,53), communicationLabel);
            return;

        }
        //Check if the hashtags are empty, if so display the error message and return
        else if(hashTags.isEmpty()) {
            handler.displayMessage("Hashtags not recognized. Try with '#hashtag' separed by a space.", Color.rgb(183,29,53),communicationLabel);
            return;

        }

        //Getting the data loader
        DataLoader dataLoader = DataLoader.getDataLoader();

        //Inserting the post and returning the latest tweet id assigned to it
        tweetId = dataLoader.insertPost(whoTweeted, tweetText, hashTags, date);

        //inserting the post into the user personal post list and global list
        Post newPost = new Post(tweetId, whoTweeted, tweetText, hashTags, date);
        thisPerson.getPostList().add(newPost);
        thisPerson.getGlobalPostList().add(newPost);

        //sort the lists by date
        thisPerson.getPostList().sort((p1, p2) -> p2.getDateTweeted().compareTo(p1.getDateTweeted()));
        thisPerson.getGlobalPostList().sort((p1, p2) -> p2.getDateTweeted().compareTo(p1.getDateTweeted()));

        //display the tweeted message
        handler.displayMessage("Tweeted!", Color.rgb(29,161,242), communicationLabel);

        //update the list view
        refreshListView(GlobalSingleton.getGlobalSingleton().getThisPerson());

        //set the nothing found to false (because we've just inserted a post)
        foundLabel.setVisible(false);

        //delete text from hashtag and tweet text field and request focus.
        tweetTextField.setText("");
        hashTagTextField.setText("");
        focusButton.requestFocus();

    }

    //Action that once performed, will refresh all the content in the current scene.
    public void refresh() {

        //call refresh from data loader and refresh the components
        DataLoader dataLoader = DataLoader.getDataLoader();
        dataLoader.refresh();
        refreshComponents(GlobalSingleton.getGlobalSingleton().getThisPerson());

        //fade in refreshed label and reset focus to communicate the user the refresh
        handler.displayMessage("Refreshed!", Color.rgb(93,188,210), communicationLabel);
        focusButton.requestFocus();

    }
}
