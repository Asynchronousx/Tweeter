package Twitter;

import Twitter.Util.AccessModifiers;
import Twitter.Util.Singleton;
import Twitter.twitterstage.model.Person;

@Singleton
//Singleton class that will hold values useful to a scene passage: it will remember things such as search query,
//account holder, and the person instance of the user client
public class GlobalSingleton {
    public static GlobalSingleton globalSingleton;
    private String accountName;
    private String searchQuery;
    private Person thisPerson;
    private String tmpPersonSearched;

    @AccessModifiers(value="Get singleton")
    public static GlobalSingleton getGlobalSingleton() {
        if(globalSingleton == null) {
            globalSingleton = new GlobalSingleton();
        }

        return globalSingleton;
    }

    @AccessModifiers
    public String getAccountName() {
        return accountName;
    }
    public Person getThisPerson() {
        return thisPerson;
    }
    public String getSearchQuery() { return searchQuery; }
    public String getTmpPersonSearched() { return tmpPersonSearched; }

    public void setThisPerson(Person thisPerson) {
        this.thisPerson = thisPerson;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }
    public void setTmpPersonSearched(String tmpPersonSearched) { this.tmpPersonSearched = tmpPersonSearched; }
}
