#ifndef TAGLIB_ANDROID_TAGLIB_WRAPPER_H
#define TAGLIB_ANDROID_TAGLIB_WRAPPER_H

#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "TagLibWrapper"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

static const std::string EMPTY_STR;

#endif //TAGLIB_ANDROID_TAGLIB_WRAPPER_H
