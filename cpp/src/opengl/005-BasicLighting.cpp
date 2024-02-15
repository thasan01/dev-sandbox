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
#include "Camera.h"
#include "VertexBufferObject.h"
#include "FileUtility.h"
#include "Texture.h"
#include "ProcessKey.h"

#pragma comment(linker, "/SUBSYSTEM:windows /ENTRY:mainCRTStartup")

GLuint shaderProgramId;
GLuint meshVAO;
GLuint meshVBOs[4]; //3 VertexBufferObjects + 1 ElementBufferObject

Camera camera = Camera(glm::vec3(0, 0, -5), glm::vec3(0, 0, 1), glm::vec3(0, 1, 0));


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

	initGL();

	loadShaderProgram(shaderProgramId, "../data/shader/glsl/model-v3.vert", "../data/shader/glsl/model-v3.frag");
	glUseProgram(shaderProgramId);

	//NEW:
	glm::mat4 projection = glm::perspective<float>(45.0f, float(width) / float(height), 0.01f, 100.0f);
	glm::mat4 model = glm::mat4(1.0);

	int locModelMatrix = glGetUniformLocation(shaderProgramId, "u_model");
	int locViewMatrix = glGetUniformLocation(shaderProgramId, "u_view");
	int locProjectionMatrix = glGetUniformLocation(shaderProgramId, "u_projection");
	int locViewPosition = glGetUniformLocation(shaderProgramId, "u_viewPosition");

	glUniformMatrix4fv(locModelMatrix, 1, GL_FALSE, glm::value_ptr(model));
	glUniformMatrix4fv(locProjectionMatrix, 1, GL_FALSE, glm::value_ptr(projection));

	//material shader locations
	int locMatAmbient = glGetUniformLocation(shaderProgramId, "material.ambient");
	int locMatDiffuse = glGetUniformLocation(shaderProgramId, "material.diffuse");
	int locMatSpecular = glGetUniformLocation(shaderProgramId, "material.specular");
	int locMatShininess = glGetUniformLocation(shaderProgramId, "material.shininess");

	glUniform3fv(locMatAmbient, 1, glm::value_ptr(glm::vec3(1.0f, 0.5f, 0.31f)));
	glUniform3fv(locMatDiffuse, 1, glm::value_ptr(glm::vec3(1.0f, 0.5f, 0.31f)));
	glUniform3fv(locMatSpecular, 1, glm::value_ptr(glm::vec3(0.5f, 0.5f, 0.5f)));
	glUniform1f(locMatShininess, 32.0f);

	//light shader locations
	int locLightAmbient = glGetUniformLocation(shaderProgramId, "light.ambient");
	int locLightDiffuse = glGetUniformLocation(shaderProgramId, "light.diffuse");
	int locLightSpecular = glGetUniformLocation(shaderProgramId, "light.specular");
	int locLightPosition = glGetUniformLocation(shaderProgramId, "light.position");

	glUniform3fv(locLightAmbient, 1, glm::value_ptr(glm::vec3(0.2f, 0.2f, 0.2f)));
	glUniform3fv(locLightDiffuse, 1, glm::value_ptr(glm::vec3(0.5f, 0.5f, 0.5f)));
	glUniform3fv(locLightSpecular, 1, glm::value_ptr(glm::vec3(1.0f, 1.0f, 1.0f)));
	glUniform3fv(locLightPosition, 1, glm::value_ptr(glm::vec3(10.0f, 10.0f, -10.0f)));
	//END NEW

	auto sptrMesh = createCube();
	auto renderFlag = (VBO_VERTEX | VBO_NORMAL | VBO_TEXCOORD);
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

	bool bQuit = false;
	SDL_Event event;

	//delta time step
	float dt = 0.01;

	while (!bQuit)
	{
		while (SDL_PollEvent(&event) != 0)
		{
			if (event.type == SDL_QUIT)
			{
				bQuit = true;
				break;
			}
		}

		processKeys(dt, camera);

		//NEW:
		glm::mat4 view = camera.calculateViewMatrix();
		glUniformMatrix4fv(locViewMatrix, 1, GL_FALSE, glm::value_ptr(view));
		glUniform3fv(locViewPosition, 1, glm::value_ptr(camera.getPosition()));
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