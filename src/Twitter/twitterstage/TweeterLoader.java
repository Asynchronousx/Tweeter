package Twitter.twitterstage;

import Twitter.SceneNavigator.WindowSingleton;
import Twitter.Util.WindowSizer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

//TweeterLoader class: called at the transition from the main stage to the tweeter stage, when the login button have success.
//It loads the new tweeter stage and save them into the windows singleton, to make them retrievable whenever.
public class TweeterLoader implements WindowSizer {
    public void loadStage(String resource, int minW, int minH, int maxW, int maxH) throws IOException {
        Parent form = FXMLLoader.load(getClass().getResource(resource));
        Scene scene = new Scene(form, minW, minH);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.setTitle("Tweeter - Home");
        stage.getIcons().add(new Image("Twitter/resources/IMG/tweeterlogo.png"));
        anchorSize(stage, minW, minH, maxW, maxH);
        WindowSingleton.getWindowSingleton().getRootStage().close();
        WindowSingleton.getWindowSingleton().setRootTweeterStage(stage);
        WindowSingleton.getWindowSingleton().setRootTweeterScene(scene);
        stage.show();
    }
}
