import java.io.*;
import java.net.*;
import java.util.*;

/*
@class:WebServer

A class to accept client sockets infinitely(unless terminated by user) and pass each incoming socket into threads for processing

*/
public final class WebServer
{
   public static void main(String argv[]) throws Exception
   {
   	int port =Integer.parseInt(argv[0]);
   	ServerSocket server = new ServerSocket(port);
   	Socket sock = null; 
   	while(true){
   		sock=server.accept();
         String clienthostname=sock.getInetAddress().toString();
         String clientport=""+sock.getPort();
         int client_timeout=sock.getSoTimeout();
   		HttpRequest request = new HttpRequest(sock,server);
   		Thread thread = new Thread(request);
		   thread.start();
   	} 
     
   }

}


/*
@class:HttpRequest

A class to process multithreded socket Http Requests requests from clients

*/
final class HttpRequest implements Runnable
{
  final static String CRLF = "\r\n";
   Socket socket;
   ServerSocket server;

   
   public HttpRequest(Socket socket,ServerSocket server) throws Exception 
   {
      this.socket = socket;
      this.server=server;
   }

   
   public void run()
   {
     try {
      processRequest();
   }
   catch (Exception e) {
      System.out.println(e);
   }
   }
   
   /*
   @method:processRequest

   processes the Http request messages and responds to the client with the Http Response.


   */
   private void processRequest() throws Exception
   {
      InputStream is = socket.getInputStream() ;
      OutputStream os = socket.getOutputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      String requestLine = br.readLine();
      System.out.println();
      System.out.println(requestLine);
      String headerLine = null ;
      
      while ((headerLine = br.readLine()).length() != 0) {
         System.out.println (headerLine);

      }
      
      StringTokenizer tokens = new StringTokenizer( requestLine ) ;
      tokens.nextToken( ) ; 
      String fileName = tokens.nextToken( ) ;
      fileName = "." + fileName ;
      FileInputStream fis = null ;
      boolean fileExists = true ;
       try 
      {
         fis = new FileInputStream( fileName ) ;
      } 
      catch ( FileNotFoundException e ) 
      {
       fileExists = false ;
      }
      String statusLine = null ;
      String contentTypeLine = null ;
      String entityBody = null ;
      if ( fileExists )
      {
         statusLine = "HTTP/1.1 200 OK"+CRLF ;
         contentTypeLine = "Content-type: " + 
         contentType( fileName ) + CRLF ;
      }
      else
      {
         statusLine = "HTTP/1.1 404 Not Found"+CRLF ;
         contentTypeLine = "Content-type:text/html"+CRLF ;
         entityBody = "<HTML>" + 
         "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
         "<BODY>Not Found</BODY></HTML>" ;
      }
     os.write( statusLine.getBytes( ) ) ;

  
      os.write( contentTypeLine.getBytes()) ;
      String serverhost="Server Host:"+server.toString()+CRLF;
      String serverport="Server Port:"+server.getLocalPort()+CRLF;
      String servertimeout="Server Timeout: "+server.getSoTimeout()+CRLF;
      String rcvbuffer="Server receive buffer size: "+server.getReceiveBufferSize()+CRLF;

      os.write(serverhost.getBytes());
      os.write(serverport.getBytes());
      os.write(servertimeout.getBytes());
      os.write(rcvbuffer.getBytes());
      

      os.write( CRLF.getBytes( ) ) ;
  
      if ( fileExists )
      {
         sendBytes( fis, os ) ;
         fis.close( );
      }
      else
      {
         os.write(entityBody.getBytes()) ;
      }
      

   os.close();
   br.close();
   socket.close();
   }
   
   /*
   @method: sendBytes(FileInputStream,OutputStream)

   used the write bytes from a file input to the output stream of the client.

   */
   private static void sendBytes( FileInputStream fis, OutputStream os ) throws Exception
   {
    
      byte[] buffer = new byte[1024] ;
      int bytes = 0 ;
      while ( ( bytes = fis.read( buffer ) ) != -1 ) 
      {
         os.write( buffer, 0, bytes );
      }
   }
    
   
   /*
   @method:contentType(String)

    returns the the MIME format of Content type for the given file based on the file format.


   */
    private static String contentType( String fileName )
   {
      if ( fileName.endsWith(".htm") || fileName.endsWith(".html") )
      {
         return "text/html" ;
      }
      if ( fileName.endsWith(".txt"))
      {
         return "text/plain" ;
      }
      if ( fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))
      {
         return "image/jpeg" ;
      }
    
    
      return "" ;
   }

}