package Twitter.Navigation;

import Twitter.SceneNavigator.*;
import Twitter.Util.Attributes;
import Twitter.Util.Constructor;
import Twitter.Util.Methods;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

//GoToNextScene is the class that perform the action of going into another scene that isn't the root.
//It will hold two attributes, main window and windows navigator to perform the navigation.
//Also, in the constructor we specify that attributes, to make the navigation performable.
//In the method goToScene we set the next scene the main windows shoul load, and send the main window
//to be executed into the windowsnavigator invoker, that will call it's function to perform the navigation.
public class GoToNextScene implements NavigationStrategy {
    @Attributes
    private WindowsNavigator windowsNavigator;
    private MainWindow mainWindow;

    @Constructor
    public GoToNextScene(WINDOWS where, String title, int width, int height) {
        //Init the components
        this.mainWindow = new MainWindow(width, height);
        this.windowsNavigator = new WindowsNavigator();

        //if whe need to go to some scene into the main stage
        if(where.equals(WINDOWS.MAIN)) {
            //Get the root main stage as the root stage of main window from the window singleton and set the title
            mainWindow.setRootStage(WindowSingleton.getWindowSingleton().getRootStage());
            mainWindow.setTitle("Tweeter - " + title);

        }
        //else, if we need to go to some scene into the tweeter stage
        else if(where.equals(WINDOWS.FEED)) {
            //Get the root tweeter stage as the root stage of main window from the window singleton and set the title
            mainWindow.setRootStage(WindowSingleton.getWindowSingleton().getRootTweeterStage());
            mainWindow.setTitle("Tweeter - " + title);

        }
    }

    @Override
    //Overriden method that will perform the transition from one scene to another
    public void goToScene(Parent root) {
        //se as next scene of the mainWindow a new scene containing the root FXML view file
        mainWindow.setNextScene(new Scene(root, mainWindow.getWidth(), mainWindow.getHeight()));
        //Perform the navigation with the navigator
        windowsNavigator.performNavigation("Search", new SetNextScene(mainWindow));
    }


}