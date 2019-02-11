@echo off

SET startdir=%~dp0

call mvn -f %startdir%\common\pom.xml clean install
call mvn -f %startdir%\customerservice\pom.xml clean install
call mvn -f %startdir%\orderservice\pom.xml clean install
call mvn -f %startdir%\productservice\pom.xml clean install