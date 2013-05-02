#!/bin/bash

for arg in $@
do
	if [ $arg = "--unity-launcher" ]; then
		echo "Unity launcher installation"
		echo "Please type the user for which you want to install the unity launcher (the name of your home folder under /home) :"
		read user
		cp addons/SSHTelnetLauncher.desktop /home/$user/.local/share/applications/SSHTelnetLauncher.desktop
	elif [ $arg = "--xterm-conf" ]; then
		echo "xterm configuration installation"
		echo "Please type the user for which you want to install the xterm configuration :"
		read user
		cp addons/.Xresources /home/$user/.Xresources
	elif [ $arg = "--install" ]; then
		echo "Installing software"
		cp -R ./ /usr/local/lib/sshtelnetlauncher
		chmod a+rwx /usr/local/lib/sshtelnetlauncher/sshtellaunch.conf
	elif [ $arg = "--uninstall" ];then
		echo "Uninstalling software"
		rm -rf /usr/local/lib/sshtelnetlauncher
		echo "Removing Unity launcher for all users except root (this could fail if you didn't install it...)"
		for dir in `find /home -maxdepth 1 -type d`
		do
			if [ $dir != "/home" ]; then
				echo "Removing for user : $dir"
		    	rm $dir/.local/share/applications/SSHTelnetLauncher.desktop
		    fi;
		done
	fi
done


