Name: Nirmal Kumar Ravichandran
						
				
INSTRUCTIONS TO RUN THE PROJECT

* The following steps will give the commands to be executed in command prompt(windows) or terminal(omega) in order to run the project. 
				
*Compiling the java files:
	1. javac WebServer.java
	2. javac WebClient.java

*Open two windows of the terminal or command prompt 

*Running the Server:
	In one of the windows use the following command to run the server
		java WebServer <port_number> (For example, java WebServer 5411)

*Running the Client:
	In the other window use the following command to run the client 
		java WebClient <host_name> <port_number> <request_file>	 (For example, java WebClient localhost 5411 index.html)
(You can use sample request files 1.txt and index.html that are attached allong with the project)

* The server will be listening in a particular port specified while running the program waiting for the clients to connect.
				
* The client will connect with the server and request the server by sending a GET request message and client socket parameters. These parameters are displayed 
in the server.

* Then the server will respond with the response, contents of the file and the server socket parameters which will be displayed in the client side.
