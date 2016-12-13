import java.io.*;
import java.net.*;

public final class WebClient
{
   public static void main(String argv[]) throws Exception
   {
      	int port = Integer.parseInt(argv[1]);
      	String filename=argv[2];
		InetAddress IPAddress = InetAddress.getByName(argv[0]);
		String CRLF = "\r\n";
		long start = System.currentTimeMillis();
		Socket socket=null;
		InputStream is=null;
		OutputStream os=null;
		InputStreamReader isr=null;
		BufferedReader br=null;
		OutputStreamWriter osw =null;
		BufferedWriter bw=null;
		long end=0;
		

		try{
		 	socket = new Socket(IPAddress,port);
		 	is = socket.getInputStream();
			os = socket.getOutputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader( isr );
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			socket.setSoTimeout(50000);
		
			//Writing Request Line to the Sever
			bw.write("GET /"+filename+" HTTP/1.1"+CRLF);

			//Writing Client socket parameters to the server
			bw.write("Client Host:"+socket.getInetAddress().getHostName()+CRLF);
			bw.write("Client Port:"+socket.getLocalPort()+CRLF);
			bw.write("Client Timeout:"+socket.getSoTimeout()+"\n");
			bw.write("Client Sender BufferSize:"+socket.getSendBufferSize()+CRLF);
			bw.write("Client Receiver BufferSize:"+socket.getReceiveBufferSize()+CRLF);
			bw.write(CRLF);
			bw.flush();

			String messageFromServer;
			int count=0;
			end=System.currentTimeMillis();;
			

			//Displaying the server reponse,server socket parameters and response file cotent
			while((messageFromServer = br.readLine())!=null){
				if(count==0){end = System.currentTimeMillis();count++;}
				System.out.println(messageFromServer);
			}		
			
			//Printing Rountrip time for the request
			System.out.println("Round trip time: "+(end-start)+"millis");
			bw.close();
			br.close();
			socket.close();
	
		}
		catch(ConnectException e){
			System.out.println("Host Not found!");
			if(socket!=null)socket.close();
			if(bw!=null)bw.close();
			if(br!=null)br.close();
		}
		 catch(Exception e){
		 	e.printStackTrace();
		 	if(socket!=null)socket.close();
		 	if(bw!=null)bw.close();
			if(br!=null)br.close();
		 }
		
	

	
		
		
	
	
	
   }
}