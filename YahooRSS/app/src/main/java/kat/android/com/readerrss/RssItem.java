package kat.android.com.readerrss;

import android.graphics.Bitmap;

import java.io.Serializable;

public class RssItem implements Serializable {
    private final String title;
    private final String link;
    private String date;
    private Bitmap attachmentUrl; //IMG

    public RssItem(String title, String link, String date, Bitmap attachmentUrl) {
        this.title = title;
        this.link = link;
        this.date = date;
        this.attachmentUrl = attachmentUrl;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public Bitmap getAttachmentUrl() {
        return attachmentUrl;
    }
}
