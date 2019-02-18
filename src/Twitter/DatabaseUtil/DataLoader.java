package Twitter.DatabaseUtil;

import Twitter.GlobalSingleton;
import Twitter.Util.Methods;
import Twitter.Util.Singleton;
import Twitter.twitterstage.model.Person;
import Twitter.twitterstage.model.Post;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

//Class data loader holds the function information we need to build this person and do query on database.
//It is also a singleton to avoid memory leaks.
@Singleton
public class DataLoader {
    public static DataLoader dataLoader;
    Connection conn;

    //Get the connection from the controller
    public DataLoader() {
        this.conn = DBConnection.getConnection();
    }

    //Enum useful to know which category we're analyzing.
    public enum FollowType {
        FOLLOWER,
        FOLLOWING
    }

    @Methods(value="Private")
    //Method that check if a given link is an image.
    private Boolean isImage(String url) {

        //Initially, the image is null
        java.awt.Image image = null;

        //if the url is present
        if(url != null) {
            try {
                //get the url
                URL imgUrl = new URL(url);
                //open the byte stream to check if is an image
                image = ImageIO.read(imgUrl.openStream());

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        //if the image is not null, it means that an image could be loaded from the url.
        //return true or false, depending on the state of the image
        if(image != null) {
            return true;

        } else {
            return false;

        }
    }

    //Method that will return all the hashtag of a given post
    private List<String> returnHashTags(int tweetID) {
        List<String> hashTags = new ArrayList<>();
        ResultSet resultSet;
        String query;

        //Select every fields for the given tweet post ID and get the result
        query = "SELECT * FROM HASHTAG WHERE TWEETID="+tweetID+";";
        resultSet = this.genericSearchQuery(query);

        try {
            //While there is something to analyze into the result set
            while(resultSet.next()) {
                //add the content of that hashtag to the hashtags list
                hashTags.add(resultSet.getString("CONTENT"));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        //return the hashtag lists
        return hashTags;

    }

    @Methods(value="Protected")
    //Method that will return the list of the followers or following based on what's passed into the query.
    protected List<Person> returnFollow(String personName, FollowType followType) {
        List<Person> followList = new ArrayList<>();
        ResultSet resultSet;
        String query;

        //Choose the query type based on the follow type: follower or following, then get the result
        query = followType.equals(FollowType.FOLLOWER)?
                "SELECT * FROM FOLLOWER WHERE USERNAME=\""+personName+"\";":
                "SELECT * FROM FOLLOWING WHERE USERNAME=\""+personName+"\";";

        resultSet = this.genericSearchQuery(query);

        try {
            //Followers can be a lot. resultSet could store > 1 value, so we need to call next on it in a while.
            while(resultSet.next()) {
                //add the person into the list: we'll search that specific person with the function returnPerson defined
                //into this class. we'll pass the username of that follower/following for returning all the info needed.
                followList.add(returnPerson(resultSet.getString(resultSet.getMetaData().getColumnLabel(1))));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //return the list
        return followList;

    }

    //Method that will return all the following person posts
    protected List<Post> returnFollowingPostList(List<Person> following) {
        List<Post> postsList = new ArrayList<>();
        String query = "SELECT * FROM POST WHERE USERNAME =";

        //for every following person, get his posts
        for(Person p: following) {
            postsList.addAll(returnPostList(query+"\""+p.getName()+"\";"));

        }

        //merge the post retrieved (if presents) with the personal user post
        postsList.addAll(GlobalSingleton.getGlobalSingleton().getThisPerson().getPostList());

        //sort the list by date
        postsList.sort((p1, p2) -> p2.getDateTweeted().compareTo(p1.getDateTweeted()));

        //return the list
        return postsList;

    }


    @Methods(value="Public")
    //Get the dataloader instance with the lazy init
    public static DataLoader getDataLoader() {
        if(dataLoader == null) {
            dataLoader = new DataLoader();

        }

        return dataLoader;

    }



    //Method that return true if the image was successful updated to the server, false otherwise.
    public Boolean loadImage(String url) {

        //The url passed is an image? (calling the isImage method)
        if(isImage(url)) {
            //If yes, update the database and return true
            this.genericUpdateQuery("UPDATE PERSON SET PROFILEIMAGE=\"" + url + "\" " +
                    "WHERE USERNAME=\"" + GlobalSingleton.getGlobalSingleton().getAccountName() + "\";");
            return true;

        } else {
            //else, notify of a failed update.
            return false;

        }
    }


    //Method that, given a name, will return all the information about that user:
    //In our case, name and profile image.
    public Person returnPerson(String personName) {
        ResultSet resultSet;
        Person person = new Person();
        String query;

        //Query to select all the fields from the selected person row
        query = "SELECT * FROM PERSON WHERE USERNAME=\""+personName+"\";";
        resultSet = this.genericSearchQuery(query);

        try {
            //if nothing found
            if(!resultSet.next()) {
                //return an empty person
                return person;

            }
            //else, fetch the data from the person row and initialize the person to return
            else {
                //Init username
                String username = resultSet.getString("USERNAME");
                username = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
                person.setName(username);

                //Init date joined
                person.setJoinedDate(resultSet.getString("DATEJOINED"));

                //Init the image url found into the database
                String imageUrl = resultSet.getString("PROFILEIMAGE");
                if(isImage(imageUrl)) {
                    //If is image, set the image as person profile image
                    person.setImage(new Image(imageUrl, 90, 90, false, true));

                } else {
                    //set the default image
                    person.setImage(new Image("Twitter/resources/IMG/Placeholder.png"));

                }
            }
        } catch(Exception e) {
            e.printStackTrace();

        }

        //return the person, empty or not
        return person;
    }


    //Method that will load the person personal posts and the post of his following persons.
    public List<Post> returnPostList(String query) {
        List<Post> postsList = new ArrayList<>();
        ResultSet resultSet;

        //get the info from the query
        resultSet = this.genericSearchQuery(query);

        //Load the personal user post list
        try {
            //While there is something to fetch into the result set
            while(resultSet.next()) {
                //load the post data
                Post post = new Post();
                post.setTweetID(resultSet.getInt("TWEETID"));
                post.setWhoTweeted(resultSet.getString("USERNAME"));
                post.setTweetText(resultSet.getString("TWEET"));
                post.setHashTags(returnHashTags(resultSet.getInt("TWEETID")));
                post.setDateTweeted(resultSet.getString("TWEETDATE"));

                //add the post to the post list
                postsList.add(post);

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        //sort the post retrieved by date if size > 1
        if(postsList.size() > 1) {
            postsList.sort((p1, p2) -> p2.getDateTweeted().compareTo(p1.getDateTweeted()));

        }

        //return the list
        return postsList;

    }

    //Method that will help an user to insert his new post
    public Integer insertPost(String whoTweeted, String tweetText, List<String> hashTags, String date) {
        String query;
        Boolean result;
        Integer tweetID = -1;


        //query the database inserting the new post
        query = "INSERT INTO POST VALUES(0,"+"\""+tweetText+"\","+"\""+date+"\","+"\""+whoTweeted+"\");";
        result = this.genericUpdateQuery(query);

        //if update was successful
        if(result) {
            //retrieve the new tweetID
            query = "SELECT TWEETID FROM POST ORDER BY TWEETID DESC LIMIT 1";
            ResultSet resultSet = this.genericSearchQuery(query);

            try {
                //Try to fetch the next result set and fetch the tweet id
                resultSet.next();
                tweetID = resultSet.getInt("TWEETID");

            } catch (Exception e) {
                e.printStackTrace();

            }

            //insert the hashtags into the new created post
            for(String s: hashTags) {
                //Query the database to insert the post
                query = "INSERT INTO HASHTAG VALUES(0,"+tweetID+",\""+s+"\");";
                this.genericUpdateQuery(query);

            }

        }

        //return the new tweetid
        return tweetID;

    }

    //Method that will help the user to perform a generic search query
    public ResultSet genericSearchQuery(String query) {
        ResultSet resultSet = DBController.searchQuery(conn, query);
        return resultSet;
    }

    //Method that will help the user to perform a generic update query
    public Boolean genericUpdateQuery(String query) {
        Boolean result = DBController.updateQuery(conn, query);
        return result;
    }

    //Method to check if a given user follows another user
    public Boolean checkIfFollows(String whoIsFollowing, String whoIsFollowed) {
        String query = "SELECT * FROM FOLLOWING WHERE FOLLOWINGUSERNAME=\"" + whoIsFollowed + "\" AND USERNAME =\"" + whoIsFollowing + "\";";
        ResultSet resultSet = this.genericSearchQuery(query);
        Boolean result;

        try {
            //if no result, the user does not follow the current passed user.
            if (!resultSet.next()) {
                return false;

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        //if here, the user is followed.
        return true;

    }

    //Action that once performed, will refresh all the content in the current scene.
    public void refresh() {
        //We'll use the dataLoader to refresh the page.
        Person thisPerson = GlobalSingleton.getGlobalSingleton().getThisPerson();
        thisPerson.setFollowers(this.returnFollow(thisPerson.getName(), FollowType.FOLLOWER));
        thisPerson.setFollowings(this.returnFollow(thisPerson.getName(), FollowType.FOLLOWING));
        thisPerson.setGlobalPostList(this.returnFollowingPostList(thisPerson.getFollowings()));
    }

    //Method that will return the url of a profile image of an user if present, null otherwise.

    //Method that will return all the user found into the database.
    public List<Person> retrieveAllUsers() {
        List<Person> personList = new ArrayList<>();
        ResultSet resultSet;
        String query;

        //Select all the rows from person
        query = "SELECT * FROM PERSON;";
        resultSet = this.genericSearchQuery(query);

        try {
            //While there is something to analyze, fetch the values
            while(resultSet.next()) {
                //Add to that list the current analyzed person from their username
                personList.add(this.returnPerson(resultSet.getString("USERNAME")));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        //return the populated or empty list
        return personList;

    }

}
