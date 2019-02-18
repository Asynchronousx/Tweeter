package Twitter.adminstage;

import Twitter.DatabaseUtil.DataLoader;
import Twitter.GlobalSingleton;
import Twitter.twitterstage.model.Person;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class AdminCellLoader {

    public enum Show {
        ALLUSER,
        SINGLEUSER
    }

    public Integer loadCell(ObservableList observablePersonList, ListView listView, Show type) {
        //the admin need to retrieve all the user to be displayed into his view.
        List<Person> personList = new ArrayList<>();
        DataLoader loader = DataLoader.getDataLoader();

        //If the type is show all user, retrieve all the user from the database
        if(type.equals(Show.ALLUSER)) {
            personList = loader.retrieveAllUsers();

        }
        //else, retrieve just the single user prompted into the query
        else if(type.equals(Show.SINGLEUSER)){
            personList.add(loader.returnPerson(GlobalSingleton.getGlobalSingleton().getSearchQuery()));

        }

        //if is empty, don't bother init the listview
        if(personList.isEmpty() || personList.get(0).getName() == null) {
            return 0;

        } else {
            //else, sort the person for their name
            personList.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
        }

        //clear the lists and reset the new object
        observablePersonList.clear();
        observablePersonList.addAll(personList);
        listView.setItems(null);
        listView.setItems(observablePersonList);
        listView.setCellFactory(cell -> new AdminListViewCellController());

        //success
        return 1;

    }

}
