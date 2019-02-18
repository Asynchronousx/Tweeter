package Twitter.DatabaseUtil;


import Twitter.GlobalSingleton;
import Twitter.twitterstage.model.Person;


//Person loader is our facade builder.
public class PersonLoader implements Facade {
    @Override
    public void loadThisPerson() {
        //Init the variables
        String accountName = GlobalSingleton.getGlobalSingleton().getAccountName();
        DataLoader loader = DataLoader.getDataLoader();
        String query = "SELECT * FROM POST WHERE USERNAME =\""+accountName+"\";";
        Person thisPerson;

        //set the current person querying the database with the person's name who logged into the client
        GlobalSingleton.getGlobalSingleton().setThisPerson(loader.returnPerson(accountName));

        //set thisPerson as the just retrieved person with the query from the database
        thisPerson = GlobalSingleton.getGlobalSingleton().getThisPerson();

        //Set person follower
        thisPerson.setFollowers(loader.returnFollow(accountName, DataLoader.FollowType.FOLLOWER));

        //Set person followings
        thisPerson.setFollowings(loader.returnFollow(accountName, DataLoader.FollowType.FOLLOWING));

        //Set person personal post list
        thisPerson.setPostList(loader.returnPostList(query));

        //Set person followings post list
        thisPerson.setGlobalPostList(loader.returnFollowingPostList(thisPerson.getFollowings()));

    }
}

