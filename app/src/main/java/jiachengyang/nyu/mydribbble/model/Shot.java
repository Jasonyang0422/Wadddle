package jiachengyang.nyu.mydribbble.model;

import java.util.Map;

public class Shot {

    public String id;
    public String title;
    public String description;
    public String html_url;

    public int width;
    public int height;
    public Map<String, String> images;
    public boolean animated;

    public int views_count;
    public int likes_count;
    public int buckets_count;

    public User user;

}
