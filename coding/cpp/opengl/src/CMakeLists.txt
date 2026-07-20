cmake_minimum_required(VERSION 3.20)

if(WIN32)
    # Ship a self-contained exe: statically link the MSVC CRT, and use
    # vcpkg's static triplet so glfw3/glad/glm are static libs too (no
    # separate glfw3.dll to distribute alongside the exe). The debug CRT
    # (MSVCP140D.dll etc.) isn't redistributable at all, so this is what
    # lets the exe run on machines without Visual Studio / Build Tools —
    # the whole point, since this app ships to users, not just builders.
    if(NOT DEFINED VCPKG_TARGET_TRIPLET)
        set(VCPKG_TARGET_TRIPLET "x64-windows-static" CACHE STRING "")
    endif()
    set(CMAKE_MSVC_RUNTIME_LIBRARY "MultiThreaded$<$<CONFIG:Debug>:Debug>")
endif()

project(OpenGLHelloWorld)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Find packages provided by vcpkg
find_package(glfw3 CONFIG REQUIRED)
find_package(glad CONFIG REQUIRED)
find_package(glm CONFIG REQUIRED)
find_package(OpenGL REQUIRED)

# Create the executable
add_executable(HelloWorld main.cpp)

# Link the libraries
target_link_libraries(HelloWorld PRIVATE 
    glfw
    glad::glad
    glm::glm
    OpenGL::GL
)