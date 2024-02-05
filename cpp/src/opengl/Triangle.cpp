#include "Mesh.h"

std::shared_ptr<MeshData> createTriangle()
{
    MeshData* tri= new MeshData();
    tri->numVertices = 3;
    tri->numTriIndices = 1;

    tri->vertices = new VertexType[]{
        -0.5f, -0.5f, 0.0f,
         0.5f, -0.5f, 0.0f,
         0.0f,  0.5f, 0.0f
    };

    tri->normals = new VertexType[]{
        0, 0, 1,
        0, 0, 1,
        0, 0, 1,
    };

    tri->texcoords = new VertexType[]{
        0, 0,
        1, 0,
        1, 1,
    };

    tri->triIndices = new IndexType[]{
        2, 0, 1
    };

    return std::shared_ptr<MeshData>(tri);
}
