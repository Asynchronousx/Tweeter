package Twitter.twitterstage.CellManager;

import Twitter.DatabaseUtil.DataLoader;
import Twitter.GlobalSingleton;
import Twitter.Util.Attributes;
import Twitter.twitterstage.model.Person;
import Twitter.twitterstage.model.Post;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.Optional;

//Controller class of a specific cell of the post list view.
public class TweetListViewCellController extends ListCell<Post> {

    @Attributes(value="FXML")
    @FXML Circle circledUserImage;
    @FXML MenuButton dropDownButton;
    @FXML HBox hashtagHbox;
    @FXML Label tweetLabel;
    @FXML Label nameLabel;
    @FXML Label dateLabel;
    @FXML Label tweetIDLabel;
    @FXML AnchorPane anchorPane;

    @Attributes
    private TweetListViewCellController controller ;
    private Parent graphic;

    @Override
    //Method called to update an item whenever is present.
    protected void updateItem(Post post, boolean b) {
        super.updateItem(post, b);

        //If no post where found, set the graphic as null because we don't want to display empty cells.
        if(post == null || b ) {
            setGraphic(null);

        }
        //Else, if something found, update the graphic and set the component of the cell with the post element found.
        else {
            try {
                //Try to load the cell fxml and get the controller
                FXMLLoader loader  = new FXMLLoader(getClass().getResource("tweetCell.fxml"));
                graphic = loader.load();
                controller = loader.getController();

            } catch (Exception e) {
                e.printStackTrace();

            }

            //For each hashtag into the hashTags list
            post.getHashTags().forEach(h -> {
                //create a new label containing the hashtag text
                Label l = new Label(h);
                //set the color of the label text
                l.setTextFill(Color.rgb(29,161,242));
                //add the hashtag label to the hashtag HBox
                controller.hashtagHbox.getChildren().add(l);
            });

            //to enable delete post, check if the user got the right to delete it (if is the owner).
            Person thisPerson = GlobalSingleton.getGlobalSingleton().getThisPerson();
            String whoTweeted = post.getWhoTweeted().toLowerCase();
            String currentUser = GlobalSingleton.getGlobalSingleton().getAccountName().toLowerCase();

            //if the user who tweeted the post is the client user
            if(whoTweeted.equals(currentUser)) {
                //fetch his image
                controller.circledUserImage.setFill(new ImagePattern(thisPerson.getImage()!=null? thisPerson.getImage(): new Image("Twitter/resources/IMG/Placeholder.png")));

            }
            //otherwise, we need to scan the following users to find it
            else {
                //set the dropdown button to false to avoid that a user could cancel another user post
                controller.dropDownButton.setVisible(false);

                //get the image of the generic cell user if present:
                //fetch the following list and scan for the person who tweeted that post.
                List<Person> followingLists = GlobalSingleton.getGlobalSingleton().getThisPerson().getFollowings();

                //for all the person in followingList, search for the user who tweeted it
                for(Person p: followingLists) {
                    //when found, if he got an image, charge it into the post otherwise charge the default img
                    if(p.getName().toLowerCase().equals(whoTweeted)) {
                        controller.circledUserImage.setFill(new ImagePattern(p.getImage()!=null? p.getImage(): new Image("Twitter/resources/IMG/Placeholder.png")));
                        break;
                    }
                }

            }

            controller.tweetLabel.setText(post.getTweetText());
            controller.nameLabel.setText(post.getWhoTweeted());
            controller.dateLabel.setText(post.getDateTweeted());
            controller.tweetIDLabel.setText(post.getTweetID().toString());

            //set the cell graphic
            setGraphic(graphic);

        }
    }


    //Method called from the drop down button on "Delete post".
    //It will delete a post from the database and from the entire user entry.
    public void deletePost() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the post?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> answer = alert.showAndWait();

        if(answer.isPresent()) {
            if(answer.get().equals(ButtonType.YES)) {
                //Delete from the post entity into the database
                DataLoader loader = DataLoader.getDataLoader();
                String query = "DELETE FROM POST WHERE TWEETID="+Integer.parseInt(tweetIDLabel.getText());
                loader.genericUpdateQuery(query);

                //delete from the post list
                GlobalSingleton.getGlobalSingleton().getThisPerson().removePost(Integer.parseInt(tweetIDLabel.getText()));

                //show the deletion confirmation
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Deletion successful");
                alert.setContentText("The post was deleted. Refresh the page!");
                alert.showAndWait();

            }
            else {
                return;
            }
        }
        else {
            return;
        }

    }


}

