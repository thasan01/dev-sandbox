FROM mcr.microsoft.com/windows/servercore:ltsc2022

# Install Chocolatey (package manager for Windows)
RUN powershell -Command \
    Set-ExecutionPolicy Bypass -Scope Process -Force; \
    [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; \
    iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Install Visual Studio Build Tools with C++ workload
RUN choco install -y visualstudio2022buildtools --package-parameters "--add Microsoft.VisualStudio.Workload.VCTools --includeRecommended --quiet --norestart"

# Optional: Install additional tools like CMake or Git if needed
RUN choco install -y cmake git

# Install vcpkg
RUN git clone https://github.com/microsoft/vcpkg.git C:\vcpkg && \
    C:\vcpkg\bootstrap-vcpkg.bat -disableMetrics && \
    C:\vcpkg\vcpkg.exe integrate install

# Set environment variables for vcpkg
ENV VCPKG_ROOT="C:\vcpkg"
#ENV PATH="C:\vcpkg;${PATH}"

# Set working directory
WORKDIR /app

# Default command: Launch VS Developer Command Prompt for auto env setup, then cmd.exe
CMD ["C:\\Program Files (x86)\\Microsoft Visual Studio\\2022\\BuildTools\\Common7\\Tools\\VsDevCmd.bat", "-arch=x64", "&&", "cmd.exe"]