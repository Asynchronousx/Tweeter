package Twitter;

import Twitter.SceneNavigator.WindowSingleton;
import Twitter.Util.WindowSizer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application implements WindowSizer {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainstage/twitterLogin.fxml"));
        root.getChildrenUnmodifiable().forEach((c) -> c.setFocusTraversable(false));
        Scene rootScene = new Scene(root, 700, 397);
        anchorSize(primaryStage, 700, 435, 700, 435);

        WindowSingleton windowSingleton = WindowSingleton.getWindowSingleton();
        windowSingleton.setRootScene(rootScene);
        windowSingleton.setRootStage(primaryStage);

        primaryStage.getIcons().add(new Image("Twitter/resources/IMG/tweeterlogo.png"));
        primaryStage.setTitle("Tweeter - Log in");
        primaryStage.setScene(rootScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
