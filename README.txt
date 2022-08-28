The Typing Tutor App

To run the app you must be inside the MBLSHA003_CSC2002SPCP2 folder, where you find the Makefile. From
this folder open the terminal and run make clean first so that the program
can be compiled by your java version during compilation.

On the terminal type: make run-Game 
This run the code with the default dictionary and values

To run it with different list of words you just need to specify that file name with its extension on the command
line and the number of words you want to fall in total and the number of words for each instance, here is an example below:

make run-Game ARGS="<total words> <words falling at any point> <the file
with the list of words>"

E.g. make run-Game ARGS="23 4 words.txt"

For the player to win the game, he/she has to type all the number of words
given as the first parameter in the TypingTutorApp game. The second paramter
are the number of words to fall at a given point and the last parameter is
for the file containing the words.
