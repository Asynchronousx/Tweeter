package Twitter.SceneNavigator;

import Twitter.Util.AccessModifiers;
import Twitter.Util.Methods;
import Twitter.Util.Singleton;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Singleton
//Windows singleton that will hold important values such as root main stage, root twitter stage and root admin stage.
public class WindowSingleton {
    public static WindowSingleton windowSingleton;
    private Stage rootStage;
    private Scene rootScene;
    private Stage rootTweeterStage;
    private Scene rootTweeterScene;
    private Stage rootAdminStage;
    private Scene rootAdminScene;

    @Methods
    //Lazy init for getting the singleton
    public static WindowSingleton getWindowSingleton() {
        if(windowSingleton == null) {
            windowSingleton = new WindowSingleton();
        }

        return windowSingleton;

    }

    @AccessModifiers
    public Stage getRootStage() {
        return rootStage;
    }
    public Scene getRootScene() {
        return rootScene;
    }
    public Stage getRootTweeterStage() { return rootTweeterStage; }
    public Scene getRootTweeterScene() { return rootTweeterScene; }
    public Stage getRootAdminStage() { return rootAdminStage; }
    public Scene getRootAdminScene() { return rootAdminScene; }

    public void setRootScene(Scene rootScene) {
        this.rootScene = rootScene;
    }
    public void setRootStage(Stage rootStage) {
        this.rootStage = rootStage;
    }
    public void setRootTweeterScene(Scene rootTweeterScene) { this.rootTweeterScene = rootTweeterScene; }
    public void setRootTweeterStage(Stage rootTweeterStage) { this.rootTweeterStage = rootTweeterStage; }
    public void setRootAdminScene(Scene rootAdminScene) { this.rootAdminScene = rootAdminScene; }
    public void setRootAdminStage(Stage rootAdminStage) { this.rootAdminStage = rootAdminStage; }
}
