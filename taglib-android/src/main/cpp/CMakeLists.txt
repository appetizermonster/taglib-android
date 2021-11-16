cmake_minimum_required(VERSION 3.0.0 FATAL_ERROR)

project(taglib-android)

add_subdirectory(taglib)

include_directories(
  ${CMAKE_CURRENT_SOURCE_DIR}/taglib
  ${CMAKE_CURRENT_SOURCE_DIR}/taglib/taglib
  ${CMAKE_CURRENT_SOURCE_DIR}/taglib/taglib/toolkit
  ${CMAKE_CURRENT_SOURCE_DIR}/taglib/taglib/ape
  ${CMAKE_CURRENT_SOURCE_DIR}/taglib/taglib/mpeg
  ${CMAKE_CURRENT_SOURCE_DIR}/taglib/taglib/mpeg/id3v1
  ${CMAKE_CURRENT_SOURCE_DIR}/taglib/taglib/mpeg/id3v2
  ${CMAKE_CURRENT_SOURCE_DIR}/taglib/taglib/mpeg/id3v2/frames
  ${CMAKE_CURRENT_SOURCE_DIR}/taglib/bindings/c/
)

add_library(taglibwrapper SHARED taglib_mp4file.cpp taglib_mp4tag.cpp taglib_audioproperties.cpp)

find_library(log-lib log)
target_link_libraries(taglibwrapper tag ${log-lib})
