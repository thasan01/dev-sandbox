#ifdef _WIN32
#include <Windows.h>
#pragma comment(linker, "/SUBSYSTEM:windows /ENTRY:mainCRTStartup")
#endif

#include <SDL2/SDL.h>

// The primary header for modern OpenGL applications using GLEW:
#include <GL/glew.h> 

// C++ math headers:
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>

// Standard C++ headers:
#include <iostream>
#include <string>
#include <memory>
#include <sstream>
#include <vector>
#include <map>
#include <fstream>
// --- END UPDATED INCLUDES ---

#define ss() std::ostringstream().flush()

int popupErrorMessage(std::ostream& titleStream, std::ostream& messageStream, SDL_Window* parentWindow = NULL)
{
	std::string title = dynamic_cast<std::ostringstream&>(titleStream).str();
	std::string message = dynamic_cast<std::ostringstream&>(messageStream).str();
	return SDL_ShowSimpleMessageBox(SDL_MESSAGEBOX_ERROR, title.c_str(), message.c_str(), parentWindow);
}

bool loadTextFromFile(const std::string& filename, std::string& source)
{
	std::stringstream ss;

	std::string line;
	std::ifstream rfile;
	rfile.open(filename);
	if (rfile.is_open()) {
		while (std::getline(rfile, line)) {
			ss << line << std::endl;
		}
		rfile.close();
		source = ss.str();
		return true;
	}
	return false;
}

void initGL()
{
	glShadeModel(GL_SMOOTH);
	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	glReadBuffer(GL_BACK);
	glDrawBuffer(GL_BACK);
	glEnable(GL_DEPTH_TEST);

	glClearColor(0.5, 0.5, 0.5, 1.0);
	glClearDepth(1.0);
}

void createVAO(GLuint& vao, GLuint vbo[], int& numTriIndices) {

	int numVertices = 24;
	numTriIndices = 12;

	float vertices[] = {
		-0.5, -0.5, -0.5,
		0.5, -0.5, -0.5,
		0.5, 0.5, -0.5,
		-0.5, 0.5, -0.5,
		-0.5, -0.5, 0.5,
		-0.5, 0.5, 0.5,
		0.5, 0.5, 0.5,
		0.5, -0.5, 0.5,
		-0.5, -0.5, 0.5,
		-0.5, -0.5, -0.5,
		-0.5, 0.5, -0.5,
		-0.5, 0.5, 0.5,
		0.5, -0.5, 0.5,
		0.5, 0.5, 0.5,
		0.5, 0.5, -0.5,
		0.5, -0.5, -0.5,
		-0.5, 0.5, -0.5,
		0.5, 0.5, -0.5,
		0.5, 0.5, 0.5,
		-0.5, 0.5, 0.5,
		-0.5, -0.5, -0.5,
		-0.5, -0.5, 0.5,
		0.5, -0.5, 0.5,
		0.5, -0.5, -0.5,
	};

	float normals[] = {
		0, 0, -1,
			0, 0, -1,
			0, 0, -1,
			0, 0, -1,
			0, 0, 1,
			0, 0, 1,
			0, 0, 1,
			0, 0, 1,
			-1, 0, 0,
			-1, 0, 0,
			-1, 0, 0,
			-1, 0, 0,
			1, 0, 0,
			1, 0, 0,
			1, 0, 0,
			1, 0, 0,
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
			0, -1, 0,
			0, -1, 0,
			0, -1, 0,
			0, -1, 0,
	};

	float texcoords[] = {
		0, 0,
		1, 0,
		1, 1,
		0, 1,
		0, 0,
		0, 1,
		1, 1,
		1, 0,
		0, 0,
		1, 0,
		1, 1,
		0, 1,
		0, 0,
		0, 1,
		1, 1,
		1, 0,
		0, 0,
		1, 0,
		1, 1,
		0, 1,
		0, 0,
		0, 1,
		1, 1,
		1, 0,
	};

	unsigned int triIndices[] = {
		0, 2, 1,
		0, 3, 2,
		4, 6, 5,
		4, 7, 6,
		8, 10, 9,
		8, 11, 10,
		12, 14, 13,
		12, 15, 14,
		16, 18, 17,
		16, 19, 18,
		20, 22, 21,
		20, 23, 22,
	};

	glGenVertexArrays(1, &vao);
	glGenBuffers(4, vbo);

	int texcoordByteSize = sizeof(float) * 2;
	int vertexByteSize = sizeof(float) * 3;
	int triFaceByteSize = sizeof(unsigned int) * 3;

	int maxVerticiesBytes = vertexByteSize * numVertices;
	int maxTexcoordBytes = texcoordByteSize * numVertices;
	int maxTriFacesBytes = triFaceByteSize * numTriIndices;

	// bind Vertex Array Object
	glBindVertexArray(vao);

	//Vertex Attribute
	// copy vertices array in a vertex buffer for OpenGL to use
	glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
	glBufferData(GL_ARRAY_BUFFER, maxVerticiesBytes, vertices, GL_STATIC_DRAW);

	// set the vertex attributes pointers
	glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, vertexByteSize, (void*)0);
	glEnableVertexAttribArray(0);
	//

	{
		glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
		glBufferData(GL_ARRAY_BUFFER, maxVerticiesBytes, normals, GL_STATIC_DRAW);

		glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, vertexByteSize, (void*)0);
		glEnableVertexAttribArray(1);
	}

	{
		glBindBuffer(GL_ARRAY_BUFFER, vbo[2]);
		glBufferData(GL_ARRAY_BUFFER, maxTexcoordBytes, texcoords, GL_STATIC_DRAW);

		glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, texcoordByteSize, (void*)0);
		glEnableVertexAttribArray(2);
	}

	// Copy index array in a element buffer for OpenGL to use
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo[3]);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, maxTriFacesBytes, triIndices, GL_STATIC_DRAW);
}


