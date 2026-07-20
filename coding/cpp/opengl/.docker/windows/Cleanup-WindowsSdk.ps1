$ErrorActionPreference = 'Stop'

$kitsRoot = 'C:\Program Files (x86)\Windows Kits\10'
$keep = '10.0.22621.0'

Get-ChildItem "$kitsRoot\Include" -Directory -ErrorAction SilentlyContinue |
    Where-Object { $_.Name -ne $keep } |
    ForEach-Object {
        $ver = $_.Name
        Write-Host "Removing extra Windows SDK $ver"
        foreach ($sub in 'bin', 'Include', 'Lib', 'Source') {
            $p = Join-Path $kitsRoot "$sub\$ver"
            if (Test-Path $p) { Remove-Item $p -Recurse -Force }
        }
        Remove-Item "HKLM:\SOFTWARE\Microsoft\Windows Kits\Installed Roots\$ver" -Recurse -Force -ErrorAction SilentlyContinue
    }

$keepShort = $keep -replace '\.0$', ''
$sdkKey = 'HKLM:\SOFTWARE\WOW6432Node\Microsoft\Microsoft SDKs\Windows\v10.0'
Set-ItemProperty -Path $sdkKey -Name ProductVersion -Value $keepShort
Set-ItemProperty -Path $sdkKey -Name ProductName -Value "Microsoft Windows SDK for Windows $keepShort"
