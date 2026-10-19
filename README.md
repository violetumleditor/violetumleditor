How to get the software
=======================

Here, you will only find source code. If you just want to get and use the software, go to sourceforge, download it and enjoy easy diagram's drawing! https://sourceforge.net/projects/violet/


How to compile Violet's source code
===================================

Violet is developped in Java and is packaged with Maven.
So, prerequisites are :
+ Java Development Kit 17 or greater (http://www.oracle.com/technetwork/java/javase/downloads/index.html)
+ Maven 3 or greater (http://maven.apache.org/)
 
Once you grabbed the source code (git pull), just run 'mvn clean package' from the root directory. This command will compile and package everything. 

As Violet project is composed of several sub-projects (plugins, products, etc...), it is organized as a main maven project (parent) with modules (children). Once everything is compiled and package, go to [module directory]/target to get the result of this packaging. For example : violetproduct-exe/target/violetumleditor-xxx.exe
 

How to test Violet in a web browser (CheerpJ mode) without packaging
===================================================================

The online version of Violet runs the Swing application in the browser thanks to CheerpJ (see violetproduct-swing/src/main/resources/web/index.html, which runs the packaged jar). While developing, you can run your local, unpackaged code the same way, directly from your file system, without any web server :

1. From the root directory, run 'mvn compile -Pcheerpj-test'. Besides compiling, the 'cheerpj-test' profile generates violetproduct-swing/target/cheerpj-test/bundle.js, which contains the compiled classes of every module and all the third-party jars.
2. Open violetproduct-swing/src/main/resources/web/test.html in your browser (double-click it, or use a file:// URL such as file:///C:/path/to/violetumleditor/violetproduct-swing/src/main/resources/web/test.html). An internet connection is required to download the CheerpJ runtime.
3. After each change in Java code, run step 1 again, then reload the page.

Notes :
+ Always run the command from the root directory. From violetproduct-swing alone, Maven would use previously installed framework and plugin jars instead of your current code.
+ Compiling from your IDE is not enough : the bundle is only refreshed by the Maven command.
+ test.html is a copy of index.html that only differs by the way it starts the application. When you change the JavaScript bridge (native functions) in index.html, report the change in test.html.
+ Browsers block network requests from pages opened with file://. This is why the classes are embedded in bundle.js and loaded through a script tag instead of being downloaded by CheerpJ.
 

How to contribute
=================

If you want to go further and contribute (even for small fixes), please read the developed guide from the website here : http://alexdp.free.fr/violetumleditor/page.php?id=en:developerguide

Once you think you have something great, you can create a git pull request. I will examine your request and contact you back.



Kind Regards,
Alex
