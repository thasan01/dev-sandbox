#include "winutil.h"

int popupErrorMessage(std::ostream& titleStream, std::ostream& messageStream, SDL_Window* parentWindow)
{
	std::string title = dynamic_cast<std::ostringstream&>(titleStream).str();
	std::string message = dynamic_cast<std::ostringstream&>(messageStream).str();
	return SDL_ShowSimpleMessageBox(SDL_MESSAGEBOX_ERROR, title.c_str(), message.c_str(), parentWindow);
}