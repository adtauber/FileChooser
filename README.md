# FileChooser
another android file/folder selection library

Wanted a file selection library that provided both file size and creation/update information, handled both local and FTP files, and was easy to use.  This version provides full folder tree navigation to both internal and eternal folders using file system authorizations for MTKutility and Apache Commonfs FTP for FTP download.  Found a way to implement both short and long clicks in the arrayadapter.  The library is entirely standalone and the app Mainactivity fully documents the varioys ways to implement the library.

The only time changes are need is when the main program also needs to implement a <provider block for file handling.  If it does, comment out the <provider block in the library AndroidManifest.
