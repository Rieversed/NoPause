@echo off
setlocal enabledelayedexpansion

:menu
cls
echo ========================================
echo        GIT HELPER SCRIPT
echo ========================================
echo.
echo Current branch:
git branch --show-current
echo.
echo What do you want to do?
echo.
echo 1. List branches
echo 2. Switch branch
echo 3. Pull latest changes
echo 4. Add, commit, and push
echo 5. Create new branch
echo 6. Release new version (create tag)
echo 7. Exit
echo.
set /p choice=Enter your choice (1-7): 

if "%choice%"=="1" goto list
if "%choice%"=="2" goto switch
if "%choice%"=="3" goto pull
if "%choice%"=="4" goto commitpush
if "%choice%"=="5" goto create
if "%choice%"=="6" goto release
if "%choice%"=="7" exit
goto menu

:list
cls
echo ========================================
echo           All Branches
echo ========================================
git branch -a
pause
goto menu

:switch
cls
echo ========================================
echo           Switch Branch
echo ========================================
echo Listing local branches:
echo.

:: Store branches in numbered list
set i=0
for /f "tokens=*" %%b in ('git branch --format="%%(refname:short)"') do (
    set /a i=!i! + 1
    set "branch[!i!]=%%b"
    echo !i!. %%b
)
echo.
set /p bchoice=Enter the number of the branch to switch to: 

set "selectedBranch=!branch[%bchoice%]!"
if not defined selectedBranch (
    echo Invalid selection.
    pause
    goto menu
)

git checkout "!selectedBranch!"
pause
goto menu

:pull
cls
echo ========================================
echo           Pull Latest Changes
echo ========================================
git pull
pause
goto menu

:commitpush
cls
echo ========================================
echo       Add, Commit, and Push
echo ========================================
set /p msg=Enter commit message: 
git add .
git commit -m "%msg%"
git push
pause
goto menu

:create
cls
echo ========================================
echo         Create New Branch
echo ========================================
set /p newbranch=Enter new branch name: 
git checkout -b %newbranch%
git push -u origin %newbranch%
pause
goto menu

:release
cls
echo ========================================
echo        Release New Version
echo ========================================
set /p version=Enter version tag (e.g., v4.2.3): 
git tag %version%
git push origin %version%
echo Tag %version% created and pushed.
echo.
echo Your GitHub Actions workflow will now build and upload to Modrinth automatically.
pause
goto menu
