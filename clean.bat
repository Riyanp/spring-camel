@echo off

set startdir=%~dp0

RMDIR /S /Q %startdir%common\target
RMDIR /S /Q %startdir%customerservice\target
RMDIR /S /Q %startdir%orderservice\target
RMDIR /S /Q %startdir%productservice\target

PAUSE