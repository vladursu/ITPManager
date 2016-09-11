@ECHO OFF
javac -d . -cp lib/joda-time-2.9.1.jar;lib/javax.mail.jar;lib/postgresql-9.4.1209.jar @sources.txt
java -cp .;lib/joda-time-2.9.1.jar;lib/javax.mail.jar;lib/postgresql-9.4.1209.jar ITPManagerGUI
del /f *.class
rmdir model /s /q
rmdir services /s /q
::PAUSE