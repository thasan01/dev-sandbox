#include "VertexBufferObject.h"

void createVAO(const MeshData& mesh, GLuint& vao, GLuint vbo[4], int flag) {
	glGenVertexArrays(1, &vao);
	glGenBuffers(4, vbo);

	SizeType texcoordByteSize = sizeof(VertexType) * 2;
	SizeType vertexByteSize = sizeof(VertexType) * 3;
	SizeType triFaceByteSize = sizeof(IndexType) * 3;

	SizeType maxVerticiesBytes = vertexByteSize * mesh.numVertices;
	SizeType maxTexcoordBytes = texcoordByteSize * mesh.numVertices;
	SizeType maxTriFacesBytes = triFaceByteSize * mesh.numTriIndices;

	// bind Vertex Array Object
	glBindVertexArray(vao);

	if ((flag & VBO_VERTEX) == VBO_VERTEX) {
		// copy vertices array in a vertex buffer for OpenGL to use
		glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
		glBufferData(GL_ARRAY_BUFFER, maxVerticiesBytes, mesh.vertices, GL_STATIC_DRAW);

		// set the vertex attributes pointers
		glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, vertexByteSize, (void*)0);
		glEnableVertexAttribArray(0);
	}

	if ((flag & VBO_NORMAL) == VBO_NORMAL) {
		glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
		glBufferData(GL_ARRAY_BUFFER, maxVerticiesBytes, mesh.normals, GL_STATIC_DRAW);

		glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, vertexByteSize, (void*)0);
		glEnableVertexAttribArray(1);
	}

	if ((flag & VBO_TEXCOORD) == VBO_TEXCOORD) {
		glBindBuffer(GL_ARRAY_BUFFER, vbo[2]);
		glBufferData(GL_ARRAY_BUFFER, maxTexcoordBytes, mesh.texcoords, GL_STATIC_DRAW);

		glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, texcoordByteSize, (void*)0);
		glEnableVertexAttribArray(2);
	}

	// Copy index array in a element buffer for OpenGL to use
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo[3]);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, maxTriFacesBytes, mesh.triIndices, GL_STATIC_DRAW);
}
