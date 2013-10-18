package github.chenxh.media.flv.script;

public class MovieClip {
    private String url;

    public MovieClip(String url) {
        super();
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return String.valueOf(url);
    }
}
