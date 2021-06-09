#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_kangwang_ffmpeglibrary_play_FFmepegVideo_native_1version(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}