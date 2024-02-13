#include "Camera.h"

Camera::Camera(const glm::vec3& position, const glm::vec3& front, const glm::vec3& up)
	: m_position(position), m_front(front), m_up(up), m_left(glm::cross(up, front))
{
}


void Camera::setUp(const glm::vec3& position, const glm::vec3& front, const glm::vec3& up)
{
	m_position = position;
	m_front = front;
	m_up = up;
}

void Camera::orbit(float pitch, float yaw)
{
	glm::mat4 rot(1.0f);
	rot = glm::rotate(rot, glm::radians(pitch), m_left);
	rot = glm::rotate(rot, glm::radians(yaw), m_up);
	auto temp = m_position - m_front;
	m_position = (temp * glm::mat3(rot)) + m_front;

}

void Camera::pan(float pitch, float yaw, float roll)
{
	glm::mat4 rot(1.0f);
	rot = glm::rotate(rot, glm::radians(pitch), m_left);
	rot =  glm::rotate(rot, glm::radians(yaw), m_up);
	m_front = m_front * glm::mat3(rot);
	m_left = glm::cross(m_up, m_front);
}

void Camera::moveForward(float amount)
{
	m_position += m_front * amount;
}

void Camera::moveForward(const glm::vec3& amount)
{
	m_position += m_front * amount;
}

void Camera::moveUp(float amount) {
	m_position += m_up * amount;
}


void Camera::strafe(float amount)
{
	m_position += m_left * amount;
}


glm::mat4 Camera::calculateViewMatrix() const
{	
	return glm::lookAt(m_position, m_position + m_front, m_up);
}
