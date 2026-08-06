#!/bin/bash

JAVAFX_PATH="/usr/share/openjfx/lib"

echo "Compiling..."
javac --module-path $JAVAFX_PATH --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web Main.java

echo "Lauching..."
java --module-path $JAVAFX_PATH  --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web Main

echo "Cleaning up ..."
find . -name "*.class" -type f -delete

echo "Done"