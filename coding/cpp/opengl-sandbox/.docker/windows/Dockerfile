# escape=`
# =============================================================================
#  Windows C++ build container
#  Contents: MSVC compiler (cl.exe) + Windows SDK, CMake, Git, vcpkg
#  Base:     Windows Server Core LTSC2022  (needs a Win11 / Server 2022 host,
#            or run/build with --isolation=hyperv on other Windows versions)
# =============================================================================

ARG FROM_IMAGE=mcr.microsoft.com/windows/servercore:ltsc2022
FROM ${FROM_IMAGE}

# The Build Tools installer is a batch program, so use the classic cmd shell.
SHELL ["cmd", "/S", "/C"]

# -----------------------------------------------------------------------------
# 1. Visual Studio Build Tools = MSVC toolset + Windows SDK  (no IDE)
# -----------------------------------------------------------------------------
COPY Install.cmd C:\TEMP\
ADD https://aka.ms/vscollect.exe            C:\TEMP\collect.exe
ADD https://aka.ms/vs/17/release/channel    C:\TEMP\VisualStudio.chman
ADD https://aka.ms/vs/17/release/vs_buildtools.exe C:\TEMP\vs_buildtools.exe

#    --includeRecommended is intentionally omitted: it pulls in a newer
#    Windows SDK (observed: 10.0.26100.0) alongside the one requested below.
#    That newer SDK's rc.exe/mt.exe crash with an access violation on this
#    ltsc2022 (build 20348) base, since they depend on newer OS APIs than
#    the container's kernel provides — see the cleanup step after this.
RUN C:\TEMP\Install.cmd C:\TEMP\vs_buildtools.exe --quiet --wait --norestart --nocache `
    --channelUri C:\TEMP\VisualStudio.chman `
    --installChannelUri C:\TEMP\VisualStudio.chman `
    --installPath "%ProgramFiles(x86)%\Microsoft Visual Studio\2022\BuildTools" `
    --add Microsoft.VisualStudio.Workload.VCTools `
    --add Microsoft.VisualStudio.Component.VC.Tools.x86.x64 `
    --add Microsoft.VisualStudio.Component.Windows11SDK.22621 `
 && del /q C:\TEMP\vs_buildtools.exe

# -----------------------------------------------------------------------------
# 2. Switch to PowerShell for the remaining tools
# -----------------------------------------------------------------------------
SHELL ["powershell", "-NoLogo", "-ExecutionPolicy", "Bypass", "-Command", "$ErrorActionPreference='Stop'; $ProgressPreference='SilentlyContinue';"]

# Belt-and-suspenders: remove any Windows SDK version other than the one
# requested above. MSBuild and CMake's Ninja/RC-compiler detection both
# pick their default SDK from the registry (Microsoft SDKs\Windows\v10.0
# ProductVersion / Installed Roots), not from PATH — so if a second, newer
# SDK ever ends up installed (e.g. pulled in transitively by a future
# channel update), it would silently become the default and reintroduce
# the rc.exe crash described above. This keeps 22621 the only, and
# therefore default, installed SDK regardless of what else gets added.
# (A separate .ps1 file is used instead of an inline RUN because Docker's
# Windows shell-form quoting mangles the multi-word -Value string here.)
COPY Cleanup-WindowsSdk.ps1 C:\TEMP\
RUN C:\TEMP\Cleanup-WindowsSdk.ps1

# Chocolatey (used only to fetch Git + CMake cleanly)
RUN Set-ExecutionPolicy Bypass -Scope Process -Force; `
    [Net.ServicePointManager]::SecurityProtocol = [Net.ServicePointManager]::SecurityProtocol -bor 3072; `
    iex ((New-Object Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Git (needed by vcpkg) and a current standalone CMake, both added to PATH
RUN choco install -y git --no-progress; `
    choco install -y cmake --no-progress --installargs 'ADD_CMAKE_TO_PATH=System'

# -----------------------------------------------------------------------------
# 3. vcpkg
# -----------------------------------------------------------------------------
RUN git clone https://github.com/microsoft/vcpkg C:\vcpkg; `
    C:\vcpkg\bootstrap-vcpkg.bat -disableMetrics

# Expose vcpkg on PATH and via VCPKG_ROOT (CMake toolchain uses this)
ENV VCPKG_ROOT=C:\vcpkg
RUN [Environment]::SetEnvironmentVariable( `
      'Path', `
      [Environment]::GetEnvironmentVariable('Path','Machine') + ';C:\vcpkg', `
      'Machine')

# -----------------------------------------------------------------------------
# 4. Entry point: drop you into a shell with the MSVC dev environment loaded
#    (VsDevCmd sets INCLUDE / LIB / PATH so cl.exe works immediately)
#
#    This runs through Entrypoint.cmd rather than being expressed as a
#    chained "&&" command directly in the ENTRYPOINT array: Docker's
#    Windows array-to-command-line reconstruction wraps arguments
#    containing spaces (like the VsDevCmd.bat path) in quotes, and once
#    another array element also needs literal quotes (to protect a `set`
#    value from cmd's trailing-space-before-`&&` gotcha), the two quoting
#    schemes collide and corrupt each other. A plain script sidesteps that
#    entirely — see comments in Entrypoint.cmd for the -winsdk and
#    VCPKG_ROOT details.
# -----------------------------------------------------------------------------
COPY Entrypoint.cmd C:\
WORKDIR C:\src
ENTRYPOINT ["C:\\Entrypoint.cmd"]