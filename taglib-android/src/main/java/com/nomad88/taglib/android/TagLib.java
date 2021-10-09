package com.nomad88.taglib.android;

import android.util.Log;

final class TagLib {

    private static boolean _isLibraryLoaded;

    static {
        try {
            System.loadLibrary("taglibwrapper");
            _isLibraryLoaded = true;
            if (TagLibAndroid.DEBUG) {
                Log.d("TagLib", "Library loaded");
            }
        } catch (Throwable e) {
            _isLibraryLoaded = false;
            if (TagLibAndroid.DEBUG) {
                Log.d("TagLib", "Failed to load library");
            }
        }
    }

    public static boolean isLibraryLoaded() {
        return _isLibraryLoaded;
    }

    // MP4::File
    public static native boolean mp4File_isSupported(String filePath);

    public static native long mp4File_create(String filePath, boolean readAudioProperties);

    public static native void mp4File_release(long ptr);

    public static native long mp4File_tag(long ptr);

    public static native long mp4File_audioProperties(long ptr);

    // Tag
    public static native String mp4Tag_title(long ptr);

    public static native String mp4Tag_artist(long ptr);

    public static native String mp4Tag_album(long ptr);

    public static native String mp4Tag_albumArtist(long ptr);

    public static native String mp4Tag_genre(long ptr);

    public static native int mp4Tag_year(long ptr);

    public static native int mp4Tag_track(long ptr);

    public static native int mp4Tag_disc(long ptr);

    public static native String mp4Tag_lyrics(long ptr);

    public static native void mp4Tag_setTitle(long ptr, String title);

    public static native void mp4Tag_setArtist(long ptr, String artist);

    public static native void mp4Tag_setAlbum(long ptr, String artist);

    public static native void mp4Tag_setAlbumArtist(long ptr, String albumArtist);

    public static native void mp4Tag_setGenre(long ptr, String genre);

    public static native void mp4Tag_setYear(long ptr, int year);

    public static native void mp4Tag_setTrack(long ptr, int track);

    public static native void mp4Tag_setDisc(long ptr, int disc);

    public static native void mp4Tag_setLyrics(long ptr, String lyrics);

    public static native boolean mp4Tag_save(long ptr);

    // AudioProperties
    public static native int audioProperties_bitrate(long ptr);

    public static native int audioProperties_sampleRate(long ptr);
}
