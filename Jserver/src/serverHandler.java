

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.xml.sax.InputSource;




public class serverHandler  extends AbstractHandler{
	
	public void handle(String target,Request baseRequest,HttpServletRequest request,HttpServletResponse response)
	throws IOException, ServletException
	{
		try {		
			System.out.println(request.getRequestURL());
			if(request.getMethod()=="GET")
			serverget(baseRequest,request,response);
			
			else if(request.getMethod()=="POST")
			serverpost(baseRequest,request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String  m_body;
	private String  latlng;
	//
	
	//private String greeting = "Hello World";

	/*public HelloServlet(){}
	public HelloServlet(String greeting){
		this.greeting=greeting;
	}*/
	
	
	public void serverget(Request baseRequest,HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException, Exception{
		byte[] m_buffer = new byte[10000];
		StringBuffer xmlD = new StringBuffer();
		
        String filename ="index.html";
        String temp_filename ="index.html";
		//m_body=bodyout(request.getInputStream());
        StringBuffer RequestURL = request.getRequestURL();
        temp_filename=RequestURL.substring(RequestURL.lastIndexOf("/")+1);

        
		if(!temp_filename.equals(""))
        filename = temp_filename;
		
		FileInputStream input = new FileInputStream("C:/Users/hanseok/eclipse-workspace/Jserver/"+filename);		
	    input.read(m_buffer);
	    //System.out.println(new String(m_buffer));    
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
	    baseRequest.setHandled(true);
	    PrintWriter out = response.getWriter();
		out.println(new String(m_buffer,0,10000,"UTF-8"));
	    //out.println("<h1> dd aa </h1>");
		//System.out.println(m_body);
	 }
	
	
public void serverpost(Request baseRequest,HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException, Exception{
	byte[] m_buffer = new byte[1024];
	StringBuffer xmlD = new StringBuffer();
	Document Doc=null;
	
	
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
	
    //====================================== 
	//==  Receive Json File               ==
	//======================================
    /*
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(request.getInputStream(), "UTF-8"));
    JSONArray memberArray = (JSONArray) jsonObject.get("GPS");

    System.out.println("=====GPS=====");
    for(int i=0 ; i<memberArray.size() ; i++){
        JSONObject tempObj = (JSONObject) memberArray.get(i);
        System.out.println(""+(i+1)+"번째 latitude : "+tempObj.get("latitude"));
        System.out.println(""+(i+1)+"번째 longitude : "+tempObj.get("longitude"));
        System.out.println(""+(i+1)+"번째 time : "+tempObj.get("time"));
        System.out.println("----------------------------");
    }
   */
  
    // image process ==================================================  
    // BufferedImage img = ImageIO.read(request.getInputStream());
    //File file = new File("C://Users/hanseok/Desktop/test.jpg");
    //ImageIO.write(img, "jpg", file);
    
    
	//Doc = parseXML(request.getInputStream());
	
    
    
	//xmlD = tostringbuff(request.getInputStream());
	//System.out.println(xmlD);
	//Doc     =  builder.parse(new InputSource(new StringReader(xmlD.toString())));
	
	//Doc     =  builder.parse(request.getInputStream());
	
	/*
	FileInputStream input = new FileInputStream("c:/out.txt");
	
    input.read(m_buffer);
    System.out.println(new String(m_buffer));
    input.close();
    */
	
	//받은 데이터 문자열로 변환===============================================    
	m_body=bodyout(request.getInputStream());
	System.out.println("getinput : " + m_body+"\n");
	String decodeResult = URLDecoder.decode(m_body, "UTF-8");
	decodeResult= decodeResult.substring(3);
	System.out.println("decodeResult : " + decodeResult+"\n");
	
	serversender ser_sen;
	ser_sen = new serversender();
	ser_sen.init();			
	ser_sen.sendwaypoint(decodeResult,request.getRemoteAddr());
	ser_sen = null;
	
	return_webpage(baseRequest,response,request,decodeResult);
	

	/*
	try {
			Doc = obtenerDocumentDeByte(m_buffer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/
    
    
    /*
	Doc.getDocumentElement().normalize();
	
	
	NodeList personlist = Doc.getElementsByTagName("person");
	
	for (int i = 0; i < personlist.getLength(); i++) {
		
		System.out.println("---------- personNode "+ i + "번째 ------------------");

		Node personNode = personlist.item(i);

		if (personNode.getNodeType() == Node.ELEMENT_NODE) {
			// person엘리먼트 
			Element personElmnt = (Element) personNode;

			// name 태그
			NodeList nameList= personElmnt.getElementsByTagName("name");
			Element nameElmnt = (Element) nameList.item(0);
			Node name = nameElmnt.getFirstChild();
			System.out.println("name    : " + name.getNodeValue());

			// tel 태그
			NodeList telList= personElmnt.getElementsByTagName("tel");
			Element telElmnt = (Element) telList.item(0);
			Node tel = telElmnt.getFirstChild();
			System.out.println("tel     : " + tel.getNodeValue());

			// address 태그
			NodeList addressList= personElmnt.getElementsByTagName("address");
			Element addressElmnt = (Element) addressList.item(0);
			Node address = addressElmnt.getFirstChild();
			System.out.println("address : " + address.getNodeValue());
		}

		System.out.println("---------------------------------------------");
		System.out.println();
	}
	*/
	
    
    
	
 }
void return_webpage(Request baseRequest,HttpServletResponse response,HttpServletRequest request,String decodeResult) {
	response.setContentType("text/html;charset=utf-8");
	response.setStatus(HttpServletResponse.SC_OK);
    baseRequest.setHandled(true);
	//response.getWriter().println("선박이 해당 좌표로 이동합니다.\n"+m_body);
	
	try {
		response.getWriter().println("요청 좌표 <br>");
	 	
	System.out.println("request.getRemoteAddr() : " +request.getRemoteAddr());
		
	String  GPS;
	StringTokenizer t1 = new StringTokenizer(decodeResult, "/");
	StringTokenizer t2 ;
	for (int i=1; t1.hasMoreTokens(); i++) {
		GPS = t1.nextToken();
		System.out.println("전체"+i+ " : " + GPS);
		t2 = new StringTokenizer(GPS, ",");
		for (int j=1; t2.hasMoreTokens(); j++) {			
			response.getWriter().print(t2.nextToken()+"\t");
			response.getWriter().print(t2.nextToken());
			response.getWriter().print("\t");
		}
		response.getWriter().print("<br>");
	  }
	}
	catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}


private Document parseXML(InputStream stream) throws Exception{

    DocumentBuilderFactory objDocumentBuilderFactory = null;
    DocumentBuilder objDocumentBuilder = null;
    Document doc = null;

    try{

        objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
        
        doc = objDocumentBuilder.parse(stream);
        
    }catch(Exception ex){
        throw ex;
    }       

    return doc;
}

/*
protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException{
	response.setContentType("text/html;charset=utf-8");
	response.setStatus(HttpServletResponse.SC_OK);
	response.getWriter().println("<h1>"+greeting+"Get</h1>");
	response.getWriter().println("session="+request.getSession(true).getId());
 }*/

/*
protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException{
	InputStream test;
	String  m_body;
	response.setContentType("text/html");
	response.setStatus(HttpServletResponse.SC_OK);
	response.getWriter().println("<h1>"+greeting+request.getContentType()+
			  request.getInputStream()+"Post </h1>");
	response.getWriter().println("session="+request.getSession(true).getId());
	m_body=bodyout(request.getInputStream());
	
	System.out.println(m_body);
 }*/
//
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

byte[] tobyte(InputStream inputS){
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
	return m_buffer;
}
StringBuffer tostringbuff(InputStream inputS){
	int length;	
	StringBuffer out = new StringBuffer();
	byte[] m_buffer = new byte[1024]; 
	try {
		for (int n; (n = inputS.read(m_buffer)) != -1;)
		{
			//out.append(new String(m_buffer, 0, n)); 
			
			//한글 지원 되지만, 에러남..  후행 섹션에서는 콘텐츠가 허용되지 않습니다.
			out.append(new String(m_buffer,Charset.forName("UTF-8")));
			}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return out;
}
/*
private Document obtenerDocumentDeByte(byte[] documentoXml) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse(new ByteArrayInputStream(documentoXml));
}*/


}


