#include <jni.h>
#include <string>

#include <fileref.h>
#include <tag.h>
#include <tpropertymap.h>

using namespace std;
using namespace TagLib;

// FileRef
extern "C" JNIEXPORT jlong JNICALL
Java_com_nomad88_taglib_android_TagLib_fileRef_1create(JNIEnv *env, jclass, jstring filePath) {
    auto filePathChars = env->GetStringUTFChars(filePath, nullptr);
    auto *instance = new FileRef(filePathChars);
    env->ReleaseStringUTFChars(filePath, filePathChars);
    return (jlong) instance;
}

extern "C" JNIEXPORT void JNICALL
Java_com_nomad88_taglib_android_TagLib_fileRef_1release(JNIEnv *env, jclass, jlong ptr) {
    auto *instance = reinterpret_cast<FileRef *>(ptr);
    delete instance;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_nomad88_taglib_android_TagLib_fileRef_1isNull(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<FileRef *>(ptr);
    bool ret = instance->isNull();
    return static_cast<jboolean>(ret);
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_nomad88_taglib_android_TagLib_fileRef_1tag(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<FileRef *>(ptr);
    return reinterpret_cast<jlong>(instance->tag());
}

// Tag
extern "C" JNIEXPORT jstring JNICALL
Java_com_nomad88_taglib_android_TagLib_tag_1title(JNIEnv *env, jclass, jlong ptr) {
    auto instance = reinterpret_cast<Tag *>(ptr);
    auto ret = instance->title();
    return env->NewStringUTF(ret.to8Bit(true).c_str());
}
