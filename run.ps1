# CrashFinder Spring Boot Runner PowerShell Script
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "  Starting CrashFinder Spring Boot Backend + Database   " -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan

# Set Java Home if JDK 26 or installed JDK is present
if (Test-Path "C:\Program Files\Java\jdk-26.0.1") {
    $env:JAVA_HOME = "C:\Program Files\Java\jdk-26.0.1"
    $env:PATH = "C:\Program Files\Java\jdk-26.0.1\bin;$env:PATH"
}

Write-Host "[1/3] Java Environment:" -ForegroundColor Green
& java -version

Write-Host "`n[2/3] Starting Spring Boot Application..." -ForegroundColor Green
Write-Host "      Dashboard:   http://localhost:8080" -ForegroundColor Yellow
Write-Host "      H2 Console:  http://localhost:8080/h2-console" -ForegroundColor Yellow
Write-Host "      REST APIs:   http://localhost:8080/api/health" -ForegroundColor Yellow

# Launch browser after delay
Start-Job -ScriptBlock {
    Start-Sleep -Seconds 3
    Start-Process "http://localhost:8080"
} | Out-Null

$jarPath = "backend\target\crashfinder-backend-1.0.0.jar"
if (Test-Path $jarPath) {
    & java -jar $jarPath
} else {
    Set-Location backend
    & "..\apache-maven-3.9.6\bin\mvn.cmd" spring-boot:run "-Dmaven.repo.local=..\.m2\repository"
}

