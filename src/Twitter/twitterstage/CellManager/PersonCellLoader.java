package Twitter.twitterstage.CellManager;

import Twitter.DatabaseUtil.DataLoader;
import Twitter.GlobalSingleton;
import Twitter.twitterstage.model.Person;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;


//Class that implement the Strategy pattern to load a specific cell (in this case a person cell)
//to display into the search listview.
public class PersonCellLoader implements CellLoaderStrategy {

    @Override
    //Method that will load a specific person cell used into the search controller, whenever an user
    //prompt into the search bar a username to find.
    public Integer loadCell(ObservableList observablePersonList, ListView listView) {
        String searchedPerson = GlobalSingleton.getGlobalSingleton().getSearchQuery();
        DataLoader loader = DataLoader.getDataLoader();
        List<Person> personList = new ArrayList<>();

        //search for the person into the database passing in input the queried name
        personList.add(loader.returnPerson(searchedPerson));

        //if no person with that name was found, the list contain an empty person: return 0 to notify.
        if(personList.get(0).getName() == null) {
            return 0;
        }

        //else, an user was found: clear the observable list and the listview, and init them again
        observablePersonList.clear();
        observablePersonList.addAll(personList);
        listView.setItems(null);
        listView.setItems(observablePersonList);
        listView.setCellFactory(cell -> new UserListViewCellController());


        //return success
        return 1;
    }
}
