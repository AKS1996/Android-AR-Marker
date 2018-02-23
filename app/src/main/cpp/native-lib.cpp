#include <jni.h>
#include <opencv2/core/mat.hpp>

void detect(cv::Mat InImage);

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_example_detectmarker_MainActivity_detectMarker(JNIEnv *env, jobject instance, jint width,
                                                    jint height, jbyteArray data_) {
    jbyte *data = env->GetByteArrayElements(data_, NULL);
    int len = env->GetArrayLength(data_);

    unsigned char* buf = new unsigned char[len];
    env->GetByteArrayRegion (data_, 0, len, reinterpret_cast<jbyte*>(buf));

    cv::Mat image(height, width, CV_8UC4, &buf[0]);

    image = image.t();

//    detect(image);

    jbyteArray newArray = env->NewByteArray(len);
    env->SetByteArrayRegion(newArray,0,len,data);

    // Memory Friendly
    env->ReleaseByteArrayElements(data_, data, 0);

    return newArray;

}

void detect(cv::Mat InImage){
//    try{
//        aruco::MarkerDetector MDetector;
//        std::vector<aruco::Marker> Markers;
//
//        //Ok, let's detect
//        MDetector.detect(InImage, Markers);
//
//        //for each marker, draw info and its boundaries in the image
//        for (auto &Marker : Markers) {
//            std::cout << Marker << std::endl;
//            Marker.draw(InImage, cv::Scalar(0, 0, 255), 2);
//        }
//
//    } catch (std::exception &ex){
//        std::cout<<"\nException :"<<ex.what();
//    }
}