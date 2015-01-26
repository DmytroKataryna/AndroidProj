package kat.android.com.readerrss;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PcWorldRssParser {

    // We don't use namespaces
    private final String ns = null;

    public List<RssItem> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            //Use this call to change the general behaviour of the parser,
            // such as namespace processing or doctype declaration handling.
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            //Sets the input stream the parser is going to process.(InputStream inputStream, String inputEncoding)
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            inputStream.close();
        }
    }

    private List<RssItem> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "rss");
        String title = null;
        String link = null;
        String data = null;
        Bitmap img = null;

        List<RssItem> items = new ArrayList<RssItem>();
        boolean insideItem = false;
        //insideItem is used to get just item data , because there are RSS block which have  title , link but aren't rss item
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                insideItem = true;
            } else if (name.equals("title")) {
                if (insideItem)
                    title = readTitle(parser);
            } else if (name.equals("link")) {
                if (insideItem)
                    link = readLink(parser);
            } else if (name.equals("pubDate")) {
                if (insideItem)
                    data = readData(parser);
            } else if (name.equals("media:content")) {
                if (insideItem)
                    img = loadBitmap(parser);
                System.out.println(img);
            } else if (parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("item")) {
                insideItem = false;
            }
            if (title != null && link != null && data != null && img != null) {
                RssItem item = new RssItem(title, link, data, img);
                items.add(item);
                title = null;
                link = null;
                data = null;
                img = null;
            }
        }
        return items;
    }

    private String readLink(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    private String readTitle(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    private String readData(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return data;
    }

    private Bitmap loadBitmap(XmlPullParser parser) throws XmlPullParserException, IOException {
        String url = parser.getAttributeValue(0);
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }


    // For the tags title and link, extract their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
