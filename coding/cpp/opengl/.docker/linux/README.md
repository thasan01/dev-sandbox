# cppbuild-linux — Linux C++ build container

GCC/G++, CMake, Ninja, Git, vcpkg, and X11/OpenGL dev headers, bundled into
an Ubuntu 24.04 image. Drops you into a `bash` shell with the toolchain
already on `PATH`.

## Requirements

- Docker Desktop set to **Linux containers** (this is the default on
  macOS/Linux hosts; on Windows, switch via the Docker Desktop tray icon —
  "Switch to Linux containers..." — if it's currently set to Windows
  containers, since a single Docker Desktop instance runs one or the
  other, not both at once).

## Build the image

From the repository root (the build context only needs this directory —
`src/` isn't copied into the image, it gets bind-mounted at container run
time instead):

```bash
docker build -t cppbuild-linux:latest .docker/linux
```

This installs build-essential, CMake, Ninja, Git, X11/OpenGL dev headers,
and vcpkg — expect a few minutes (mostly `apt-get` + the vcpkg clone).

## Run it against your project

From the directory you want built (e.g. this repo's root), mount it into
`/app` and start an interactive shell there:

```bash
docker run --rm -it -v "$(pwd):/app" -w /app cppbuild-linux:latest
```

- `-v "$(pwd):/app"` bind-mounts your current directory into the container
  at `/app` — edits on the host are reflected immediately, no rebuild
  needed.
- `-w /app` starts the shell in that mounted directory.

### Compiling

Directly with GCC:

```
g++ -std=c++17 main.cpp -o main
```

With CMake + vcpkg (vcpkg is at `/vcpkg`, exposed via `VCPKG_ROOT`):

```
cmake -B build -S . -DCMAKE_TOOLCHAIN_FILE="$VCPKG_ROOT/scripts/buildsystems/vcpkg.cmake"
cmake --build build
```

## Building this repo's sample project from scratch

This repo's own CMake project lives in [../../src/](../../src/), not the
repo root, so the `-S` path below points there. Run both commands from the
repository root. From a clean checkout (no image, no container, no
`build/` directory):

```bash
# 1. Build the image (one-time; a few minutes)
docker build -t cppbuild-linux:latest .docker/linux

# 2. Start a container with the repo root mounted at /app
docker run --rm -it -v "$(pwd):/app" -w /app cppbuild-linux:latest
```

Then, inside the container shell:

```
cmake -B /app/build -S /app/src -DCMAKE_TOOLCHAIN_FILE="$VCPKG_ROOT/scripts/buildsystems/vcpkg.cmake"
cmake --build /app/build
```

vcpkg resolves [../../src/vcpkg.json](../../src/vcpkg.json) and builds
glfw3, glad, and glm from source on first configure (a few minutes). The
resulting binary is at `/app/build/HelloWorld`.

Note: this container only *builds* the project — GLFW's X11 backend needs
its headers at compile time, but nothing here opens an actual window, so
no display or `Xvfb` is required just to build.
