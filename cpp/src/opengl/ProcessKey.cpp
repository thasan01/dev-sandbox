#include "ProcessKey.h"
#include <SDL2/SDL.h>

void processKeys(float dt, Camera& camera)
{
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
		camera.moveForward(glm::vec3(move[2], 0, move[2]));
		camera.moveUp(move[1]);
		camera.strafe(move[0]);
	}
}
