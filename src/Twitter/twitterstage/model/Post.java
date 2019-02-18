package Twitter.twitterstage.model;

import Twitter.Util.AccessModifiers;
import Twitter.Util.Attributes;
import Twitter.Util.Constructor;

import java.util.Date;
import java.util.List;

//Data model class of our application: It represent the post itself with all the info that it contains, such as tweetText, the tweeID to identify him,
//who tweeted that tweet, hashtags and date tweeted.
public class Post {
    @Attributes
    private Integer tweetID;
    private String whoTweeted;
    private String tweetText;
    private List<String> hashTags;
    private String dateTweeted;

    @Constructor
    public Post() {}

    public Post(Integer tweetID, String whoTweeted, String tweetText, List<String> hashTags, String dateTweeted) {
        this.tweetID = tweetID;
        this.whoTweeted = whoTweeted;
        this.tweetText = tweetText;
        this.hashTags = hashTags;
        this.dateTweeted = dateTweeted;
    }

    @AccessModifiers
    public Integer getTweetID() { return this.tweetID; }
    public String getWhoTweeted() {
        return whoTweeted;
    }
    public String getTweetText() {
        return tweetText;
    }
    public List<String> getHashTags() {
        return hashTags;
    }
    public String getDateTweeted() {
        return dateTweeted;
    }

    public void setTweetID(Integer tweetID) { this.tweetID = tweetID; }
    public void setWhoTweeted(String whoTweeted) { this.whoTweeted = whoTweeted; }
    public void setTweetText(String tweetText) { this.tweetText = tweetText; }
    public void setHashTags(List<String> hashTags) { this.hashTags = hashTags; }
    public void setDateTweeted(String dateTweeted) { this.dateTweeted = dateTweeted; }

    @Override
    public String toString() {
        return this.tweetID.toString();
    }
}
