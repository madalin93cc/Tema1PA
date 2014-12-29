build: javac-p1 javac-p2
	
javac-p1:
	javac Sauron.java
	
javac-p2:
	javac Matrix.java
	
run-p1: 
	java Sauron
	
run-p2:
	java Matrix
	
clean:
	rm *.class
	
