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
echo 6. Exit
echo.
set /p choice=Enter your choice (1-6): 

if "%choice%"=="1" goto list
if "%choice%"=="2" goto switch
if "%choice%"=="3" goto pull
if "%choice%"=="4" goto commitpush
if "%choice%"=="5" goto create
if "%choice%"=="6" exit
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
git branch
echo.
set /p branch=Enter branch name to switch to: 
git checkout %branch%
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
