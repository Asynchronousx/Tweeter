package Twitter.Navigation;

import javafx.scene.Parent;

import java.io.IOException;

//NavigationStrategy is the interface of the Strategy methods that will help us into the navigation of our app.
//We choosed Strategy template pattern (in combination with Commands) to help us into the navigation routine of the
//application. This interface present a method called goToScene that once called, will lead the user to a new scene.
//We've also defined an enum to know where we want to go: LOGIN form and HOME form represent the root: MAIN AND FEED
//represent the secondary scenes of the program (like forgot password, signup, search, personal page etc..)
public interface NavigationStrategy {
    void goToScene(Parent root) throws IOException;

    //Type of windows we're moving on
    enum WINDOWS {
        LOGIN,
        HOME,
        MAIN,
        FEED
    }
}
