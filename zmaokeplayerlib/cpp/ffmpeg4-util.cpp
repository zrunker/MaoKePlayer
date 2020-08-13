#include <jni.h>
#include <string>
#include <remuxer.h>

extern "C"
JNIEXPORT jboolean JNICALL
Java_cc_ibooker_android_zmaokeplayerlib_MaoKePlayerUtil_reMuxer(JNIEnv *env, jclass clazz,
                                                            jstring in_path, jstring out_path) {
    // 获取文件地址指针 - Java的String转为C的字符串
    const char *inPath = env->GetStringUTFChars(in_path, nullptr);
    const char *outPath = env->GetStringUTFChars(out_path, nullptr);
    // 执行格式转换
    int result = main_muxer(inPath, outPath);
    // 回收指针
    env->ReleaseStringUTFChars(in_path, inPath);
    env->ReleaseStringUTFChars(out_path, outPath);
    return static_cast<jboolean>(result == 0);
}


