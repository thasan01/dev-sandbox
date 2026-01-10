@echo off

REM Check if the first argument is -clean
if "%1"=="-clean" (
    del /s /q build\*
)

cmake -S src -B build --preset debug
cmake --build build --config Debug
