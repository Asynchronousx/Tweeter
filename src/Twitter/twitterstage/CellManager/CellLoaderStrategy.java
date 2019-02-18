package Twitter.twitterstage.CellManager;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;


//Strategy pattern used to give to an algorithm family a different approach based on how the extender implemented the relative abstract function.
//The loadCell function load a cell based on which kind of loading we need (e.g: tweet or person).
public interface CellLoaderStrategy {
    Integer loadCell(ObservableList observableList, ListView listView);
}
