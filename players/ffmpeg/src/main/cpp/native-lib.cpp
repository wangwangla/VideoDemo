#include <jni.h>
#include <string>
#include <libavcodec/version.h>
#include <libavcodec/avcodec.h>
#include "util/LogUtils.h"

extern "C" {
#include <android/native_window_jni.h>
#include <libswscale/swscale.h>
#include <libavutil/imgutils.h>
#include <libavcodec/version.h>
#include <libavcodec/avcodec.h>
#include <libavformat/version.h>
#include <libavutil/version.h>
#include <libavfilter/version.h>
#include <libswresample/version.h>
#include <libswscale/version.h>
#include <libavformat/avformat.h>
#include <libswresample/swresample.h>
#include <jni.h>
#include <libavutil/audio_fifo.h>
#include <libavutil/avstring.h>
#include <libavutil/audio_fifo.h>
#include <libavutil/avstring.h>
};
extern "C" JNIEXPORT jstring JNICALL
Java_com_kangwang_ffmpeglibrary_play_FFmepegVideo_native_1version(
        JNIEnv* env,
        jobject /* this */) {
//    std::string hello = "Hello from C++";
//    return env->NewStringUTF(hello.c_str());
    char strBuffer[1024 * 4] = {0};
    strcat(strBuffer, "libavcodec : ");
    strcat(strBuffer, AV_STRINGIFY(LIBAVCODEC_VERSION));
    strcat(strBuffer, "\nlibavformat : ");
    strcat(strBuffer, AV_STRINGIFY(LIBAVFORMAT_VERSION));
    strcat(strBuffer, "\nlibavutil : ");
    strcat(strBuffer, AV_STRINGIFY(LIBAVUTIL_VERSION));
    strcat(strBuffer, "\nlibavfilter : ");
    strcat(strBuffer, AV_STRINGIFY(LIBAVFILTER_VERSION));
    strcat(strBuffer, "\nlibswresample : ");
    strcat(strBuffer, AV_STRINGIFY(LIBSWRESAMPLE_VERSION));
    strcat(strBuffer, "\nlibswscale : ");
    strcat(strBuffer, AV_STRINGIFY(LIBSWSCALE_VERSION));
    strcat(strBuffer, "\navcodec_configure : \n");
//    strcat(strBuffer, avcodec_configuration());
    strcat(strBuffer, "\navcodec_license : ");
//    strcat(strBuffer, avcodec_license());
    LOGCATE("GetFFmpegVersion\n%s", strBuffer);
    return env->NewStringUTF(strBuffer);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_kangwang_ffmpeglibrary_play_FFmepegVideo_prepare(JNIEnv *env, jobject thiz,
                                                          jstring video_path) {


}