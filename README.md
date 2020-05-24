# Multi Agent Store (MAS)
FIPA multi agent store simulation


## Install Java on Linux

First of all, you have to download from [https://adoptopenjdk.net/?variant=openjdk14&jvmVariant=hotspot](https://adoptopenjdk.net/?variant=openjdk14&jvmVariant=hotspot) the OpenJDK 14.
Once you have downloaded it, you have to extract it with:

`tar -xf OpenJDK14U-jdk_x64_linux_hotspot_14_XX.tar.gz`

Now, you can add a temporal env var:

`export PATH=$PWD/jdk-14+XX/bin:$PATH`

Test that it works correctly:

`java -version`

If you don't want to export that env var each time you turn your computer on, then keep reading.

* Move to home dir:

`cd`

* Edit .bashrc:

`nano .bashrc`

* Add java to the path:

`export PATH=/opt/jdk-14+XX/bin:$PATH`

* Relaunch the .bashrc script:

`source .bashrc`


## Install and use javafx

Follow that guide [https://openjfx.io/openjfx-docs/#install-javafx](https://openjfx.io/openjfx-docs/#install-javafx)

**Warning**: If you now run the project it will compile but you will get this error:

`Error: JavaFX runtime components are missing, and are required to run this application`

To solve this issue add the following VM arguments in your IDE (in the above link is described more detailed):

`--module-path /path/to/javafx-sdk-14/lib --add-modules javafx.controls,javafx.fxml`

## Install javafx scene builder

Scene builder is used to edit the fxml files, but it isn't mandatory for running the code, **it's just for edit the views**.

Download it from [https://www.oracle.com/java/technologies/javafxscenebuilder-1x-archive-downloads.html](https://www.oracle.com/java/technologies/javafxscenebuilder-1x-archive-downloads.html)

Install it on Linux with:

`sudo dpkg -i javafx_scenebuilder-XX-linux-x64.deb`

## Install Python dependencies

Be sure you have 3.6 Python version or later.

`pip3 install -r requirements.txt`


## Run the chatbot script

First of all, move your 'credentials.json' file to the same directory as 'chatbot.py'.

Then, run the script using Python 3:

`python3 chatbot.py`
