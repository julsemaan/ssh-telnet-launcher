#!/bin/bash

for arg in $@
do
	if [ $arg = "--unity-launcher" ]; then
		echo "Please type the user for which you want to install the unity launcher :"
		read user
		cp addons/SSHTelnetLauncher.desktop /home/$user/.local/share/applications/SSHTelnetLauncher.desktop
	elif [ $arg = "--xterm-conf" ]; then
		echo "Please type the user for which you want to install the xterm configuration :"
		read user
		cp addons/.Xresources /home/$user/.Xresources
	elif [ $arg = "--install" ]; then
		echo "Installing software"
		cp . /usr/local/sshtelnetlauncher
	fi
done


