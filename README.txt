To compile, put all files in same directory and run the following command:

javac *.java

To run the program:

java Main [-bhi] samplesize inputfile
	-b: run the -BASE version of TRIEST (default)
	-h: prints this help message and exits
	-i: run the -IMPR version of TRIEST
	
Example command:

java Main -i 50000 ca-AstroPh-clean.txt (runs TRIEST-IMPR on file soc.txt with sample size 50,000)
