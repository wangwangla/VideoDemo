
cmake_minimum_required(VERSION 3.4.1)

project("ffmpeddemo")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
set(libs ${CMAKE_SOURCE_DIR}/../libs)
set(libname play)  #编译之后的名字  不一样要和文件名相同

#编译的文件 在include文件夹下、、、

include_directories(
        include
        play
        utils
        ${CMAKE_SOURCE_DIR}/util
        ${CMAKE_SOURCE_DIR}/playvideo/PlayFFmpeg.c
        ${CMAKE_SOURCE_DIR}/playvideo/PlayFFmpeg.h
)

link_directories(
        ${libs}/${ANDROID_ABI})

#编译那些文件
file(GLOB src-files
        ${CMAKE_SOURCE_DIR}/utils/*.cpp
        ${CMAKE_SOURCE_DIR}/utils/*.h
        ${CMAKE_SOURCE_DIR}/playvideo/PlayFFmpeg.h
        ${CMAKE_SOURCE_DIR}/playvideo/PlayFFmpeg.c
        )

add_library( # Sets the name of the library.
        ${libname}
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        ${src-files}  #加入的就是上面指定文件下的所有
        )

set(third-party-libs
        avformat
        avcodec
        avfilter
        swresample
        swscale
        avutil
        fdk-aac
        x264
        )

set(native-libs
        android
        log
        OpenSLES
        )

target_link_libraries( # Specifies the target library.
        ${libname}
        ${log-lib}
        ${third-party-libs}
        ${native-libs}
        android
        z
        OpenSLES
        )