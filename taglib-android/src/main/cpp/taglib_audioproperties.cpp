#include "taglib_wrapper.h"

#include <audioproperties.h>

using namespace std;
using namespace TagLib;

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
