@echo off
REM Launcher for CS-320 Journal
SET APPDIR=%~dp0
java --module-path "%APPDIR%..\lib" --add-modules javafx.controls,javafx.fxml -cp "%APPDIR%cs-320-journal.jar;%APPDIR%..\lib\*" ui.MainApp %*
