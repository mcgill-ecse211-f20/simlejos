# Simlejos Makefile
#
# Author: Younes Boubekeur
#
# Usage:
# `make` compiles the Java source and makes the simlejos.jar file
# `make clean` removes the previously compiled class files and the simlejos.jar
#
# This Makefile requires WEBOTS_HOME to be correctly defined.

simlejos.jar:
	javac --release 11 -cp "$(WEBOTS_HOME)/lib/controller/java/Controller.jar" simlejos/*.java simlejos/**/*.java simlejos/**/**/*.java
	jar cf simlejos.jar `#MF` simlejos/*.java simlejos/**/*.java simlejos/**/**/*.java simlejos/*.class simlejos/**/*.class simlejos/**/**/*.class

clean:
	rm -f simlejos.jar simlejos/*.class simlejos/**/*.class simlejos/**/**/*.class
