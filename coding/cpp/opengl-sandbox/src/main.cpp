// GLAD MUST be included before GLFW
#include <glad/glad.h>
#include <GLFW/glfw3.h>

// GLM Headers
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>

#include <iostream>

// Callback function to adjust the viewport when the user resizes the window
void framebuffer_size_callback(GLFWwindow* window, int width, int height) {
    glViewport(0, 0, width, height);
}

int main() {
    // ------------------------------------------------------------------------
    // GLM Verification Test
    // ------------------------------------------------------------------------
    std::cout << "Initializing project dependencies..." << std::endl;
    
    glm::vec4 vec(1.0f, 0.0f, 0.0f, 1.0f);
    glm::mat4 trans = glm::mat4(1.0f); // Identity matrix
    trans = glm::translate(trans, glm::vec3(1.0f, 1.0f, 0.0f)); // Translate by (1,1,0)
    vec = trans * vec;
    
    std::cout << "GLM Test Success! Vector moved to: (" 
              << vec.x << ", " << vec.y << ", " << vec.z << ")\n" << std::endl;

    // ------------------------------------------------------------------------
    // GLFW Initialization & Configuration
    // ------------------------------------------------------------------------
    if (!glfwInit()) {
        std::cerr << "Failed to initialize GLFW" << std::endl;
        return -1;
    }

    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

    #ifdef __APPLE__
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE); // Required for macOS
    #endif

    // ------------------------------------------------------------------------
    // Window Creation
    // ------------------------------------------------------------------------
    GLFWwindow* window = glfwCreateWindow(800, 600, "OpenGL Hello World + GLM", NULL, NULL);
    if (!window) {
        std::cerr << "Failed to create GLFW window" << std::endl;
        glfwTerminate();
        return -1;
    }
    glfwMakeContextCurrent(window);
    glfwSetFramebufferSizeCallback(window, framebuffer_size_callback);

    // ------------------------------------------------------------------------
    // GLAD Initialization
    // ------------------------------------------------------------------------
    if (!gladLoadGLLoader((GLADloadproc)glfwGetProcAddress)) {
        std::cerr << "Failed to initialize GLAD" << std::endl;
        return -1;
    }

    // ------------------------------------------------------------------------
    // Main Render Loop
    // ------------------------------------------------------------------------
    while (!glfwWindowShouldClose(window)) {
        // Input handling
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetWindowShouldClose(window, true);
        }

        // Rendering commands
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f); // Dark teal background
        glClear(GL_COLOR_BUFFER_BIT);         // Clear the back buffer

        // Swap buffers and poll window events
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    // ------------------------------------------------------------------------
    // Cleanup
    // ------------------------------------------------------------------------
    glfwTerminate();
    return 0;
}