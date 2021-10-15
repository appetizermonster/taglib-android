#include <jni.h>
#include <string>
#include <android/log.h>

#include <mp4/mp4file.h>
#include <mp4/mp4tag.h>
#include <mp4/mp4item.h>
#include <tfilestream.h>
#include <tag.h>
#include <audioproperties.h>
#include <tpropertymap.h>

using namespace std;
using namespace TagLib;

#define LOG_TAG "TagLibWrapper"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

static const string EMPTY_STR;

// MP4File
extern "C" JNIEXPORT jboolean JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4File_1isSupported(JNIEnv *env, jclass, jstring filePath) {
    auto filePathChars = env->GetStringUTFChars(filePath, nullptr);
    auto fileStream = new FileStream(filePathChars, true);
    auto isMP4File = MP4::File::isSupported(fileStream);
    delete fileStream;
    env->ReleaseStringUTFChars(filePath, filePathChars);
    return static_cast<jboolean>(isMP4File);
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4File_1create(JNIEnv *env, jclass, jstring filePath,
                                                       jboolean readAudioProperties) {
    auto filePathChars = env->GetStringUTFChars(filePath, nullptr);
    auto *instance = new MP4::File(filePathChars, readAudioProperties);
    env->ReleaseStringUTFChars(filePath, filePathChars);
    if (!instance->isValid()) {
        delete instance;
        instance = nullptr;
    }
    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4File_1release(JNIEnv *env, jclass, jlong ptr) {
    auto *instance = reinterpret_cast<MP4::File *>(ptr);
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4File_1tag(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<MP4::File *>(ptr);
    return reinterpret_cast<jlong>(instance->tag());
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4File_1audioProperties(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<MP4::File *>(ptr);
    return reinterpret_cast<jlong>(instance->audioProperties());
}

// MP4Tag
extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1title(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto &ret = instance->title();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1artist(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto &ret = instance->artist();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1album(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto &ret = instance->album();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1albumArtist(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    if (instance->contains("aART")) {
        const auto &ret = instance->item("aART").toStringList().toString(", ");
        return env->NewStringUTF(ret.to8Bit(true).c_str());
    }
    return env->NewStringUTF(EMPTY_STR.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1genre(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto &ret = instance->genre();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1year(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    auto ret = instance->year();
    return static_cast<jint>(ret);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1track(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    auto ret = instance->track();
    return static_cast<jint>(ret);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1disc(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    if (instance->contains("disk"))
        return instance->item("disk").toIntPair().first;
    return 0;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1lyrics(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    if (instance->contains("\251lyr")) {
        const auto &ret = instance->item("\251lyr").toStringList().toString(", ");
        return env->NewStringUTF(ret.to8Bit(true).c_str());
    }
    return env->NewStringUTF(EMPTY_STR.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1setTitle(JNIEnv *env, jclass, jlong ptr,
                                                        jstring title) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto titleChars = env->GetStringUTFChars(title, nullptr);
    instance->setTitle(String(titleChars, String::UTF8));
    env->ReleaseStringUTFChars(title, titleChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1setArtist(JNIEnv *env, jclass, jlong ptr,
                                                         jstring artist) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto artistChars = env->GetStringUTFChars(artist, nullptr);
    instance->setArtist(String(artistChars, String::UTF8));
    env->ReleaseStringUTFChars(artist, artistChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1setAlbum(JNIEnv *env, jclass, jlong ptr,
                                                        jstring album) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto albumChars = env->GetStringUTFChars(album, nullptr);
    instance->setAlbum(String(albumChars, String::UTF8));
    env->ReleaseStringUTFChars(album, albumChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1setAlbumArtist(JNIEnv *env, jclass, jlong ptr,
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
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1setGenre(JNIEnv *env, jclass, jlong ptr,
                                                        jstring genre) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    const auto genreChars = env->GetStringUTFChars(genre, nullptr);
    instance->setGenre(String(genreChars, String::UTF8));
    env->ReleaseStringUTFChars(genre, genreChars);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1setYear(JNIEnv *env, jclass, jlong ptr,
                                                       jint year) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    instance->setYear(static_cast<unsigned int>(year));
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1setTrack(JNIEnv *env, jclass, jlong ptr,
                                                        jint track) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    instance->setTrack(static_cast<unsigned int>(track));
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1setDisc(JNIEnv *env, jclass, jlong ptr,
                                                       jint disc) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    if (disc < 0) {
        instance->removeItem("disk");
    } else {
        instance->setItem("disk", MP4::Item(disc, 0));
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1setLyrics(JNIEnv *env, jclass, jlong ptr,
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

extern "C" JNIEXPORT jboolean JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1save(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    return static_cast<jboolean>(instance->save());
}

// AudioProperties
extern "C" JNIEXPORT jint JNICALL
Java_com_nomad88_taglib_android_TagLib_audioProperties_1bitrate(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<AudioProperties *>(ptr);
    auto ret = instance->bitrate();
    return static_cast<jint>(ret);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_nomad88_taglib_android_TagLib_audioProperties_1sampleRate(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<AudioProperties *>(ptr);
    auto ret = instance->sampleRate();
    return static_cast<jint>(ret);
}
