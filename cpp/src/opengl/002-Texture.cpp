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

	loadShaderProgram(shaderProgramId, "../data/shader/glsl/model-v1.vert", "../data/shader/glsl/model-v1.frag");
	glUseProgram(shaderProgramId);

	auto sptrMesh = createCube();
	auto renderFlag = (VBO_VERTEX | VBO_NORMAL | VBO_TEXCOORD);
	createVAO(*sptrMesh.get(), meshVAO, meshVBOs, renderFlag);


	// NEW: Load & bind texture
	GLuint textureId = loadTextureFile("../data/texture/brick.jpg");

	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, textureId);
	// End NEW 

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

		//=========
		// Render
		//=========
		glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);

		glBindVertexArray(meshVAO);
		glDrawElements(GL_TRIANGLES, sptrMesh.get()->numTriIndices * 3, GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);

		SDL_GL_SwapWindow(window);
	}

	//Cleanup GL objects

	//NEW: Unload Texture
	glBindTexture(GL_TEXTURE_2D, 0);
	glDeleteTextures(1, &textureId);
	//End NEW

	glDeleteBuffers(4, meshVBOs);
	glDeleteVertexArrays(1, &meshVAO);
	glDeleteProgram(shaderProgramId);

	SDL_GL_DeleteContext(context);
	SDL_DestroyWindow(window);

	//Quit SDL subsystems
	SDL_Quit();

	return 0;
}