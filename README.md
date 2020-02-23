TDA220 Assisted Living

To get it running:

1. make a user library named JavaFX11 and add all .jar files in the javafx-sdk... folder
2. make a user library named EclipsePaho and add the .jar file mqtt-client-0.4.0
3. in run configurations set VM arguments to "--module-path "**YOUR PATH**\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml"
4. install the mosquitto mqtt broker (install file included)
5. run the mosquitto broker (mosquitto.exe)

Notes: there may be some issues with the .classpath having absolute references to locations on my system we may have to add it the gitignore.
