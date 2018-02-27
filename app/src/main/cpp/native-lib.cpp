#include <android/log.h>
#include <jni.h>
#include <string>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/features2d/features2d.hpp>
#include "aruco/marker.h"
#include "aruco/markerdetector.h"

using namespace std;
using namespace cv;
using namespace aruco;


extern "C"
{
void JNICALL Java_example_marker_detection_MainActivity_salt(JNIEnv *env, jobject instance,
                                                                           jlong matAddrGray,
                                                                           jint nbrElem) {
    Mat &mGr = *(Mat *) matAddrGray;
    for (int k = 0; k < nbrElem; k++) {
        int i = rand() % mGr.cols;
        int j = rand() % mGr.rows;
        mGr.at<uchar>(j, i) = 255;
    }
}

void JNICALL Java_example_marker_detection_MainActivity_detectMarker(JNIEnv *env, jobject instance,
                                                             jlong matAddrGray) {
    Mat &mGr = *(Mat *) matAddrGray;

    // TODO There is a resizing issue. Fails on large images ~ 760 x 1280
//    __android_log_print(2,"###","There are %d rows",mGr.rows);

    MarkerDetector MDetector;
    vector<Marker> Markers;
    MDetector.setDictionary("ARUCO_MIP_36h12");
    MDetector.setDetectionMode(DM_VIDEO_FAST,10);

    //Ok, let's detect
    MDetector.detect(mGr, Markers);

    //for each marker, draw info and its boundaries in the image
    for (auto &Marker : Markers) {
        cout << Marker << endl;
        Marker.draw(mGr, Scalar(0, 0, 255), 2);
    }

    // Memory friendly
    Markers.clear();
}
}
