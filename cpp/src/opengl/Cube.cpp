#include "Mesh.h"

std::shared_ptr<MeshData> createCube()
{
    MeshData* cube = new MeshData();
    cube->numVertices = 24;
    cube->numTriIndices = 12;

    cube->vertices = new VertexType[] {
        -0.5, -0.5, -0.5,
        0.5, -0.5, -0.5,
        0.5, 0.5, -0.5,
        -0.5, 0.5, -0.5,
        -0.5, -0.5, 0.5,
        -0.5, 0.5, 0.5,
        0.5, 0.5, 0.5,
        0.5, -0.5, 0.5,
        -0.5, -0.5, 0.5,
        -0.5, -0.5, -0.5,
        -0.5, 0.5, -0.5,
        -0.5, 0.5, 0.5,
        0.5, -0.5, 0.5,
        0.5, 0.5, 0.5,
        0.5, 0.5, -0.5,
        0.5, -0.5, -0.5,
        -0.5, 0.5, -0.5,
        0.5, 0.5, -0.5,
        0.5, 0.5, 0.5,
        -0.5, 0.5, 0.5,
        -0.5, -0.5, -0.5,
        -0.5, -0.5, 0.5,
        0.5, -0.5, 0.5,
        0.5, -0.5, -0.5,
    };

    cube->normals = new VertexType[] {
        0, 0, -1,
            0, 0, -1,
            0, 0, -1,
            0, 0, -1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            -1, 0, 0,
            -1, 0, 0,
            -1, 0, 0,
            -1, 0, 0,
            1, 0, 0,
            1, 0, 0,
            1, 0, 0,
            1, 0, 0,
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
            0, -1, 0,
            0, -1, 0,
            0, -1, 0,
            0, -1, 0,
    };

    cube->texcoords = new VertexType[] {
        0, 0,
        1, 0,
        1, 1,
        0, 1,
        0, 0,
        0, 1,
        1, 1,
        1, 0,
        0, 0,
        1, 0,
        1, 1,
        0, 1,
        0, 0,
        0, 1,
        1, 1,
        1, 0,
        0, 0,
        1, 0,
        1, 1,
        0, 1,
        0, 0,
        0, 1,
        1, 1,
        1, 0,
    };

    cube->triIndices = new IndexType[]{
        0, 2, 1,
        0, 3, 2,
        4, 6, 5,
        4, 7, 6,
        8, 10, 9,
        8, 11, 10,
        12, 14, 13,
        12, 15, 14,
        16, 18, 17,
        16, 19, 18,
        20, 22, 21,
        20, 23, 22,
    };

    return std::shared_ptr<MeshData>(cube);
}

