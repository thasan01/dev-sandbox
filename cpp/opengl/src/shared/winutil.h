#pragma once
#include <SDL2/SDL.h>
#include <iostream>
#include <string>
#include <sstream>

int popupErrorMessage(std::ostream& titleStream, std::ostream& messageStream, SDL_Window* parentWindow = NULL);
