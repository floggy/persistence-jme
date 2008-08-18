#/bin/sh

rm -rf eclipse-*

unzip -qq ~/download/eclipse-SDK-3.3.1.1-win32.zip
mv eclipse eclipse-SDK-3.3.1.1-win32

unzip -qq ~/download/eclipse-java-europa-winter-win32.zip
mv eclipse eclipse-java-europa-winter-win32

unzip -qq ~/download/eclipse-SDK-3.2.2-win32.zip
mv eclipse eclipse-SDK-3.2.2-win32

tar -xzf ~/download/eclipse-jee-ganymede-linux-gtk.tar.gz
mv eclipse eclipse-jee-ganymede-linux-gtk

tar -xzf ~/download/eclipse-java-ganymede-linux-gtk.tar.gz
mv eclipse eclipse-java-ganymede-linux-gtk

tar -xzf ~/download/eclipse-SDK-3.2.2-linux-gtk.tar.gz
mv eclipse eclipse-SDK-3.2.2-linux-gtk
