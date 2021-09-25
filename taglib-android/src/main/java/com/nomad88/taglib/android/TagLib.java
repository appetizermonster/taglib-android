package com.nomad88.taglib.android;

final class TagLib {

    static {
        System.loadLibrary("taglibwrapper");
    }

    // FileRef
    public static native long fileRef_create(String filePath);

    public static native void fileRef_release(long ptr);

    public static native boolean fileRef_isNull(long ptr);

    public static native long fileRef_tag(long ptr);

    // Tag
    public static native String tag_title(long ptr);
}
