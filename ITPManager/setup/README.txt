This is a somewhat detailed guide on how to set up this whole project for personal use or your own small business.
In accordance with the GNU General Public License, this piece of software can be freely copied, modified and redistributed as long as it is done under the same licensing. For short, you can do anything you want with it as long as you do not charge money for it.

Now with the legal stuff out of the way, there are a couple of steps to take here, and I'll try to be as detailed as possible, but this also depends on your willingness to google stuff if you get stuck.

1. Setting up the database
If you're using either your personal computer or a remote server, you'll need to install PostgreSQL on it for this to actually work.
Depending on the Operating System of the machine running the server, you'll have to download and install the corresponding version of PostgreSQL from their website (it's completely free): https://www.postgresql.org/download/

After all that is done, we'll need a psql console to continue. On Linux, you can connect to the database from the terminal and we'll take it from there. On Windows, I'd recommend using pgAdmin. In pgAdmin, under plugins you'll find the PSQL console.

Now open ITPM_db_setup.sql in a text editor and edit according to your preferences the name and password for the role(s) that will use the database.
When that is done, go back to the psql console and type in \i <absolute path>/ITPM_db_setup.sql
The absolute path is the full path to the file ITPM_db_setup.sql. Make sure you get the slashes/back slashes right (try both if it doesn't work).
That will import all the commands and automatically create the role(s) and database.

2. Editing configurations
The database is created, but now we need to edit he config file so the applications know where to connect to the database.
Open config.properties in a text editor (can be found in the lib folder).
Type in the desired language.
For PSQLJDBC.url=jdbc:postgresql://localhost/itpmanager replace localhost with the server's IP address if the server is on another machine.
While we're here, we should change the email configuration as well. Note that this is set up to work with Gmail. Fill in the username and password with the relevant information.
You'll also have to go in your Gmail settings and allow 3rd parties to access your Gmail. This is so that the application can use the email account to send email notifications. If you can't find it in settings, you'll get an email about it when you run the application and try to send a notification.

3. Editing the notification email
This is a bit more tricky. You can either use the default one included in the project or you can make a custom one.
To customise the email message, open customer_message.html in a text editor and go nuts. Note that the message is written in HTML, so make sure you're familiar with that before editing.
Also note that if you add/delete the placeholders "_____" the code might not work properly. To fully customise it, you'll have to edit EmailService.java. Just use my example if you get stuck.

4. Run it
That's it! All that's left is to run the application. You can do that by either opening launcher.bat (for Windows) or compiling and executing the code yourself.

If you have any problems or want to discuss anything related to the application, feel free to email me.