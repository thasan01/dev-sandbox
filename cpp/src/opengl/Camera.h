#pragma once
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>

class Camera {
	public:

		Camera(const glm::vec3& position, const glm::vec3& front, const glm::vec3& up);

		void setUp(const glm::vec3& position, const glm::vec3& front, const glm::vec3& up);

		void moveForward(float amount);
		void moveForward(const glm::vec3& amount);
		void strafe(float amount);

		void pan(float pitch, float yaw, float roll);
		void orbit(float pitch, float yaw );

		const glm::mat4& calculateViewMatrix();
	
	private:

		glm::vec3 m_position;
		glm::vec3 m_front;
		glm::vec3 m_up;

		glm::vec3 m_left; //derived value

};