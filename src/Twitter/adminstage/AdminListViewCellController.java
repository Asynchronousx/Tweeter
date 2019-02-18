package Twitter.adminstage;

import Twitter.DatabaseUtil.DataLoader;
import Twitter.Util.Attributes;
import Twitter.twitterstage.model.Person;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Optional;

public class AdminListViewCellController extends ListCell<Person> {

    @Attributes(value="FXML")
    @FXML Button banButton;
    @FXML Button resetPSWButton;
    @FXML Circle circledUserImage;
    @FXML Label nameLabel;
    @FXML Label dateLabel;

    @Attributes
    private AdminListViewCellController controller ;
    private Parent graphic;

    @Override
    protected void updateItem(Person person, boolean b) {
        super.updateItem(person, b);

        if(person == null || b) {
            setGraphic(null);
        } else {
            //Try to load the graphics
            try {
                FXMLLoader loader  = new FXMLLoader(getClass().getResource("adminCell.fxml"));
                graphic = loader.load();
                controller = loader.getController();

            } catch (Exception e) {
                e.printStackTrace();

            }

            //Set the components
            controller.circledUserImage.setFill(new ImagePattern(person.getImage()!=null? person.getImage(): new Image("Twitter/resources/IMG/Placeholder.png")));
            controller.nameLabel.setText(person.getName());
            controller.dateLabel.setText(person.getJoinedDate());

            //Init graphic
            setGraphic(graphic);

        }

    }

    public void ban() {
        DataLoader loader = new DataLoader();
        String query;
        Boolean result;

        //Init the alert
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("User ban");
        alert.setContentText("Something went wrong. try again.");

        //Delete the account entry
        query = "DELETE FROM ACCOUNT WHERE USERNAME=\""+nameLabel.getText()+"\";";
        System.out.println(query);
        result = loader.genericUpdateQuery(query);

        //check if successful
        if(result) {
            //Now delete all the entry in the follower
            query = "DELETE FROM FOLLOWER WHERE FOLLOWERUSERNAME=\""+nameLabel.getText()+"\";";
            result = loader.genericUpdateQuery(query);

            //delete all the entry in the following
            query = "DELETE FROM FOLLOWING WHERE FOLLOWINGUSERNAME=\""+nameLabel.getText()+"\";";
            result = loader.genericUpdateQuery(query);

        } else {
            //prompt the alert
            alert.showAndWait();
            return;

        }

        //check if the last query was successful: if yes, the deletion was submitted.
        if(result) {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("User ban");
            alert.setContentText("User " + nameLabel.getText() + " successfully banned.");
            alert.showAndWait();

        }
        //prompt the unsuccessful query message
        else {
            alert.showAndWait();
            return;

        }

    }

    public void resetPSW() {
        //Init the variables
        TextInputDialog resetPsw = new TextInputDialog("Insert new password here");
        resetPsw.setTitle("Password Reset");
        resetPsw.setContentText("New password: ");
        Optional<String> newPsw = resetPsw.showAndWait();

        Alert alert;

        //if the password is present
        if(newPsw.isPresent()) {
            //update the password
            DataLoader loader = new DataLoader();
            String query = "UPDATE ACCOUNT SET PASSWORD=\""+newPsw.get()+"\" WHERE USERNAME=\""+nameLabel.getText()+"\";";
            if(loader.genericUpdateQuery(query)) {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Password Reset");
                alert.setContentText("Password resetted successfully.");
                alert.showAndWait();

            }
            //show the error of unsuccessful query
            else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Reset Password");
                alert.setContentText("Could not reset the password.");
                alert.showAndWait();

            }
        }
        //tell the user that a password should be present
        else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Reset Passowrd");
            alert.setContentText("Password field can't be empty.");
            alert.showAndWait();

        }
    }


}

