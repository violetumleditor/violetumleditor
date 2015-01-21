How to compile Violet's source code
===================================

Violet is developped in Java and is packaged with Maven.
So, prerequisites are :
+ Java Development Kit 6 or greater (http://www.oracle.com/technetwork/java/javase/downloads/index.html)
+ Maven 2 or greater (http://maven.apache.org/)
 
Once you grabbed the source code (git pull), just run 'mvn clean package' from the root directory. This command will compile and package everything. 

As Violet project is composed of several sub-projects (plugins, products, etc...), it is organized as a main maven project (parent) with modules (children). Once everything is compiled and package, go to [module directory]/target to get the result of this packaging. For example : violetproduct-exe/target/violetumleditor-xxx.exe
 
If you want to go further and contribute, please read the developed guide from the website here : http://alexdp.free.fr/violetumleditor/page.php?id=en:developerguide

Kind Regards,
Alex
