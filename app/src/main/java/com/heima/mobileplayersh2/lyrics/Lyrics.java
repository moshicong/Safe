package com.heima.mobileplayersh2.lyrics;

/**
 * Created by Administrator on 2015/11/17.
 */
public class Lyrics implements Comparable<Lyrics>{

    private int startPoint;
    private String content;

    public Lyrics(int startPoint, String content) {
        this.startPoint = startPoint;
        this.content = content;
    }

    public int getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(Lyrics another) {
        return startPoint - another.startPoint;
    }
}
