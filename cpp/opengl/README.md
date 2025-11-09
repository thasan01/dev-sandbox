
For Windows
```
docker build -m 4GB -t opengl-sandbox:windows-v0.1 -f .devops/Dockerfile.windows .
docker run -it --rm -v ./:C:/app --name opengl-sandbox opengl-sandbox:windows-v0.1
```

For Linux
```
docker build -t opengl-sandbox:linux-v0.1 -f .devops/Dockerfile.linux .
docker run -it --rm -v ./:/opt/sandbox --name opengl-sandbox opengl-sandbox:linux-v0.1 /bin/bash
``` 



In VSCode, install `Dev Containers` extension

```
cmake -S src -B build --preset debug -DCMAKE_TOOLCHAIN_FILE=%VCPKG_ROOT%/scripts/buildsystems/vcpkg.cmake 
cmake --build build --config {Debug|Release}
```