cmake_minimum_required(VERSION 3.21)
set(VCPKG_MANIFEST_MODE ON)
project(cpp_sandbox)
set(CMAKE_CXX_STANDARD 20)            # Enable c++20 standard


find_package(GLEW REQUIRED)
find_package(glm CONFIG REQUIRED)
find_package(SDL2 CONFIG REQUIRED)
find_package(freeimage CONFIG REQUIRED)
  

add_executable(OOP-Classes oop/Classes.cpp)
add_executable(DataStructures-Vectors datastructures/Vectors.cpp)
add_executable(RAII-Pointers raii/Pointers.cpp)

set (OPENGL_COMMON_SRC 
		opengl/Mesh.h opengl/Cube.cpp opengl/Triangle.cpp opengl/Square.cpp opengl/Camera.h opengl/Camera.cpp 
		opengl/Shader.h opengl/Shader.cpp opengl/VertexBufferObject.h opengl/VertexBufferObject.cpp opengl/Texture.h opengl/Texture.cpp
		opengl/FileUtility.h opengl/FileUtility.cpp		
	)

set (OPENGL_COMMON_LIB 
		OpenGL32 GlU32 GLEW::GLEW glm::glm
        SDL2::SDL2 SDL2::SDL2main
		freeimage::FreeImage freeimage::FreeImagePlus
	)

#OpenGL-001-HelloTriangle 
add_executable(OpenGL-001-HelloTriangle opengl/001-HelloTri.cpp ${OPENGL_COMMON_SRC})	
target_link_libraries(OpenGL-001-HelloTriangle PRIVATE ${OPENGL_COMMON_LIB} )

#OpenGL-002-Texture 
add_executable(OpenGL-002-Texture opengl/002-Texture.cpp ${OPENGL_COMMON_SRC})	
target_link_libraries(OpenGL-002-Texture PRIVATE ${OPENGL_COMMON_LIB} )

#OpenGL-003-FixedCamera
add_executable(OpenGL-003-FixedCamera opengl/003-FixedCamera.cpp ${OPENGL_COMMON_SRC})	
target_link_libraries(OpenGL-003-FixedCamera PRIVATE ${OPENGL_COMMON_LIB} )

#OpenGL-004-FreeCamera
add_executable(OpenGL-004-FreeCamera opengl/004-FreeCamera.cpp ${OPENGL_COMMON_SRC})	
target_link_libraries(OpenGL-004-FreeCamera PRIVATE ${OPENGL_COMMON_LIB} )
