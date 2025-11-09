#pragma once

#include <iostream>
#include <string>
#include <memory>
#include <sstream>
#include <fstream>

#define ss() std::ostringstream().flush()

bool loadTextFromFile(const std::string& filename, std::string& source);