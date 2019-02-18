package Twitter.SceneNavigator;

import Twitter.Util.*;

import java.util.HashMap;
import java.util.Map;

//Invoker of the command pattern, it will store all the commands into the command map and call the abstract command
//method execute to perform an action.
public class WindowsNavigator {
    @Attributes
    private Map<String, Command> commands = new HashMap<>();

    @Methods
    //Method used to perform the navigation invoking the command passed in input.
    public void performNavigation(String operation, Command toExecute) {
        commands.put(operation, toExecute);
        toExecute.execute();
    }

}
