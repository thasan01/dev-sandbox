#pragma once

#include <memory>
typedef unsigned int SizeType;
typedef float VertexType;
typedef unsigned int IndexType;

#define VBO_VERTEX		1 << 0
#define VBO_NORMAL		1 << 1
#define VBO_TEXCOORD	1 << 2


class MeshData  {
	public:
	~MeshData() {
		delete[] vertices;
		delete[] normals;
		delete[] texcoords;
		delete[] triIndices;
	}

	SizeType numVertices;
	SizeType numTriIndices;
	VertexType* vertices;
	VertexType* normals;
	VertexType* texcoords;
	IndexType* triIndices;
};

std::shared_ptr<MeshData> createCube();
std::shared_ptr<MeshData> createTriangle();
std::shared_ptr<MeshData> createSquare();
