TARGET=Hello

all: ${TARGET}.class

%.class : %.java
	javac $<

run : ${TARGET}.class
	java ${TARGET}
