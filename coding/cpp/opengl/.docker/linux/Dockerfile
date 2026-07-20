# =============================================================================
#  Linux C++ build container
#  Contents: GCC/G++, CMake, Ninja, Git, vcpkg, X11/OpenGL dev headers
#  Base:     Ubuntu 24.04 LTS
# =============================================================================

ARG FROM_IMAGE=ubuntu:24.04
FROM ${FROM_IMAGE}

ENV DEBIAN_FRONTEND=noninteractive

# -----------------------------------------------------------------------------
# 1. Toolchain + vcpkg's build-time requirements
#    - build-essential/cmake/ninja/git/pkg-config: the actual toolchain
#    - curl/zip/unzip/tar/ca-certificates: vcpkg downloads and unpacks
#      portable copies of tools (and port sources) with these
#    - libx11-dev and friends: GLFW's X11 backend needs these headers to
#      compile, even though nothing here ever opens a window (no display
#      is expected in this container — it only needs to build, not run,
#      the project)
#    - python3: some vcpkg ports (e.g. glad's code generator) need it
# -----------------------------------------------------------------------------
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    cmake \
    ninja-build \
    git \
    curl \
    zip \
    unzip \
    tar \
    pkg-config \
    ca-certificates \
    python3 \
    libx11-dev \
    libxrandr-dev \
    libxinerama-dev \
    libxcursor-dev \
    libxi-dev \
    libxext-dev \
    libgl1-mesa-dev \
    && rm -rf /var/lib/apt/lists/*

# -----------------------------------------------------------------------------
# 2. vcpkg
# -----------------------------------------------------------------------------
RUN git clone https://github.com/microsoft/vcpkg /vcpkg && \
    /vcpkg/bootstrap-vcpkg.sh -disableMetrics

# Expose vcpkg on PATH and via VCPKG_ROOT (CMake toolchain uses this)
ENV VCPKG_ROOT=/vcpkg
ENV PATH="${VCPKG_ROOT}:${PATH}"

# -----------------------------------------------------------------------------
# 3. Entry point: drop you into a shell with the toolchain already on PATH
#    (no VsDevCmd-style environment bootstrap needed on Linux — gcc/g++ and
#    vcpkg are just on PATH from the ENV above)
# -----------------------------------------------------------------------------
WORKDIR /src
ENTRYPOINT ["/bin/bash"]
