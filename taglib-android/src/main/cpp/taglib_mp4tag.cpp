#include "taglib_wrapper.h"

#include <mp4/mp4tag.h>
#include <mp4/mp4item.h>
#include <tag.h>
#include <tpropertymap.h>

using namespace std;
using namespace TagLib;

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_title(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto &ret = instance->title();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_artist(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto &ret = instance->artist();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_album(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto &ret = instance->album();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_albumArtist(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    if (instance->contains("aART")) {
        const auto &ret = instance->item("aART").toStringList().toString(", ");
        return env->NewStringUTF(ret.to8Bit(true).c_str());
    }
    return env->NewStringUTF(EMPTY_STR.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_genre(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto &ret = instance->genre();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_year(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    auto ret = instance->year();
    return static_cast<jint>(ret);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_track(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    auto ret = instance->track();
    return static_cast<jint>(ret);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_disc(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    if (instance->contains("disk"))
        return instance->item("disk").toIntPair().first;
    return 0;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_lyrics(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    if (instance->contains("\251lyr")) {
        const auto &ret = instance->item("\251lyr").toStringList().toString(", ");
        return env->NewStringUTF(ret.to8Bit(true).c_str());
    }
    return env->NewStringUTF(EMPTY_STR.c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_coverArtFormat(JNIEnv *env, jobject,
                                                                     jlong ptr) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    if (instance->contains("covr")) {
        const auto &coverArtList = instance->item("covr").toCoverArtList();
        if (!coverArtList.isEmpty()) {
            const auto &coverArt = coverArtList.front();
            return static_cast<jint>(coverArt.format());
        }
    }
    return -1;
}

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_coverArtData(JNIEnv *env, jobject,
                                                                   jlong ptr) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    if (instance->contains("covr")) {
        const auto &coverArtList = instance->item("covr").toCoverArtList();
        if (!coverArtList.isEmpty()) {
            const auto &coverArt = coverArtList.front();
            const auto &data = coverArt.data();
            auto ret = env->NewByteArray(data.size());
            env->SetByteArrayRegion(ret, 0, data.size(),
                                    reinterpret_cast<const jbyte *>(data.data()));
            return ret;
        }
    }
    return nullptr;
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_setTitle(JNIEnv *env, jobject, jlong ptr,
                                                               jstring title) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto titleChars = env->GetStringUTFChars(title, nullptr);
    instance->setTitle(String(titleChars, String::UTF8));
    env->ReleaseStringUTFChars(title, titleChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_setArtist(JNIEnv *env, jobject, jlong ptr,
                                                                jstring artist) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto artistChars = env->GetStringUTFChars(artist, nullptr);
    instance->setArtist(String(artistChars, String::UTF8));
    env->ReleaseStringUTFChars(artist, artistChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_setAlbum(JNIEnv *env, jobject, jlong ptr,
                                                               jstring album) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto albumChars = env->GetStringUTFChars(album, nullptr);
    instance->setAlbum(String(albumChars, String::UTF8));
    env->ReleaseStringUTFChars(album, albumChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_setAlbumArtist(JNIEnv *env, jobject,
                                                                     jlong ptr,
                                                                     jstring albumArtist) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    const auto albumArtistChars = env->GetStringUTFChars(albumArtist, nullptr);
    const String albumArtistStr(albumArtistChars, String::UTF8);
    if (albumArtistStr.isEmpty()) {
        instance->removeItem("aART");
    } else {
        instance->setItem("aART", StringList(albumArtistStr));
    }
    env->ReleaseStringUTFChars(albumArtist, albumArtistChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_setGenre(JNIEnv *env, jobject, jlong ptr,
                                                               jstring genre) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto genreChars = env->GetStringUTFChars(genre, nullptr);
    instance->setGenre(String(genreChars, String::UTF8));
    env->ReleaseStringUTFChars(genre, genreChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_setYear(JNIEnv *env, jobject, jlong ptr,
                                                              jint year) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    instance->setYear(static_cast<unsigned int>(year));
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_setTrack(JNIEnv *env, jobject, jlong ptr,
                                                               jint track) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    instance->setTrack(static_cast<unsigned int>(track));
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_setDisc(JNIEnv *env, jobject, jlong ptr,
                                                              jint disc) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    if (disc < 0) {
        instance->removeItem("disk");
    } else {
        instance->setItem("disk", MP4::Item(disc, 0));
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_setLyrics(JNIEnv *env, jobject, jlong ptr,
                                                                jstring lyrics) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    const auto lyricsChars = env->GetStringUTFChars(lyrics, nullptr);
    const String lyricsStr(lyricsChars, String::UTF8);
    if (lyricsStr.isEmpty()) {
        instance->removeItem("\251lyr");
    } else {
        instance->setItem("\251lyr", StringList(lyricsStr));
    }
    env->ReleaseStringUTFChars(lyrics, lyricsChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_setCoverArt(JNIEnv *env, jobject, jlong ptr,
                                                                  jint format, jbyteArray data) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    const auto dataBytes = env->GetByteArrayElements(data, nullptr);
    const auto dataSize = env->GetArrayLength(data);
    const ByteVector byteVector(reinterpret_cast<const char *>(dataBytes),
                                static_cast<unsigned int>(dataSize));
    const MP4::CoverArt coverArt(static_cast<MP4::CoverArt::Format>(format), byteVector);
    MP4::CoverArtList coverArtList;
    coverArtList.append(coverArt);
    instance->setItem("covr", coverArtList);
    env->ReleaseByteArrayElements(data, dataBytes, JNI_ABORT);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_MP4TagNative_deleteCoverArt(JNIEnv *env, jobject,
                                                                     jlong ptr) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    instance->removeItem("covr");
}
