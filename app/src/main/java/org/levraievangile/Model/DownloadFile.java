package org.levraievangile.Model;

import android.graphics.Bitmap;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class DownloadFile {
    private String data;
    private String title;
    private String album;
    private String artist;
    private String duration;
    private int mipmap;
    private String date;
    private String shortcode;
    private Bitmap bitmap;
    private long albumId;

    public DownloadFile(String data, String title, String album, String artist, String duration, int mipmap, String date, String shortcode, long albumId, Bitmap bitmap) {
        this.data = data;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.mipmap = mipmap;
        this.date = date;
        this.shortcode = shortcode;
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "{\"data\":\""+data+"\"," +
                "\"title\":\""+title+"\"," +
                "\"album\":\""+album+"\"," +
                "\"artist\":\""+artist+"\"," +
                "\"duration\":\""+duration+"\"," +
                "\"mipmap\":\""+mipmap+"\"," +
                "\"shortcode\":\""+shortcode+"\"," +
                "\"albumId\":\""+albumId+"\"," +
                "\"bitmap\":\""+bitmap+"\"," +
                "\"date\":"+date+"}";
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

    public int getMipmap() {
        return mipmap;
    }

    public void setMipmap(int mipmap) {
        this.mipmap = mipmap;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShortcode() {
        return shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
