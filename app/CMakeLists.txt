# DON'T TOUCH
cmake_minimum_required(VERSION 3.4.1)

add_library(native-lib
             SHARED
             src/main/cpp/native-lib.cpp )

find_library(log-lib
             log ) # Specifies the name of the NDK library
# DON'T TOUCH

set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

# OpenCV Part
include_directories(/home/akshay/Projects/vision/OpenCV-android-sdk/sdk/native/jni/include)
add_library(lib_opencv SHARED IMPORTED )
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libopencv_java3.so)

target_link_libraries(native-lib ${log-lib} lib_opencv)