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

## Install javafx scene builder

Scene builder is used to edit the fxml files.

Download it from [https://www.oracle.com/java/technologies/javafxscenebuilder-1x-archive-downloads.html](https://www.oracle.com/java/technologies/javafxscenebuilder-1x-archive-downloads.html)

Install it on Linux with:

`sudo dpkg -i javafx_scenebuilder-XX-linux-x64.deb`