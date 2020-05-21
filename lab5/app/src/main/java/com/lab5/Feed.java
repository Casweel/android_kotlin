package com.lab5;

public class Feed {
    private String media;
    private int like;

    Feed(String media, int like)
    {
        this.media = media;
        this.like = like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public int getLike() {
        return like;
    }

    public String getMedia() {
        return media;
    }
}
