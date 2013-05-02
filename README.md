# ssh-telnet-launcher #

**THIS PROGRAM IS IN ALPHA STAGE**

**Alpha3 should be a version that is *fully* working**

*Tested on Ubuntu 12.04LTS*

*Please report any issues on github*

*This program is developed using Java 7 and currently doesn't support Java 6*

*A java 6 version will be built soon or you can build it from sources available on github*

  - The SSHTelnetLauncher is a program that reads a PuttyCM database (xml) and displays it in a tree form.
  - It was built for Ubuntu but i'm sure it will work for any other distro that has xterm.
  - It is used to launch an xterm with an ssh or telnet process that will connect to the specified PuttyCM connection.
  - No support for stored password, commands. Only supports the ip, protocol and connection name.
  - Supports only one database at a time.

## Download ##
  - Alpha4 : http://bit.ly/105sJ30

## How to install ##
  - Extract the content of the archive using 'tar -xzf ssh-telnet-launcher-xxx.tar.gz'
  - Go into the extracted directory : 'cd ssh-telnet-launcher-xxx'
  - Execute the install script : 'sudo ./install.sh --install' or 'sudo sh install.sh --install' contained in the directory
  - It will install this program in /usr/local/lib/sshtelnetlauncher/

## How to configure (MUST BE DONE) ##
  - Edit the configuration file contained in : /usr/local/lib/sshtelnetlauncher/sshtellaunch.conf
  - For database_path put the relative or absolute path to the PuttyCM database you want to use
  	- Relative paths start from /usr/local/lib/sshtelnetlauncher
  - Then for ssh_path put the ssh command patlh (usually /usr/bin/ssh). To find it use 'whereis ssh'.
  - Then for telnet_path put the telnet command path (usually /usr/bin/telnet). To find it use 'whereis telnet'.
  	- It may not be installed on your system. Just install it and it will work fine

## How to start the program ##
  - Simply execute /usr/local/lib/sshtelnetlauncher/SSHTelnetLauncher or install the Unity launcher (see below)
  - If this fails do 'chmod +x /usr/local/lib/sshtelnetlauncher/SSHTelnetLauncher'

## How to upgrade ##
*Coming soon*
  - For now :
    - Go into /usr/local/lib/sshtelnetlauncher
  	- backup your config somewhere (sshtellauncher.conf)
  	- ./install.sh --uninstall
  	- Redo the installation
  	- Copy your backed up configuration back into /usr/local/lib/sshtelnetlauncher

## How to uninstall ##
  - Go into /usr/local/lib/sshtelnetlauncher : 'cd /usr/local/lib/sshtelnetlauncher'
  - Execute 'sudo ./install.sh --uninstall'
    - This will uninstall everything (except for the xterm configuration if you installed it)
    - To delete the xterm configuration 'rm ~/.Xresources'

## Unity Launcher ##
  - A unity launcher (for Ubuntu 11+) is provided in the folder
  	- Go in the folder you installed from or in /usr/local/lib/sshtelnetlauncher
  	- Execute './install.sh --unity-launcher'
  	- Input the user for which you wish the launcher to be installed
  	  - This has to be the user's folder name under /home
  	- It will then appear in your dash

## xterm customization (highly recommended) ##
  - If your xterm is not configured to use different fonts, size, color, etc. We suggest you use our configuration file for a better experience.
  - Execute './install.sh --xterm-conf'
  - Input the user for which you wish the configuration to be installed
 	- This has to be the user's folder name under /home
  - After you can do : 'xrdb -load ~/.Xresources' or restart your x session to apply changes to xterm.
