#include <ApplicationServices/ApplicationServices.h>
#include <jni.h>

JNIEXPORT jint JNICALL Java_MacMouse_warpCursor(JNIEnv *env, jclass type, jint x, jint y)
{
    (void)env;
    (void)type;
    return (jint)CGWarpMouseCursorPosition(CGPointMake(x, y));
}
