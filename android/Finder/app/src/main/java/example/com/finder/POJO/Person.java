package example.com.finder.POJO;

/**
 * Created by Alexandru on 28-Mar-15.
 */
public class Person {
    private String name;
    private String id;
    private String pictureUrl;
    private int mutualFriends;
    private int mutualLikes;
    private int score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMutualFriends() {
        return mutualFriends;
    }

    public void setMutualFriends(int mutualFriends) {
        this.mutualFriends = mutualFriends;
    }

    public int getMutualLikes() {
        return mutualLikes;
    }

    public void setMutualLikes(int mutualLikes) {
        this.mutualLikes = mutualLikes;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
