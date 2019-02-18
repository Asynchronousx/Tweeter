package Twitter.SceneNavigator;

import Twitter.Util.*;

//Concrete command that implement the change of the scene: it will hold the main window
//to know where to go, and once the method execute is performed, it will go to the next scene.
public class SetNextScene implements Command {
    @Attributes
    private MainWindow mainWindow;

    @Constructor
    public SetNextScene(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    //Method that will set the next scene of the stage: it will be performed thanks to the info
    //contained into the main window (root stage and scene, and next scene).
    public void execute() {
        //Get the root stage and set the current scene as the next scene
        mainWindow.getRootStage().setScene(mainWindow.getNextScene());

        //set the title
        mainWindow.getRootStage().setTitle(mainWindow.getTitle());

        //show the scene
        mainWindow.getRootStage().show();

    }
}
