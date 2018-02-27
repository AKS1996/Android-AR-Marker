cmake_minimum_required(VERSION 3.4.1)
find_library(log-lib log )

# OpenCV
include_directories(/home/akshay/Projects/vision/OpenCV-android-sdk/sdk/native/jni/include)
add_library( lib_opencv SHARED IMPORTED )
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libopencv_java3.so)
# OpenCV

## Removed inclusion of Eigen as a library, most errors gone
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen)
include_directories(
${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/Cholesky                ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/MetisSupport     ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/SparseLU
${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/CholmodSupport          ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/misc             ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/SparseQR
${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/Core                    ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/OrderingMethods  ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/SPQRSupport
${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/Eigenvalues             ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/PardisoSupport   ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/StlSupport
${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/Geometry                ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/PaStiXSupport    ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/SuperLUSupport
${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/Householder             ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/plugins          ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/SVD
${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/IterativeLinearSolvers  ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/QR               ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/UmfPackSupport
${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/Jacobi                  ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/SparseCholesky
${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/LU                      ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/Eigen/src/SparseCore
)
##

# ArUco
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/aruco)
add_library( aruco SHARED
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
        src/main/cpp/aruco/markerlabelers/dictionary_based.cpp
        src/main/cpp/aruco/markerlabelers/svmmarkers.cpp
        )
target_link_libraries(aruco lib_opencv  ${log-lib} )
# ArUco


# Native Lib
add_library(native-lib SHARED src/main/cpp/native-lib.cpp )
target_link_libraries(native-lib aruco lib_opencv ${log-lib} )
# Native Lib