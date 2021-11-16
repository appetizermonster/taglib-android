#include "taglib_wrapper.h"

#include <ogg/xiphcomment.h>
#include <flac/flacpicture.h>
#include <tag.h>
#include <tpropertymap.h>
#include <tlist.h>

using namespace std;
using namespace TagLib;

String formatToMimeType(int format) {
    switch (format) {
        case 13:
            return "image/jpeg";
        case 14:
            return "image/png";
        default:
            return "";
    }
}

int mimeTypeToFormat(const String &format) {
    if (format == "image/jpeg" || format == "image/jpg") {
        return 13;
    } else if (format == "image/png") {
        return 14;
    }
    return 0;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_title(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto &ret = instance->title();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_artist(JNIEnv *env, jobject,
                                                                   jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto &ret = instance->artist();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_album(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto &ret = instance->album();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_albumArtist(JNIEnv *env, jobject,
                                                                        jlong ptr) {
    auto instance = reinterpret_cast<Ogg::XiphComment *>(ptr);
    if (instance->contains("ALBUMARTIST")) {
        const auto &ret = instance->fieldListMap()["ALBUMARTIST"].toString();
        return env->NewStringUTF(ret.to8Bit(true).c_str());
    }
    return env->NewStringUTF(EMPTY_STR.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_genre(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto &ret = instance->genre();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_year(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    auto ret = instance->year();
    return static_cast<jint>(ret);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_track(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    auto ret = instance->track();
    return static_cast<jint>(ret);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_disc(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<Ogg::XiphComment *>(ptr);
    if (instance->contains("DISCNUMBER"))
        return instance->fieldListMap()["DISCNUMBER"].front().toInt();
    return 0;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_lyrics(JNIEnv *env, jobject,
                                                                   jlong ptr) {
    auto instance = reinterpret_cast<Ogg::XiphComment *>(ptr);
    if (instance->contains("LYRICS")) {
        const auto &ret = instance->fieldListMap()["LYRICS"].toString();
        return env->NewStringUTF(ret.to8Bit(true).c_str());
    }
    return env->NewStringUTF(EMPTY_STR.c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_coverArtFormat(JNIEnv *env, jobject,
                                                                           jlong ptr) {
    auto instance = reinterpret_cast<Ogg::XiphComment *>(ptr);
    const auto &pictureList = instance->pictureList();
    if (!pictureList.isEmpty()) {
        const auto &picture = pictureList.front();
        const auto &mimeType = picture->mimeType();
        int format = mimeTypeToFormat(mimeType);
        return static_cast<jint>(format);
    }
    return -1;
}

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_coverArtData(JNIEnv *env, jobject,
                                                                         jlong ptr) {
    auto instance = reinterpret_cast<Ogg::XiphComment *>(ptr);
    const auto &pictureList = instance->pictureList();
    if (!pictureList.isEmpty()) {
        const auto &picture = pictureList.front();
        const auto &data = picture->data();
        auto ret = env->NewByteArray(data.size());
        env->SetByteArrayRegion(ret, 0, data.size(),
                                reinterpret_cast<const jbyte *>(data.data()));
        return ret;
    }
    return nullptr;
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_setTitle(JNIEnv *env, jobject,
                                                                     jlong ptr,
                                                                     jstring title) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto titleChars = env->GetStringUTFChars(title, nullptr);
    instance->setTitle(String(titleChars, String::UTF8));
    env->ReleaseStringUTFChars(title, titleChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_setArtist(JNIEnv *env, jobject,
                                                                      jlong ptr,
                                                                      jstring artist) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto artistChars = env->GetStringUTFChars(artist, nullptr);
    instance->setArtist(String(artistChars, String::UTF8));
    env->ReleaseStringUTFChars(artist, artistChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_setAlbum(JNIEnv *env, jobject,
                                                                     jlong ptr,
                                                                     jstring album) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto albumChars = env->GetStringUTFChars(album, nullptr);
    instance->setAlbum(String(albumChars, String::UTF8));
    env->ReleaseStringUTFChars(album, albumChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_setAlbumArtist(JNIEnv *env, jobject,
                                                                           jlong ptr,
                                                                           jstring albumArtist) {
    auto instance = reinterpret_cast<Ogg::XiphComment *>(ptr);
    const auto albumArtistChars = env->GetStringUTFChars(albumArtist, nullptr);
    const String albumArtistStr(albumArtistChars, String::UTF8);
    if (albumArtistStr.isEmpty()) {
        instance->removeFields("ALBUMARTIST");
    } else {
        instance->addField("ALBUMARTIST", albumArtistStr, true);
    }
    env->ReleaseStringUTFChars(albumArtist, albumArtistChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_setGenre(JNIEnv *env, jobject,
                                                                     jlong ptr,
                                                                     jstring genre) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto genreChars = env->GetStringUTFChars(genre, nullptr);
    instance->setGenre(String(genreChars, String::UTF8));
    env->ReleaseStringUTFChars(genre, genreChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_setYear(JNIEnv *env, jobject, jlong ptr,
                                                                    jint year) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    instance->setYear(static_cast<unsigned int>(year));
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_setTrack(JNIEnv *env, jobject,
                                                                     jlong ptr,
                                                                     jint track) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    instance->setTrack(static_cast<unsigned int>(track));
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_setDisc(JNIEnv *env, jobject, jlong ptr,
                                                                    jint disc) {
    auto instance = reinterpret_cast<Ogg::XiphComment *>(ptr);
    if (disc < 0) {
        instance->removeFields("DISCNUMBER");
    } else {
        instance->addField("DISCNUMBER", String::number(disc));
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_setLyrics(JNIEnv *env, jobject,
                                                                      jlong ptr,
                                                                      jstring lyrics) {
    auto instance = reinterpret_cast<Ogg::XiphComment *>(ptr);
    const auto lyricsChars = env->GetStringUTFChars(lyrics, nullptr);
    const String lyricsStr(lyricsChars, String::UTF8);
    if (lyricsStr.isEmpty()) {
        instance->removeFields("LYRICS");
    } else {
        instance->addField("LYRICS", lyricsStr, true);
    }
    env->ReleaseStringUTFChars(lyrics, lyricsChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_setCoverArt(JNIEnv *env, jobject,
                                                                        jlong ptr,
                                                                        jint format,
                                                                        jbyteArray data) {
    auto instance = reinterpret_cast<Ogg::XiphComment *>(ptr);
    const auto dataBytes = env->GetByteArrayElements(data, nullptr);
    const auto dataSize = env->GetArrayLength(data);
    const ByteVector byteVector(reinterpret_cast<const char *>(dataBytes),
                                static_cast<unsigned int>(dataSize));
    auto *picture = new FLAC::Picture;
    const auto mimeType(formatToMimeType(format));
    picture->setType(FLAC::Picture::Type::FrontCover);
    picture->setMimeType(mimeType);
    picture->setData(byteVector);
    instance->removeAllPictures();
    instance->addPicture(picture);
    env->ReleaseByteArrayElements(data, dataBytes, JNI_ABORT);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisTagNative_deleteCoverArt(JNIEnv *env, jobject,
                                                                           jlong ptr) {
    auto instance = reinterpret_cast<Ogg::XiphComment *>(ptr);
    instance->removeAllPictures();
}
