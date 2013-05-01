# ssh-telnet-launcher #

**THIS PROGRAM IS IN ALPHA STAGE**

*This program is developed using Java 7*

  - The SSHTelnetLauncher is a program that reads a PuttyCM database (xml) and displays it in a tree form.
  - It was built for Ubuntu but i'm sure it will work for any other distro that has xterm.
  - It is used to launch an xterm with an ssh or telnet process that will connect to the specified PuttyCM connection.
  - No support for stored password, commands. Only supports the ip, protocol and connection name.
  - Supports only one database at a time.

## Download ##
  - Alpha 2 : https://www.dropbox.com/s/0nku1d008us6f3z/ssh-telnet-launcher-alpha2.tar.gz

## How to install ##
  - Extract the content of the archive using 'tar -xzf ssh-telnet-launcher-xxx.tar.gz'
  - Go into the extracted directory : 'cd ssh-telnet-launcher-xxx'l
  - Execute the install script : 'sudo ./install.sh --install' or 'sudo sh install.sh --install' contained in the directory
  - It will install this program in /usr/local/lib/sshtelnetlauncher/

## How to upgrade ##
*Coming soon*

## How to configure (MUST BE DONE) ##
  - Then edit the configuration file contained in : /usr/local/lib/sshtelnetlauncher/sshtellaunch.conf
  - For database_path put the relative or absolute path to the PuttyCM database you want to use
  	- Relative paths start from /usr/local/lib/sshtelnetlauncher
  - Then for ssh_path put the ssh command patlh (usually /usr/bin/ssh). To find it use 'whereis ssh'.
  - Then for telnet_path put the telnet command path (usually /usr/bin/telnet). To find it use 'whereis telnet'.
  	- It may not be installed on your system. Just install it and it will work fine

## How to start ##
  - Simply execute /usr/local/lib/sshtelnetlauncher/sshtelnetlauncher or use the unity launcher (see below)
  - If this fails do 'chmod +x /usr/local/lib/sshtelnetlauncher/sshtelnetlauncher'

## Unity Launcher ##
  - A unity launcher (for Ubuntu 11+) is provided in the folder
  	- Execute './install.sh --unity-launcher'
  	- It will then appear in your dash

## xterm customization (highly recommended) ##
  - If your xterm is not configured to use different fonts, size, color, etc. We suggest you use our configuration file for a better experience.
  - Execute './install.sh --xterm-conf'
  - After you can do : 'xrdb -load ~/.Xresources' or restart your x session to apply changes to xterm.

## Known issues ##
  - When no db is found the program just doesn't start
  - When no config file is found the program just doesn't start
