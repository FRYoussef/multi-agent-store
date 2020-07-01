#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Please, specify the javafx path as argument."
    exit 1
fi

java \
--module-path $1/lib \
--add-modules javafx.controls,javafx.fxml \
-cp "lib/jade.jar:build" Main