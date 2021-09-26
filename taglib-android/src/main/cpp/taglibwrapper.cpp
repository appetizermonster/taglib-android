#include <jni.h>
#include <string>
#include <android/log.h>

#include <mp4/mp4file.h>
#include <mp4/mp4tag.h>
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
extern "C" JNIEXPORT jlong JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4File_1create(JNIEnv *env, jclass, jstring filePath) {
    auto filePathChars = env->GetStringUTFChars(filePath, nullptr);
    auto fileStream = new FileStream(filePathChars);
    auto isMP4File = MP4::File::isSupported(fileStream);
    MP4::File *instance = nullptr;
    if (isMP4File) {
        instance = new MP4::File(filePathChars);
    }
    delete fileStream;
    env->ReleaseStringUTFChars(filePath, filePathChars);
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

// Tag
extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1title(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    auto ret = instance->title();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1artist(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    auto ret = instance->artist();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1album(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    auto ret = instance->album();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1albumArtist(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<MP4::Tag *>(ptr);
    if (instance->contains("aART")) {
        auto ret = instance->item("aART").toStringList().toString(", ");
        return env->NewStringUTF(ret.to8Bit(true).c_str());
    }
    return env->NewStringUTF(EMPTY_STR.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_TagLib_mp4Tag_1genre(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    auto ret = instance->genre();
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
        auto ret = instance->item("\251lyr").toStringList().toString(", ");
        return env->NewStringUTF(ret.to8Bit(true).c_str());
    }
    return env->NewStringUTF(EMPTY_STR.c_str());
}

// AudioProperties
extern "C" JNIEXPORT jint JNICALL
Java_com_nomad88_taglib_android_TagLib_audioProperties_1bitrate(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<AudioProperties *>(ptr);
    auto ret = instance->bitrate();
    return static_cast<jint>(ret);
}
