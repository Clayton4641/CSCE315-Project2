CSCE315-project 2

Shortest Chain may hang when first ran, this is because it needs to make a copy of some data from the server. Once this is done it will act normally.

compile with:
javac businessBasicSearchGUITest.java

run with:
windows:
java -classpath .;lib/postgresql-42.2.8.jar businessBasicSearchGUITest

other:
java -classpath .:lib/postgresql-42.2.8.jar businessBasicSearchGUITest
