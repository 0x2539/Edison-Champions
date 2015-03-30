package example.com.finder.POJO;

import android.graphics.Bitmap;

/**
 * Created by Alexandru on 29-Mar-15.
 */
public class Event {
    private String eventName;
    private String eventUrl;
    private Bitmap eventPicture;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public Bitmap getEventPicture() {
        return eventPicture;
    }

    public void setEventPicture(Bitmap eventPicture) {
        this.eventPicture = eventPicture;
    }
}
