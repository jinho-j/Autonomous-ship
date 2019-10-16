import java.nio.charset.Charset;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class serversender {
	
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
	
	public void sendwaypoint(String waypoints,String from) {
		try {			 					
			ContentResponse content_reponse;
			Request request_client;
			byte[] body;
			char[] buf = new char[1024];
			int numRead=0;

			String url="http://192.168.0.16:1122/";//localhost
			
			request_client = m_http_client.newRequest(url);
			
			if(request_client == null) {
				System.out.println("null");
			}
			else
				System.out.println("creted...");
			request_client.method("POST ");
			
			JSONObject jsonObject = new JSONObject();
	        JSONArray desGPSArray = new JSONArray();
	        JSONObject desGPSInfo;
	        //Object testt = new JSONObject();
	        String GPS;
	        StringTokenizer t1 = new StringTokenizer(waypoints, "/");
	    	StringTokenizer t2 ;
	        for (int i=1; t1.hasMoreTokens(); i++) {
	    		GPS = t1.nextToken();
	    		System.out.println("ÀüÃ¼"+i+ " : " + GPS);
	    		t2 = new StringTokenizer(GPS, ",");
	    		desGPSInfo = new JSONObject();
	    		for (int j=1; t2.hasMoreTokens(); j++) {	    			
	    	        desGPSInfo.put("from", from);
	    	        desGPSInfo.put("num", i);
	    	        desGPSInfo.put("latitude", t2.nextToken());
	    	        desGPSInfo.put("longitude", t2.nextToken());
	    		}
	    		desGPSArray.add(desGPSInfo);
	    		desGPSInfo = null;
	    	 }
	        jsonObject.put("destination", desGPSArray);
	        //personInfo.put("time", "2018-5-22 9:00:00");
        	        
	        String jsonInfo = jsonObject.toJSONString();
	        
	        
	        /*
	        JSONParser parser = new JSONParser();	      	        
	        testt = parser.parse(jsonInfo);
	        JSONObject jsonObjj = (JSONObject) testt;
	        
	        System.out.println(jsonInfo); 
	        */
	        //====================================== 
			//==  Send Byte                       ==
			//======================================
	        System.out.println(jsonInfo);
	        String a = "aaa";
	        byte[] aa = a.getBytes(Charset.forName("utf-8"));
	        request_client.content(new BytesContentProvider(aa),"application/json");
	        content_reponse =  request_client.send();
	        body = jsonInfo.getBytes(Charset.forName("utf-8"));
	        request_client.content(new BytesContentProvider(body),"application/json");

	   
			//====================================== 
			//==  Send Json File                  ==
			//======================================
	        /*
		    Path path = Paths.get("C://Users/hanseok/Desktop/GPS_json.json");
			try {
				request.content(new PathContentProvider("json",path),"application/json");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			 
			//====================================== 
			//==  Send String                     ==
			//======================================		
			//request.content(new StringContentProvider(test),"application/xml");

			
			//send request!
	        
			content_reponse =  request_client.send();
			
			if(content_reponse == null) {
				System.out.print("res null.");
				return;
			}
			
			//String a = content_reponse.getContentAsString();
			//System.out.println(a);
						
			try {
				m_http_client.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
						
		} catch (InterruptedException | TimeoutException | ExecutionException   e) {//| UnsupportedEncodingException
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("exxception..");
		}
	}
}