GLuint createShader(const std::string& shaderSource, GLuint shaderType) {
	const char* ptrSource = shaderSource.data();
	GLuint shaderId = glCreateShader(shaderType);
	glShaderSource(shaderId, 1, &ptrSource, 0);
	glCompileShader(shaderId);
	return shaderId;
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
		throw errorss.str().c_str();
	}
}

GLuint createShaderProgram(const std::string& vsFile, const std::string& fsFile) {

	std::string source;
	loadTextFromFile(vsFile, source);
	GLuint vsId = createShader(source, GL_VERTEX_SHADER);

	loadTextFromFile(fsFile, source);
	GLuint fsId = createShader(source, GL_FRAGMENT_SHADER);

	GLuint programId = glCreateProgram();
	attachShader(programId, vsId, "Vertex Shader");
	attachShader(programId, fsId, "Fragment Shader");

	glLinkProgram(programId);

	GLint status;
	glGetProgramiv(programId, GL_LINK_STATUS, &status);

	if (status == GL_FALSE)
		throw "Failed to link program";

	glDeleteShader(vsId);
	glDeleteShader(fsId);
	return programId;
}

void setupCamera(GLuint programId, int width, int height)
{
	glm::mat4 model(1.0);
	glm::mat4 view = glm::lookAt(glm::vec3(0,0,-5), glm::vec3(0,0,0), glm::vec3(0, 1, 0));
	glm::mat4 projection = glm::perspective(45.0f, float(width)/float(height), 0.1f, 100.0f);
	glm::mat4 pvm = projection * view* model;

	GLint locPvm = glGetUniformLocation(programId, "projModelViewMatrix");

	if (locPvm == -1) {
		popupErrorMessage(ss() << "Undefined unofirm location", 
			ss() << "Uniform locartion for projModelViewMatrix is -1 for Shader " << programId);
	}

	glUniformMatrix4fv(locPvm, 1, GL_FALSE, glm::value_ptr(pvm));
}

//==============================
//  Entrypoint
//==============================
int main(int argc, char* argv[]) {

	int x = SDL_WINDOWPOS_CENTERED;
	int y = SDL_WINDOWPOS_CENTERED;
	int width = 640;
	int height = 480;
	SDL_Window* window = SDL_CreateWindow("Windows", x, y, width, height, SDL_WINDOW_OPENGL);
	SDL_GLContext context = SDL_GL_CreateContext(window);

	//Initialize GLEW
	if (glewInit() != GLEW_OK)
	{
		popupErrorMessage(ss() << "GLEW Initialization Error", ss() << "Failed to initalize glew", window);
		return -1;
	}

	//Initialize SDL
	if (SDL_Init(SDL_INIT_VIDEO) < 0)
	{
		popupErrorMessage(ss() << "SDL Initialization Error", ss() << SDL_GetError(), window);
		return -1;
	}

	GLuint programId = createShaderProgram("../data/glsl/scene.vert", "../data/glsl/scene.frag");
	glUseProgram(programId);
	setupCamera(programId, width, height);

	int numTriIndices;
	GLuint vao, vbo[4];
	initGL();
	createVAO(vao, vbo, numTriIndices);	

	bool quit = false;
	SDL_Event e;

	while (!quit)
	{
		while (SDL_PollEvent(&e) != 0)
		{
			if (e.type == SDL_QUIT)
			{
				quit = true;
			}
		}

		//=========
		// Render
		//=========
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		//Render the mesh
		glBindVertexArray(vao);
		glDrawElements(GL_TRIANGLES, numTriIndices * 3, GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);

		SDL_GL_SwapWindow(window);
	}

	//GL clean up
	glDeleteBuffers(4, vbo);
	glDeleteVertexArrays(1, &vao);
	glDeleteProgram(programId);

	SDL_GL_DeleteContext(context);
	SDL_DestroyWindow(window);

	//Quit SDL subsystems
	SDL_Quit();

	return 0;
}