package org.levraievangile.Model;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class DownloadFile {
    private String data;
    private String title;
    private String album;
    private String artist;
    private String duration;

    public DownloadFile(String data, String title, String album, String artist, String duration) {
        this.data = data;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "{\"data\":\""+data+"\"," +
                "\"title\":\""+title+"\"," +
                "\"album\":\""+album+"\"," +
                "\"artist\":\""+artist+"\"," +
                "\"duration\":"+duration+"}";
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
