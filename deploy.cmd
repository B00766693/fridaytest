@echo off

echo ---Deploying site

copy d:\home\site\repository\FriBusApp\target\*.war %DEPLOYMENT_TARGET%\webapps\*.war
