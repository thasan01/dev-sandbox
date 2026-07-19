@echo off
:: Wrapper for the Visual Studio Build Tools installer.
:: Exit code 3010 means "success, but a reboot is required" - inside a
:: container there's nothing to reboot, so we treat it as success (0).
call %*
if "%ERRORLEVEL%"=="3010" (
    exit /b 0
) else (
    exit /b %ERRORLEVEL%
)
