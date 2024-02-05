#include "Shader.h"

void loadShaderProgram(GLuint& programId,
	const std::string& vertexShaderFilename,
	const std::string& fragmentShaderFilename
)
{
	glGetError();//reset the error flag
	std::string source;

	loadShaderFromFile(vertexShaderFilename, source);
	const char* ptrVertSource = source.data();
	GLuint vertShaderId = glCreateShader(GL_VERTEX_SHADER);
	glShaderSource(vertShaderId, 1, &ptrVertSource, 0);
	glCompileShader(vertShaderId);

	loadShaderFromFile(fragmentShaderFilename, source);
	const char* ptrFragSource = source.data();
	GLuint fragShaderId = glCreateShader(GL_FRAGMENT_SHADER);
	glShaderSource(fragShaderId, 1, &ptrFragSource, 0);
	glCompileShader(fragShaderId);

	programId = glCreateProgram();

	attachShader(programId, vertShaderId, "vertex shader");
	attachShader(programId, fragShaderId, "fragment shader");

	glLinkProgram(programId);

	GLint status;
	glGetProgramiv(programId, GL_LINK_STATUS, &status);

	if (status == GL_FALSE)
		throw "Failed to link program";

	glDeleteShader(vertShaderId);
	glDeleteShader(fragShaderId);
}

void attachShader(GLuint programId, GLuint shaderId, const std::string& shaderName) {

	std::stringstream errorss;
	std::string buffer;
	int buffer_length = 0;

	glGetShaderiv(shaderId, GL_INFO_LOG_LENGTH, &buffer_length);

	if (buffer_length == 0)
		glAttachShader(programId, shaderId);
	else {
		buffer.resize(buffer_length);
		glGetShaderInfoLog(shaderId, buffer_length, &buffer_length, &buffer[0]);

		errorss << "Encountered error in ShaderLog: " << shaderName << std::endl << buffer << std::endl;
		throw errorss.str();
	}

}