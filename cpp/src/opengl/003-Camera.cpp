#include <Windows.h>
#include <SDL2/SDL.h>
#include <gl/glew.h>
#include <gl/GL.h>
#include <gl/GLU.h>
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>

#include "Shader.h"
#include "Mesh.h"
#include "VertexBufferObject.h"
#include "FileUtility.h"
#include "Texture.h"

#pragma comment(linker, "/SUBSYSTEM:windows /ENTRY:mainCRTStartup")

GLuint shaderProgramId;
GLuint meshVAO;
GLuint meshVBOs[4]; //3 VertexBufferObjects + 1 ElementBufferObject

void initGL()
{
	glShadeModel(GL_SMOOTH);
	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	glReadBuffer(GL_BACK);
	glDrawBuffer(GL_BACK);
	glEnable(GL_DEPTH_TEST);

	glClearColor(0.5, 0.5, 0.5, 1.0);
	glClearDepth(1.0);

	glEnable(GL_TEXTURE_2D);
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

	if (glewInit() != GLEW_OK)
	{
		MessageBox(NULL, TEXT("Failed to initalize glew"), TEXT("ERROR"), 0);
		return -1;
	}

	initGL(); //NEW:

	loadShaderProgram(shaderProgramId, "../data/shader/glsl/model-v2.vert", "../data/shader/glsl/model-v2.frag");
	glUseProgram(shaderProgramId);

	//NEW: Setup Camera
	glm::mat4 view = glm::lookAt(glm::vec3(0, 0, 2), glm::vec3(0, 0, 0), glm::vec3(0, 1, 0));
	glm::mat4 projection = glm::perspective<float>(45.0f, float(width) / float(height), 1.0f, 100.0f);
	int locPVM = glGetUniformLocation(shaderProgramId, "u_pvmMatrix");
	float rotY = 0.0f;
	//END NEW

	auto sptrMesh = createCube();
	auto renderFlag = (VBO_VERTEX | VBO_TEXCOORD);
	createVAO(*sptrMesh.get(), meshVAO, meshVBOs, renderFlag);

	//Load & bind texture
	GLuint textureId = loadTextureFile("../data/texture/brick.jpg");

	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, textureId);

	//Initialize SDL
	if (SDL_Init(SDL_INIT_VIDEO) < 0)
	{
		MessageBox(NULL, TEXT("Failed to initalize SDL"), TEXT(SDL_GetError()), 0);
		return -1;
	}

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

		//NEW: 
		glm::mat4 model = glm::mat4(1.0);
		model = glm::rotate(model, glm::radians(rotY), glm::vec3(0, 1, 0));
		glm::mat4 pvmMatrix = projection * view * model;
		glUniformMatrix4fv(locPVM, 1, GL_FALSE, glm::value_ptr(pvmMatrix));
		rotY += 0.05;
		while (rotY > 360) rotY -= 360;
		//END NEW 

		//=========
		// Render
		//=========
		glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glBindVertexArray(meshVAO);
		glDrawElements(GL_TRIANGLES, sptrMesh.get()->numTriIndices * 3, GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);

		SDL_GL_SwapWindow(window);
	}

	//Cleanup GL objects

	//Unload Texture
	glBindTexture(GL_TEXTURE_2D, 0);
	glDeleteTextures(1, &textureId);

	glDeleteBuffers(4, meshVBOs);
	glDeleteVertexArrays(1, &meshVAO);
	glDeleteProgram(shaderProgramId);

	SDL_GL_DeleteContext(context);
	SDL_DestroyWindow(window);

	//Quit SDL subsystems
	SDL_Quit();

	return 0;
}