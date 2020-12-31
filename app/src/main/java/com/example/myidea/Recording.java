package com.example.myidea;

public class Recording {
    String Uri , fileName;
    boolean isPlaying = false;

    public Recording(String uri, String fileName, boolean isPlaying){
        Uri = uri;
        this.fileName = fileName;
        this.isPlaying=isPlaying;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUri() {
        return Uri;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
