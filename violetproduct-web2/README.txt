War overlay project. To run it, run :

(Prerequisites : mvn install from parent project to install all violets artifacts to your local repo)

mvn clean package war:war tomcat7:run-war

You can add extra args for debugging purpose :

MAVEN_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000"

Testing address is : http://localhost:8080/violetproduct-web2


