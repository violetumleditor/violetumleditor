How to get the software
=======================

Here, you will only find source code. If you just want to get and use the software, go to sourceforge, download it and enjoy easy diagram's drawing! https://sourceforge.net/projects/violet/


How to compile Violet's source code
===================================

Violet is developped in Java and is packaged with Maven.
So, prerequisites are :
+ Java Development Kit 8 or greater (http://www.oracle.com/technetwork/java/javase/downloads/index.html)
+ Maven 3 or greater (http://maven.apache.org/)
 
Once you grabbed the source code (git pull), just run 'mvn clean package' from the root directory. This command will compile and package everything. 

As Violet project is composed of several sub-projects (plugins, products, etc...), it is organized as a main maven project (parent) with modules (children). Once everything is compiled and package, go to [module directory]/target to get the result of this packaging. For example : violetproduct-exe/target/violetumleditor-xxx.exe
 

How to contribute
=================

If you want to go further and contribute (even for small fixes), please read the developed guide from the website here : http://alexdp.free.fr/violetumleditor/page.php?id=en:developerguide

Once you think you have something great, you can create a git pull request. I will examine your request and contact you back.


Try beta version
=================
https://drive.google.com/folderview?id=0B8Mrn_bc5gt2dGl6OGtydXhOX1k&usp=sharing#list


Kind Regards,
Alex
