@echo off
rem -winsdk pins the Windows SDK to 10.0.22621.0 as defense in depth (the
rem Dockerfile's cleanup step should already make it the only installed SDK;
rem a newer SDK's rc.exe/mt.exe crash with an access violation on this
rem ltsc2022 base).
call "C:\Program Files (x86)\Microsoft Visual Studio\2022\BuildTools\Common7\Tools\VsDevCmd.bat" -arch=amd64 -winsdk=10.0.22621.0

rem VsDevCmd.bat unconditionally resets VCPKG_ROOT to VS's own bundled vcpkg
rem install, overriding the ENV set in the Dockerfile - re-export it so it
rem points back at the vcpkg this image actually built.
set "VCPKG_ROOT=C:\vcpkg"

cmd.exe
