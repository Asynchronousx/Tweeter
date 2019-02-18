package Twitter.twitterstage.CellManager;


import Twitter.DatabaseUtil.DataLoader;
import Twitter.GlobalSingleton;
import Twitter.twitterstage.model.Post;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

//Class that implement the Strategy pattern to load a specific cell (in this case a tweet cell)
//to display into the search listview.
public class TweetCellLoader implements CellLoaderStrategy {

    @Override
    //Method that will load the cell from the observable post list into the list view.
    public Integer loadCell(ObservableList observablePostList, ListView listView) {
        List<Post> resultPostList = new ArrayList<>();
        DataLoader loader = DataLoader.getDataLoader();
        String searchedHashtag = GlobalSingleton.getGlobalSingleton().getSearchQuery();
        String query;
        ResultSet resultSet;


        //First query:
        //We need to retrieve all thew TWEETID from the post that contains the searched hashtags:
        //Those tweetid will be useful later when we'll need to display all the post containing that specific hashtag.
        query = "SELECT TWEETID FROM HASHTAG WHERE CONTENT=\""+searchedHashtag+"\";";
        resultSet = loader.genericSearchQuery(query);

        //if result is null, something went wrong
        if(resultSet == null) {
            //return error
            return -1;

        }
        //otherwise, there are some result
        else {
            try {
                //Check if there is a next result: if not, return 0.
                if(!resultSet.next()) {
                    return 0;

                }

                //if here, we got some result, and we need to retrieve all the information about a given post
                do {
                    //query the database to retrieve the post with that tweetID
                    query = "SELECT * FROM POST WHERE TWEETID="+resultSet.getInt("TWEETID");

                    //add the result post to the list
                    resultPostList.addAll(loader.returnPostList(query));

                } while(resultSet.next());

                //sort the list by date if the result are major than 1
                if(resultPostList.size() > 1) {
                    resultPostList.sort((p1, p2) -> p2.getDateTweeted().compareTo(p1.getDateTweeted()));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //Update the observable post list with all the post found with that hashtag.
        observablePostList.clear();
        observablePostList.addAll(resultPostList);
        listView.setItems(null);
        listView.setItems(observablePostList);
        listView.setCellFactory(cell -> new TweetListViewCellController());

        //return success
        return 1;

    }
}
