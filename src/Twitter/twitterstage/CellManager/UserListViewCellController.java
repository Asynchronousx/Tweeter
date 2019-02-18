package Twitter.twitterstage.CellManager;

import Twitter.DatabaseUtil.DataLoader;
import Twitter.GlobalSingleton;
import Twitter.Util.Attributes;
import Twitter.Util.Methods;
import Twitter.twitterstage.model.Person;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

//Controller class of a specific cell of the post list view.
public class UserListViewCellController extends ListCell<Person> {

    @Attributes(value="FXML")
    @FXML Button followButton;
    @FXML Button focusButton;
    @FXML Button unfollowButton;
    @FXML Label nameLabel;
    @FXML Circle circledUserImage;

    @Attributes
    private UserListViewCellController controller ;
    private Parent graphic;

    @Override
    //Method called to update an item whenever is present.
    protected void updateItem(Person person, boolean b) {
        super.updateItem(person, b);

        //If no post where found, set the graphic as null because we don't want to display empty cells.
        if(person == null || b ) {
            setGraphic(null);

        }
        //Else, if something found, update the graphic and set the component of the cell with the post element found.
        else {
            try {
                //Try to load the cell fxml and get the controller
                FXMLLoader loader  = new FXMLLoader(getClass().getResource("userCell.fxml"));
                graphic = loader.load();
                controller = loader.getController();

            } catch (Exception e) {
                e.printStackTrace();

            }

            //initialize the variables needed to populate the cell components
            String thisUsername = GlobalSingleton.getGlobalSingleton().getThisPerson().getName();
            String personUsername = person.getName();
            DataLoader loader = DataLoader.getDataLoader();

            //Set the textLabel
            controller.nameLabel.setText(personUsername);

            //Check if the generic cell user follow the client user: if so, disable the follow button (because already followed)
            //otherwise, disable the unfollow button (because already not followed)
            if(loader.checkIfFollows(thisUsername, personUsername)) {
                controller.followButton.setDisable(true);

            } else {
                controller.unfollowButton.setDisable(true);

            }

            //get image url of the user and display it if present
            controller.circledUserImage.setFill(new ImagePattern(person.getImage()!=null? person.getImage(): new Image("Twitter/resources/IMG/Placeholder.png")));

            //Set the current graphic cell
            setGraphic(graphic);

        }
    }


    @Methods
    //Method that once performed, will follow the generic cell user
    public void followPerson() {
        //Get the person name from it's label and init the data loader
        DataLoader loader = DataLoader.getDataLoader();
        String personToFollow = nameLabel.getText();
        String query;

        //Insert into following of the user the user we want to follow: ThisPerson -follows-> PersonToFollow
        query = "INSERT INTO FOLLOWING VALUES(\""+personToFollow+"\",\""+GlobalSingleton.getGlobalSingleton().getAccountName()+"\");";
        loader.genericUpdateQuery(query);

        //Update the follower list of the followed user: PersonToFollow -isfollowedby-> ThisPerson
        query = "INSERT INTO FOLLOWER VALUES(\""+GlobalSingleton.getGlobalSingleton().getAccountName()+"\",\""+personToFollow+"\");";
        loader.genericUpdateQuery(query);

        //disable the follow button
        followButton.setDisable(true);

        //if unfollow button was disabled, now enable it
        if(unfollowButton.isDisabled()) {
            unfollowButton.setDisable(false);
            focusButton.requestFocus();

        }

        //add the person we just followed into the following list
        GlobalSingleton.getGlobalSingleton().getThisPerson().getFollowings().add(loader.returnPerson(personToFollow));

    }

    //Method that once performed, will unfollow a given person.
    public void UnfollowPerson() {
        //Get the person name from it's label and init the data loader
        DataLoader loader = DataLoader.getDataLoader();
        String personToUnfollow = nameLabel.getText();
        String query;

        //Delete the following user: ThisPerson -unfollow-> PersonToUnfollow
        query = "DELETE FROM FOLLOWING WHERE FOLLOWINGUSERNAME=\""+personToUnfollow+"\"AND USERNAME=\""+GlobalSingleton.getGlobalSingleton().getAccountName()+"\";";
        loader.genericUpdateQuery(query);

        //Delete the follower from the unfollowed user PersonToUnfollow -isnotfollowedanymoreby-> ThisPerson
        query = "DELETE FROM FOLLOWER WHERE FOLLOWERUSERNAME=\""+GlobalSingleton.getGlobalSingleton().getAccountName()+"\"AND USERNAME=\""+personToUnfollow+"\";";
        loader.genericUpdateQuery(query);

        //disable the unfollow button
        unfollowButton.setDisable(true);

        //if follow button was disabled, now enable it
        if(followButton.isDisabled()) {
            followButton.setDisable(false);
            focusButton.requestFocus();

        }

    }


}