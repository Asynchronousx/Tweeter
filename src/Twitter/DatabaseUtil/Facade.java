package Twitter.DatabaseUtil;

//We'll use a FACADE approach to hide the methods to build a person
//once logged into the main view.
public interface Facade {
    void loadThisPerson();
}
