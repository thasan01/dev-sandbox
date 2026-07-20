# cppbuild — Windows C++ build container

MSVC (cl.exe) + Windows SDK, CMake, Git, and vcpkg, bundled into a Windows
Server Core (ltsc2022) image. Drops you into a Developer Command Prompt with
the toolchain already on `PATH`.

## Requirements

- Windows 11 or Windows Server 2022 host with Docker configured for
  **Windows containers**.
- Hyper-V isolation (this is the default when the host is Windows 11/10
  client, which is required to run a Server Core-based image).
- **At least 8GB of memory** available to the container. The Visual Studio
  Build Tools + Windows SDK install needs it during both build and run — less
  than that and the container's utility VM gets silently killed mid-install.

## Build the image

From the repository root (the build context only needs this directory —
`src/` isn't copied into the image, it gets bind-mounted at container run
time instead):

```powershell
docker build -t cppbuild:latest -m 8GB .docker/windows
```

This installs VS Build Tools, Git, CMake, and vcpkg — expect it to take a
while (large downloads + VS Build Tools install). The `-m 8GB` is required
for the build to succeed; lower values fail partway through with a generic,
non-descriptive error.

## Run it against your project

From the directory you want built (e.g. this repo's root), mount it into
`C:\app` and start an interactive shell there:

```powershell
docker run --rm -it -m 8GB -v "${PWD}:C:\app" -w C:\app cppbuild:latest
```

- `-v "${PWD}:C:\app"` bind-mounts your current directory into the container
  at `C:\app` — edits on the host are reflected immediately, no rebuild
  needed.
- `-w C:\app` starts the shell in that mounted directory.
- `-m 8GB` — same memory floor as the build; compiling needs headroom too.

The container's entrypoint runs `VsDevCmd.bat -arch=amd64 -winsdk=10.0.22621.0`
before dropping you into `cmd.exe`, so `cl.exe`, `INCLUDE`, `LIB`, and `PATH`
are already set up — no extra setup needed. (The SDK is pinned explicitly,
and the image build removes any other installed SDK version, because a
newer SDK's `rc.exe`/`mt.exe` crash with an access violation on this
ltsc2022 base — see comments in the [Dockerfile](Dockerfile) for details.
`VCPKG_ROOT` is likewise re-exported to `C:\vcpkg` after `VsDevCmd.bat` runs,
since VsDevCmd otherwise overwrites it with VS's own bundled vcpkg copy.)

### Compiling

Directly with MSVC:

```
cl /EHsc main.cpp
```

With CMake + vcpkg (vcpkg is at `C:\vcpkg`, exposed via `VCPKG_ROOT`):

```
cmake -B build -S src -DCMAKE_TOOLCHAIN_FILE="%VCPKG_ROOT%\scripts\buildsystems\vcpkg.cmake"
cmake --build build
```

## Building this repo's sample project from scratch

This repo's own CMake project lives in [../../src/](../../src/), not the
repo root, so the `-S` path below points there. Run both commands from the
repository root. From a clean checkout (no image, no container, no
`build/` directory):

```powershell
# 1. Build the image (one-time; ~30+ min, large downloads + VS Build Tools install)
docker build -t cppbuild:latest -m 8GB .docker/windows

# 2. Start a container with the repo root mounted at C:\app
docker run --rm -it -m 8GB -v "${PWD}:C:\app" -w C:\app cppbuild:latest
```

Then, inside the container shell:

```
cmake -B C:\app\build -S C:\app\src -DCMAKE_TOOLCHAIN_FILE="%VCPKG_ROOT%\scripts\buildsystems\vcpkg.cmake"
cmake --build C:\app\build
```

vcpkg resolves [../../src/vcpkg.json](../../src/vcpkg.json) and builds glfw3, glad, and
glm from source on first configure (a few minutes). The resulting binary is
at `C:\app\build\Debug\HelloWorld.exe`.
