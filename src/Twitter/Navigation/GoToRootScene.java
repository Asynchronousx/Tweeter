package Twitter.Navigation;

import Twitter.SceneNavigator.MainWindow;
import Twitter.SceneNavigator.WindowSingleton;
import Twitter.SceneNavigator.WindowsNavigator;
import Twitter.SceneNavigator.setRootScene;
import Twitter.Util.Attributes;
import Twitter.Util.Constructor;
import javafx.scene.Parent;

//Go to root scene is the class that will perform the navigation back to the root scene of the stage.
//It will hold two element, MAINWINDOWS & WINDOWSNAVIGATOR, which are defined into the command pattern into another package.
//The class implement the constructor to know which stage should be loaded (HOME OR LOGIN), where home represent the feed, and login
//represent the first view of the program.
//It will use the windows navigator to perform the command of going back to the root.
public class GoToRootScene implements NavigationStrategy {
    @Attributes
    private MainWindow mainWindow;
    private WindowsNavigator windowsNavigator;

    @Constructor
    public GoToRootScene(WINDOWS where) {
        //Init the window navigator
        windowsNavigator = new WindowsNavigator();

        //Defining variables: where we need to go?
        //If into the HOME (root stage of tweeter stage)
        if(where.equals(WINDOWS.HOME)) {
            //Init a new window with the desired W/H and set root stage and scene as tweeter stage and scene.
            mainWindow = new MainWindow(880, 873);
            mainWindow.setRootStage(WindowSingleton.getWindowSingleton().getRootTweeterStage());
            mainWindow.setRootScene(WindowSingleton.getWindowSingleton().getRootTweeterScene());
            mainWindow.setTitle("Tweeter - Home");
        }
        //If into the login (root stage of main stage)
        else if (where.equals(WINDOWS.LOGIN)){
            //Init a new window with the desired W/H and set root stage and scene as main stage and scene.
            mainWindow = new MainWindow(700, 397);
            mainWindow.setRootStage(WindowSingleton.getWindowSingleton().getRootStage());
            mainWindow.setRootScene(WindowSingleton.getWindowSingleton().getRootScene());
            mainWindow.setTitle("Tweeter - Log in");
        }
    }

    @Override
    //Overriden method that will perform the navigation to the root
    public void goToScene(Parent root) {
        windowsNavigator.performNavigation("Root", new setRootScene(mainWindow));
    }
}
