#include "taglib_wrapper.h"

#include <ogg/vorbis/vorbisfile.h>
#include <tfilestream.h>

using namespace std;
using namespace TagLib;

extern "C" JNIEXPORT jboolean JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisFileNative_isSupported(JNIEnv *env, jobject,
                                                                         jstring filePath) {
    auto filePathChars = env->GetStringUTFChars(filePath, nullptr);
    auto fileStream = new FileStream(filePathChars, true);
    auto isOggVorbisFile = Ogg::Vorbis::File::isSupported(fileStream);
    delete fileStream;
    env->ReleaseStringUTFChars(filePath, filePathChars);
    return static_cast<jboolean>(isOggVorbisFile);
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisFileNative_create(JNIEnv *env, jobject,
                                                                    jstring filePath,
                                                                    jboolean readAudioProperties) {
    auto filePathChars = env->GetStringUTFChars(filePath, nullptr);
    auto *instance = new Ogg::Vorbis::File(filePathChars, readAudioProperties);
    env->ReleaseStringUTFChars(filePath, filePathChars);
    if (!instance->isValid()) {
        delete instance;
        instance = nullptr;
    }
    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisFileNative_release(JNIEnv *env, jobject,
                                                                     jlong ptr) {
    auto *instance = reinterpret_cast<Ogg::Vorbis::File *>(ptr);
    delete instance;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisFileNative_save(JNIEnv *env, jobject, jlong ptr) {
    auto *instance = reinterpret_cast<Ogg::Vorbis::File *>(ptr);
    return static_cast<jboolean>(instance->save());
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisFileNative_tag(JNIEnv *env, jobject, jlong ptr) {
    auto instance = reinterpret_cast<Ogg::Vorbis::File *>(ptr);
    return reinterpret_cast<jlong>(instance->tag());
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_nomad88_taglib_android_internal_OggVorbisFileNative_audioProperties(JNIEnv *env, jobject,
                                                                             jlong ptr) {
    auto instance = reinterpret_cast<Ogg::Vorbis::File *>(ptr);
    return reinterpret_cast<jlong>(instance->audioProperties());
}
