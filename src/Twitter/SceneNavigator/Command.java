package Twitter.SceneNavigator;

//For navigating the scene, we'll implement the Command pattern,
//To decouple the object from their implementation.

//Command interface
public interface Command {
    void execute();
}
