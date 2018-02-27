cmake_minimum_required(VERSION 3.4.1)
find_library(log-lib log )

# OpenCV
include_directories(/home/akshay/Projects/vision/OpenCV-android-sdk/sdk/native/jni/include)
add_library( lib_opencv SHARED IMPORTED )
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libopencv_java3.so)
# OpenCV

## Removed inclusion of Eigen as a library, most errors gone
##

# ArUco
add_library( lib_aruco SHARED
        src/main/cpp/aruco/cameraparameters.cpp
        src/main/cpp/aruco/cvdrawingutils.cpp
        src/main/cpp/aruco/debug.cpp
        src/main/cpp/aruco/dictionary.cpp
        src/main/cpp/aruco/ippe.cpp
        src/main/cpp/aruco/marker.cpp
        src/main/cpp/aruco/markerdetector.cpp
        src/main/cpp/aruco/markerlabeler.cpp
        src/main/cpp/aruco/markermap.cpp
        src/main/cpp/aruco/posetracker.cpp
        )
target_link_libraries(lib_aruco lib_opencv  ${log-lib} )
# ArUco


# Native Lib
add_library(native-lib SHARED src/main/cpp/native-lib.cpp )
target_link_libraries(native-lib lib_aruco lib_opencv ${log-lib} )
# Native Lib