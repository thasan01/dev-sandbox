cmake_minimum_required(VERSION 3.21)
set(VCPKG_MANIFEST_MODE ON)

# Setting CMAKE_TOOLCHAIN_FILE is better handled via a CMake Preset 
# or passed on the command line, but this is fine if you're only using Vcpkg.
# If you are passing the toolchain via the command line (which you are with 
# the preset in your history), you don't strictly need this line here.

project(opengl-sandbox)
set(CMAKE_CXX_STANDARD 20) # Enable c++20 standard

# 1. FIND PACKAGES: These are correct. Vcpkg ensures these modules are found.
find_package(GLEW REQUIRED)
find_package(glm CONFIG REQUIRED)
find_package(SDL2 CONFIG REQUIRED)
 
# 2. DEFINE LIBRARIES: This is where you remove the Windows names.
#    The Vcpkg targets (GLEW::GLEW, glm::glm, SDL2::SDL2, etc.) 
#    automatically resolve to the correct library files on Windows, Linux, and macOS.
set (OPENGL_COMMON_LIB GLEW::GLEW glm::glm SDL2::SDL2main SDL2::SDL2 )

add_executable(app1 app1-hello-triangles.cpp )

# 3. LINK LIBRARIES: Link the modern, cross-platform targets.
target_link_libraries(app1 PRIVATE ${OPENGL_COMMON_LIB} )