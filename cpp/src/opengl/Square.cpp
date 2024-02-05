#include "Mesh.h"
std::shared_ptr<MeshData> createSquare()
{
    MeshData* square = new MeshData();
    square->numVertices = 4;
    square->numTriIndices = 2;

    square->vertices = new VertexType[]{
     0.5f,  0.5f, 0.0f,  // top right
     0.5f, -0.5f, 0.0f,  // bottom right
    -0.5f, -0.5f, 0.0f,  // bottom left
    -0.5f,  0.5f, 0.0f   // top left 
    };

    square->normals = new VertexType[]{
        0.0f, 0.0f, 0.0f, //1.0f,
        0.0f, 0.0f, 0.0f, //1.0f,
        0.0f, 0.0f, 0.0f, //1.0f,
        0.0f, 0.0f, 0.0f, //1.0f,
    };

    square->texcoords = new VertexType[]{
        0.0f, 0.0f,
        1.0f, 0.0f,
        1.0f, 1.0f,
        0.0f, 1.0f,
    };

    square->triIndices = new IndexType[]{
        0, 1, 3,   // first triangle
        1, 2, 3    // second triangle
    };

    return std::shared_ptr<MeshData>(square);

}
