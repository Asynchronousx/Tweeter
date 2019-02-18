package Twitter.SceneNavigator;

import Twitter.Util.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Receiver:
//Main window is the receiver of the command pattern. it will holds all the element to perform the navigation
//Inside our application, holding three fundamental object:
//rootStage: is the stage we're loading, it will be the anchor of the navigation between scenes of that specific "stage" of the application (main-tweeter-admin)
//rootScene: the principal scene of the root stage.
//nextScene: the next scene we want navigate into.
//Width/Height: size of the new window created from the root stage.
public class MainWindow {
    @Attributes(value = "Private")
    private Stage rootStage;
    private Scene rootScene;
    private Scene nextScene;
    private String title;
    private final int width;
    private final int height;

    @Constructor
    public MainWindow(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @AccessModifiers
    public Scene getRootScene() { return this.rootScene; }
    public Stage getRootStage() { return this.rootStage; }
    public Scene getNextScene() { return nextScene; }
    public String getTitle() { return title; }
    public int getHeight() { return height; }
    public int getWidth() { return width; }

    public void setRootStage(Stage rootStage) {
        this.rootStage = rootStage;
    }
    public void setRootScene(Scene rootScene) {
        this.rootScene = rootScene;
    }
    public void setNextScene(Scene nextScene) { this.nextScene = nextScene; }
    public void setTitle(String title) { this.title = title; }
}

