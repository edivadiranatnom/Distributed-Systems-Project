#!/bin/sh
clear
echo "---------Starting Script---------"
echo
echo "$JAVA_HOME"
javac *.java UnoGame/*.java
osascript -e "tell application "Terminal" do script "java ServerForStart 2""
osascript -e "java"
osascript -e "java"
echo
echo "---------Finished Script---------"