JC=javac
.SUFFIXES:.java .class
.java.class:
	$(JC) $*.java
CLASSES= \
	RBTree.java \
	bbst.java \
	RBNode.java
default:classes
classes:$(CLASSES:.java=.class)
clean:
	rm -f *.class