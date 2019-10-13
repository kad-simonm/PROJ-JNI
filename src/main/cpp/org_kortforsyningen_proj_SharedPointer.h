/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_kortforsyningen_proj_SharedPointer */

#ifndef _Included_org_kortforsyningen_proj_SharedPointer
#define _Included_org_kortforsyningen_proj_SharedPointer
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_kortforsyningen_proj_SharedPointer
 * Method:    getDimension
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_kortforsyningen_proj_SharedPointer_getDimension
  (JNIEnv *, jobject);

/*
 * Class:     org_kortforsyningen_proj_SharedPointer
 * Method:    inverse
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_org_kortforsyningen_proj_SharedPointer_inverse
  (JNIEnv *, jobject);

/*
 * Class:     org_kortforsyningen_proj_SharedPointer
 * Method:    format
 * Signature: (Lorg/kortforsyningen/proj/Context;IIZZ)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_kortforsyningen_proj_SharedPointer_format
  (JNIEnv *, jobject, jobject, jint, jint, jboolean, jboolean);

/*
 * Class:     org_kortforsyningen_proj_SharedPointer
 * Method:    rawPointer
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_org_kortforsyningen_proj_SharedPointer_rawPointer
  (JNIEnv *, jobject);

/*
 * Class:     org_kortforsyningen_proj_SharedPointer
 * Method:    run
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_kortforsyningen_proj_SharedPointer_run
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
