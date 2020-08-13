cmake_minimum_required(VERSION 3.4.1)

#设置FFmpeg头文件的路径
include_directories(
        include #因为和CMakeLists.txt在同一级，所以直接写include
)

add_library(ffmpeg4-util
        SHARED
        ffmpeg4-util.cpp)

find_library(
        log-lib
        log)

#1.定义一个变量avcodec
add_library(
        libavcodec
        SHARED
        IMPORTED
)
#给avcodec这个变量赋值
set_target_properties(libavcodec PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../../main/jniLibs/${ANDROID_ABI}/libavcodec.so)

#2.
add_library(
        libavdevice
        SHARED
        IMPORTED
)
set_target_properties(libavdevice PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../../main/jniLibs/${ANDROID_ABI}/libavdevice.so)

#3.
add_library(
        libavfilter
        SHARED
        IMPORTED
)
set_target_properties(libavfilter PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../../main/jniLibs/${ANDROID_ABI}/libavfilter.so)

#4.
add_library(
        libavformat
        SHARED
        IMPORTED
)
set_target_properties(libavformat PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../../main/jniLibs/${ANDROID_ABI}/libavformat.so)

#5.
add_library(
        libavutil
        SHARED
        IMPORTED
)
set_target_properties(libavutil PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../../main/jniLibs/${ANDROID_ABI}/libavutil.so)

#7.
add_library(
        libswresample
        SHARED
        IMPORTED
)
set_target_properties(libswresample PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../../main/jniLibs/${ANDROID_ABI}/libswresample.so)

#8.
add_library(
        libswscale
        SHARED
        IMPORTED
)
set_target_properties(libswscale PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../../main/jniLibs/${ANDROID_ABI}/libswscale.so)

#将avcodec混合编译到ffmpeg4-util中
target_link_libraries(
        ffmpeg4-util
        ${log-lib}
        libavcodec #1
        libavdevice #2
        libavfilter #3
        libavformat #4
        libavutil #5
        libswresample #7
        libswscale #8
)