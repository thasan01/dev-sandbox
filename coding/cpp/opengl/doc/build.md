# Docker Building for Windows Container
## Build the image
```
docker build -t cppbuild:latest -m 8GB .docker/windows
```

## Run conatiner

If you are using powershell:
```powershell
docker run --rm -it -m 8GB -v "${PWD}:C:\app" -w C:\app cppbuild:latest
```
OR
if you are using Windows Command Prompt (cmd):

```cmd
docker run --rm -it -m 8GB -v "%cd%":C:\app -w C:\app cppbuild:latest
```

## Build the project

From inside the container
```
cmake -B C:\app\build -S C:\app\src -DCMAKE_TOOLCHAIN_FILE="%VCPKG_ROOT%\scripts\buildsystems\vcpkg.cmake" -DCMAKE_BUILD_TYPE=Release
cmake --build C:\app\build
```

# Docker Build for Linux Container
## Build the image
```
docker build -t cppbuild-linux:latest .docker/linux
```

## Run conatiner
```bash
docker run --rm -it -v "$(pwd):/app" -w /app cppbuild-linux:latest
```

## Build the project
From inside the container
```
cmake -B build -S src -DCMAKE_TOOLCHAIN_FILE="$VCPKG_ROOT/scripts/buildsystems/vcpkg.cmake" -DCMAKE_BUILD_TYPE=Release
cmake --build /app/build
```
