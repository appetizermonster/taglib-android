#include "taglib_wrapper.h"

#include <mp4/mp4file.h>
#include <tfilestream.h>

using namespace std;
using namespace TagLib;

extern "C" JNIEXPORT jboolean JNICALL
Java_com_nomad88_taglib_android_internal_MP4FileNative_isSupported(JNIEnv *env, jobject,
                                                                   jstring filePath) {
    auto filePathChars = env->GetStringUTFChars(filePath, nullptr);
    auto fileStream = new FileStream(filePathChars, true);
    auto isMP4File = MP4::File::isSupported(fileStream);
    delete fileStream;
    env->ReleaseStringUTFChars(filePath, filePathChars);
    return static_cast<jboolean>(isMP4File);
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_nomad88_taglib_android_internal_MP4FileNative_create(JNIEnv *env, jobject,
                                                              jstring filePath,
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
Java_com_nomad88_taglib_android_internal_MP4FileNative_release(JNIEnv *env, jobject, jlong ptr) {
    auto *instance = reinterpret_cast<MP4::File *>(ptr);
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_nomad88_taglib_android_internal_MP4FileNative_tag(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<MP4::File *>(ptr);
    return reinterpret_cast<jlong>(instance->tag());
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_nomad88_taglib_android_internal_MP4FileNative_audioProperties(JNIEnv *env, jobject,
                                                                       jlong ptr) {
    auto instance = reinterpret_cast<MP4::File *>(ptr);
    return reinterpret_cast<jlong>(instance->audioProperties());
}
