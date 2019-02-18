package Twitter.SceneNavigator;

import Twitter.Util.*;

//Concrete Command that return to a root scene. It will hold a main window to know where the application wants to go.
public class setRootScene implements Command {
    @Attributes
    private MainWindow mainWindow;

    public setRootScene(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    //Method that will return to the root scene of the current stage (e.g: if into tweeter stage it will return to the home,
    //if into main it will return to the login etc).
    public void execute() {
        //get the root stage and set the current scene as the root scene
        mainWindow.getRootStage().setScene(mainWindow.getRootScene());

        //set the title
        mainWindow.getRootStage().setTitle(mainWindow.getTitle());

        //show the scene
        mainWindow.getRootStage().show();
    }
}
