import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import java.util.concurrent.TimeoutException;

 

import org.eclipse.jetty.client.HttpClient;

import org.eclipse.jetty.client.api.ContentResponse;

import org.eclipse.jetty.client.api.Request;


// ContentProvider
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.eclipse.jetty.client.util.PathContentProvider;
import org.eclipse.jetty.client.util.StringContentProvider;


import com.sun.corba.se.impl.ior.ByteBuffer;


public class Myclient {

    String UI_GPS = "";
	private HttpClient m_http_client;
	
	public int init() {
		m_http_client = new HttpClient();
		try {
			m_http_client.start();
		}catch(Exception e) {
			e.printStackTrace();
		}
	return 0;	
	}
	public void sendreq(String json_path,String method) {
		try {	
			ContentResponse content_reponse;
			Request request ;
			byte[] body;
			char[] buf = new char[1024];
			int numRead=0;
			//String Sbody = "dfafafdaddsddd";
			//body = Sbody.getBytes();
			String url="http://61.82.252.135:443/";
			request = m_http_client.newRequest(url);
			if(request == null) {
				System.out.println("null");
			}
			else
				System.out.println("creted...");
			request.method(method);
			//bodyout_b(reader2);
			
			
			//char to byte....
			//body = Charset.forName("utf-8").encode(CharBuffer.wrap(test)).array();
			
            
            //아두이노 값을 위한 stirng 
            
            
			 
            
            
            
            //====================================== 
			//==  Send jpg                        ==
			//======================================
			//String jpgpath = "C://Users/hanseok/Desktop/cute_owl-wallpaper-1680x1050.jpg";
			//Path path = Paths.get(jpgpath);
		
            
            
			//====================================== 
			//==  Send String                     ==
			//======================================		
			//request.content(new StringContentProvider(test),"application/xml");
      
            //====================================== 
    	    //==  Send Byte                       ==
    		//======================================
            String test = "shipID";
            body = test.getBytes(Charset.forName("utf-8"));
    	    request.content(new BytesContentProvider(body),"application/html");
    			
    	    if(json_path.contains("json")) {
    			//====================================== 
    			//==  Send Json File                  ==
    			//======================================           
    		    Path path = Paths.get(json_path);
    		    request.content(new PathContentProvider("json",path),"application/json");           
                }
			
			//====================================== 
			//==  Send Request                    ==
			//======================================
			content_reponse = request.send();
			
			if(content_reponse == null) {
				System.out.print("res null.");
				return;
			}
			String command = content_reponse.getContentAsString();
			 
			System.out.println("서버문자:"+command);
			if(command.contains("$"))    //  서버의 UI GPS 넘김  
				UI_GPS = command;
			else if(command.contains("em")) {
				command = command.substring(2);
				serial_socket(command,4444);
			}
			else if(!command.equals("")){// waypoint 제어 모듈로 넘김
			serial_socket(command,1234);
			}
			
			m_http_client.stop();
			m_http_client.destroy();
			m_http_client = null;
		} catch (InterruptedException | TimeoutException | ExecutionException | IOException   e) {//| UnsupportedEncodingException
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("exxception..");
			//serial_socket("1t",1234);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("http client stop error .. ");
			//serial_socket("1t",1234);
		}
	}

	void serial_socket(String command,int port_number){

    	byte[] m_buffer = new byte[1024];

		String Ip = "127.0.0.1"; 

		try {

		Socket s = new Socket(Ip,port_number);

	    OutputStream sender = s.getOutputStream();			

	        System.out.println("클라이언트 실행..");
	    	byte[] data = new byte[100];
	    	data = command.getBytes();
				sender.write(data,0,data.length);

			m_buffer = null;	

			System.out.println("socket done..");

			s.close();

			sender.close();

			} catch (IOException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

    }
	
/*
	byte[] bodyout_b(bufferStream inputS){
		int length;	
		String m_body;
		byte[] m_buffer = new byte[1024];
		m_body = new String();
		while(true){
			try {
				length = inputS.read(m_buffer,0,1024);
				if(length<=0) {
					break;
				}
				m_body = m_body+ new String(m_buffer,0,length,"UTF-8");
				if(length < 1024) {
					break;
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		try {
			inputS.close();
		}catch(IOException e) {

			e.printStackTrace();
		}
		return m_body;
	}
	*/
	 
	
	
	String bodyout(InputStream inputS){
		int length;	
		String m_body;
		byte[] m_buffer = new byte[1024];
		m_body = new String();
		while(true){
			try {
				length = inputS.read(m_buffer,0,1024);
				if(length<=0) {
					break;
				}
				m_body = m_body+ new String(m_buffer,0,length,"UTF-8");
				if(length < 1024) {
					break;
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		try {
			inputS.close();
		}catch(IOException e) {

			e.printStackTrace();
		}
		return m_body;
	}
	
}