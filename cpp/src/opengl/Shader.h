#pragma once

#include <gl/glew.h>
#include <gl/GL.h>
#include <gl/GLU.h>
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>
#include <string>
#include <iostream>
#include "FileUtility.h"

void loadShaderProgram(GLuint& programId, const std::string& vertexShaderFilename, const std::string& fragmentShaderFilename);
void attachShader(GLuint programId, GLuint shaderId, const std::string& shaderName);