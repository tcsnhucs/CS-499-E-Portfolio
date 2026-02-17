#!/usr/bin/env bash
# Launcher for CS-320 Journal
DIR="$(cd "$(dirname "$0")" && pwd)"
java --module-path "$DIR/../lib" --add-modules javafx.controls,javafx.fxml -cp "$DIR/cs-320-journal.jar:$DIR/../lib/*" ui.MainApp "$@"
