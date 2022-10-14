call mvn clean install
xcopy "TestModule\target\TestModule.war" "%PET_PROJECT_TOMCAT%\webapps\" /Y
cd %PET_PROJECT_TOMCAT%
call %PET_PROJECT_TOMCAT%\bin\catalina.bat stop
call %PET_PROJECT_TOMCAT%\bin\catalina.bat jpda start  