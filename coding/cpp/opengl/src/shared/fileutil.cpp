#include "fileutil.h"

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
