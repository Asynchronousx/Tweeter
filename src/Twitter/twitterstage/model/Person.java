package Twitter.twitterstage.model;

import Twitter.Util.AccessModifiers;
import Twitter.Util.Attributes;
import Twitter.Util.Methods;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

//Data model of our application: It represents the generic Person that use the tweeter client:
//It got a name, a profile image, a joined date and following/follower lists aswell as personal and global post lists.
public class Person {
    @Attributes
    private String name;
    private Image image;
    private String joinedDate;
    private List<Post> postList = new ArrayList<>();
    private List<Post> globalPostList = new ArrayList<>();
    private List<Person> followers = new ArrayList<>();
    private List<Person> followings = new ArrayList<>();

    @AccessModifiers
    public String getName() { return name; }
    public Image getImage() { return image; }
    public String getJoinedDate() { return joinedDate; }
    public List<Post> getPostList() { return postList; }
    public List<Post> getGlobalPostList() { return globalPostList; }
    public List<Person> getFollowers() { return followers; }
    public List<Person> getFollowings() { return followings; }

    public void setFollowers(List<Person> followers) { this.followers = followers; }
    public void setFollowings(List<Person> followings) { this.followings = followings; }
    public void setJoinedDate(String joinedDate) { this.joinedDate = joinedDate; }
    public void setPostList(List<Post> postList) { this.postList = postList; }
    public void setGlobalPostList(List<Post> postListFollowing) { this.globalPostList = postListFollowing; }
    public void setName(String name) {
        this.name = name;
    }
    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User: " + getName();
    }

    @Methods
    //Method that remove a post from bot global and personal post list
    public void removePost(Integer tweetID) {
        int curIndex = 0;

        //KNOWN PROBLEM: when the post list is big, the complexity O(N) could be slow.
        //A better approach would be use HashMap to store the post, and retrieve the indexed key.


        //scan the personal post list to find the index of the post to be deleted
        for(Post p: postList) {
            if(p.getTweetID().equals(tweetID)) {
                postList.remove(curIndex);
                curIndex = 0;
                break;
            }

            curIndex++;

        }

        //scan the global postList to find the index of the post to be deleted
        for(Post p: globalPostList) {
            if(p.getTweetID().equals(tweetID)) {
                globalPostList.remove(curIndex);
                curIndex = 0;
                break;
            }

            curIndex++;

        }

    }

}

