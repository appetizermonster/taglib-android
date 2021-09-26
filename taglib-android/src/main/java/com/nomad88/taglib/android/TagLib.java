package com.nomad88.taglib.android;

final class TagLib {

    static {
        System.loadLibrary("taglibwrapper");
    }

    // MP4::File
    public static native long mp4File_create(String filePath);

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

    // AudioProperties
    public static native int audioProperties_bitrate(long ptr);
}
