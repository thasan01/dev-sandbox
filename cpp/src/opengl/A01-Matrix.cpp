#include <iostream>
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>

void print(const glm::mat4 m) {
	for(int i=0; i<4; i++)
		std::cout	
			<< m[i][0] 
			<< "\t" << m[i][1] 
			<< "\t" << m[i][2] 
			<< "\t" << m[i][3] 
			<< std::endl;
}

void print(const glm::vec4& v) {
		std::cout << "Vector3:::"
		<< v[0]
		<< "\t" << v[1]
		<< "\t" << v[2]
		<< "\t" << v[3]
		<< std::endl;
}

void extractTranslation(const glm::mat4 mat, glm::vec4& vec) {
	vec[0] = mat[3][0];
	vec[1] = mat[3][1];
	vec[2] = mat[3][2];
	vec[3] = mat[3][3];
}

int main(int argc, char* argv[]) {
	glm::mat4 m1(1.0f);

	glm::mat4 rot(1.0f);
	rot = glm::rotate(rot, glm::radians(45.0f), glm::vec3(1,0,0));

	glm::mat4 view = glm::lookAt(glm::vec3(0, 0, 5), glm::vec3(0, 0, 0), glm::vec3(0, 1, 0));
	//view = view * rot;
	//print(view);

	glm::vec4 pos;
	extractTranslation(view, pos);
	print(pos);
}