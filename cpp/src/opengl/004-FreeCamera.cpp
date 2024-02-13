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

#pragma comment(linker, "/SUBSYSTEM:windows /ENTRY:mainCRTStartup")

GLuint shaderProgramId;
GLuint meshVAO;
GLuint meshVBOs[4]; //3 VertexBufferObjects + 1 ElementBufferObject

//NEW: 
Camera camera = Camera(glm::vec3(0,0,-5), glm::vec3(0,0,1), glm::vec3(0,1,0));


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

//NEW: move camrea based on input keys
void processInput(float dt) {

	const float MOVE_SPEED = 0.25f;
	const float TURN_SPEED = 2.5f;

	glm::vec3 move(0.0f);
	glm::vec3 turn(0.0f);
	bool moved = false;
	bool turned = false;

	//Get list of which keys are pressed
	auto keystate = SDL_GetKeyboardState(NULL);

	//
	if (keystate[SDL_SCANCODE_W])
	{
		move[2] += MOVE_SPEED * dt;
		moved = true;
	}
	else if (keystate[SDL_SCANCODE_S])
	{
		move[2] -= MOVE_SPEED * dt;
		moved = true;
	}

	if (keystate[SDL_SCANCODE_A])
	{
		move[0] = MOVE_SPEED * dt;
		moved = true;
	}
	else if (keystate[SDL_SCANCODE_D])
	{
		move[0] -= MOVE_SPEED * dt;
		moved = true;
	}

	if (keystate[SDL_SCANCODE_Q])
	{
		move[1] += MOVE_SPEED * dt;
		moved = true;
	}
	else if (keystate[SDL_SCANCODE_E])
	{
		move[1] -= MOVE_SPEED * dt;
		moved = true;
	}


	if (keystate[SDL_SCANCODE_UP]) {
		turn[0] = TURN_SPEED * dt;
		turned = true;
	}
	else if (keystate[SDL_SCANCODE_DOWN]) {
		turn[0] -= TURN_SPEED * dt;
		turned = true;
	}

	if (keystate[SDL_SCANCODE_LEFT])
	{
		turn[1] -= TURN_SPEED * dt;
		turned = true;
	}
	else if (keystate[SDL_SCANCODE_RIGHT])
	{
		turn[1] += TURN_SPEED * dt;
		turned = true;
	}

	if (turned)
		camera.pan(turn[0], turn[1], 0);

	if (moved) {
		camera.moveForward(glm::vec3(move[2],0,move[2]));
		camera.moveUp(move[1]);
		camera.strafe(move[0]);
	}


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

	loadShaderProgram(shaderProgramId, "../data/shader/glsl/model-v2.vert", "../data/shader/glsl/model-v2.frag");
	glUseProgram(shaderProgramId);

	glm::mat4 projection = glm::perspective<float>(45.0f, float(width) / float(height), 0.01f, 100.0f);
	glm::mat4 model = glm::mat4(1.0);

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

	bool bQuit = false;
	SDL_Event event;

	//NEW: delta time step
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

		//NEW: 
		processInput(dt);

		glm::mat4 view = camera.calculateViewMatrix();
		glm::mat4 pvmMatrix = projection * view * model;
		int locPVM = glGetUniformLocation(shaderProgramId, "u_pvmMatrix");
		glUniformMatrix4fv(locPVM, 1, GL_FALSE, glm::value_ptr(pvmMatrix));


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