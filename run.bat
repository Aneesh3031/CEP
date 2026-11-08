@echo off
setlocal
echo ========================================================
echo   Starting CrashFinder Dynamic Spring Boot + Database
echo ========================================================

:: Check Java
if exist "C:\Program Files\Java\jdk-26.0.1\bin\java.exe" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-26.0.1"
    set "PATH=C:\Program Files\Java\jdk-26.0.1\bin;%PATH%"
) else (
    where java >nul 2>nul
    if %errorlevel% neq 0 (
        echo [ERROR] Java JDK was not detected. Please install JDK 17+.
        pause
        exit /b 1
    )
)

echo [1/3] Java detected:
java -version

echo.
echo [2/3] Launching Spring Boot Backend with H2 Persistent DB...
echo       Backend URL: http://localhost:8080
echo       H2 Console:  http://localhost:8080/h2-console
echo.

:: Launch browser in background after 3 seconds
start "" powershell -NoProfile -Command "Start-Sleep -Seconds 3; Start-Process 'http://localhost:8080'"

:: Run JAR if exists, else run via Maven
if exist "backend\target\crashfinder-backend-1.0.0.jar" (
    java -jar "backend\target\crashfinder-backend-1.0.0.jar"
) else (
    cd backend
    ..\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run "-Dmaven.repo.local=..\.m2\repository"
)

pause

