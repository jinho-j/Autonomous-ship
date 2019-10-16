import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class log_management extends Thread{
   
    JSONArray SentenceArray = null ; 
    Myclient myclient = new Myclient();
  
   
    
    public void classify_sentence(String str) {  
    	//sentence 분류
    	System.out.println(str);
       StringTokenizer temp = new StringTokenizer(str,"\r\n");
      int countTokens = temp.countTokens();
    
      for(int i=0;i<countTokens;i++) {
         String sentence = temp.nextToken();
         
         if(sentence.contains("$")&&i==0) //첫번째 들어온 메시지에 대헤서는 jsonarray 새로 할당(new 한다) jsonarray 한번만 new
            sentence_parse(sentence,true,false);
         else if(sentence.contains("$")&&i==countTokens-2) // 마지막 문자에 대해서는  array전체를 Object 화 한다.
            sentence_parse(sentence,false,true);
         else 
        	 sentence_parse(sentence,false,false); // 중간에 메시지들은 생성된 배열에 넣는다.
        	

         
         
         }
    }
    
    
    public void sentence_parse(String sentence,boolean first_in,boolean last_in)
      {
    	
       
          JSONObject SentenceInfo; 
          String message;
          int start = sentence.indexOf(",");
          
          if(first_in)
             SentenceArray = new JSONArray();
          
          if(start==-1)
           return;
          
         String formatter = sentence.substring(0,start);
         
         String str = sentence.substring(start+1);
      
         if(formatter.length()!=6)
            return;
         
        SentenceInfo = new JSONObject();
        SentenceInfo.put("Format", formatter);
        SentenceInfo.put("data", str);

       SentenceArray.add(SentenceInfo);
       JSONObject j = new JSONObject();
      
       
           
           if(last_in) 
           {
           j.put("LOGDATA", SentenceArray);
           //message = j.toJSONString();
           }
           
          
           
          message = j.toJSONString();
        
           Calendar cal = Calendar.getInstance();
          String dateString;

          dateString = String.format("%04d-%02d-%02d-%02d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE) , cal.get(Calendar.SECOND) );
       
      
          try {
             FileWriter file = new FileWriter("C:\\json\\"+dateString+".json");
            
            file.write(message);
            
            
            file.flush();
            SentenceInfo = null;
            
            
            if(last_in) 
            {
            	
            SentenceArray =null;
            myclient.init();
            myclient.sendreq("C:\\json\\"+dateString+".json","POST");
            
            }
            
            
            file.close();
           
            

            
            

          }catch (FileNotFoundException e) {
             e.printStackTrace();
          }catch (IOException e) {
             e.printStackTrace();
          }catch (IndexOutOfBoundsException e) {
             e.printStackTrace();
          }
         
      }

   
    
    public void run()
    
    {
       //run 메소드를 오버라이딩 하는것. 
       this.socket();         
    }
    
    //=========================================
     //=====socket 으로 받은 메시지를 파싱해서 json파일 생성
     public void socket() {

         ServerSocket servsock = null;
         Socket sock = null;
         OutputStream outputstream = null;
         PrintStream printstream = null;
         
         
         try
         {
            byte[] data ; 
            
          servsock = new ServerSocket();
          //NameServer의  Listen 할 소켓
          
          InetSocketAddress ipep = new InetSocketAddress(6223);
          servsock.bind(ipep);
          
          while(true)
          {
        	  
         System.out.println("listening....");
        	  
          sock = servsock.accept();
          
        //클라이언트로 부터 메시지 받기
          InputStream inputstream = sock.getInputStream();
          data = new byte[2000];
          inputstream.read(data,0,data.length);
          
          
        //받은 메시지  'out'에 넣기 
          String out = new String(data);
          System.out.println("클라이언트 메시지: "+out);
       
          classify_sentence(out);
          
          data = null;
        
          
          //소켓을 통해온 메시지 classify_sentence함수로 보내기
             
         //클라이언트로 메시지 보내기
          outputstream = sock.getOutputStream();
          printstream = new PrintStream(outputstream);
          printstream.println(myclient.UI_GPS);
          printstream.flush();
          System.out.println("TCP server UI_GPS : "+ myclient.UI_GPS);
          myclient.UI_GPS="";
          //전송을 하는 스트림 안에 남아있는 데이터를 강제로 내보내는 역할
                
         }
        }   
         catch(IOException ie) {
                   ie.printStackTrace();         
         }

      finally {

        try {
        	 servsock.close();
             outputstream.close();
             printstream.close();
             
                 if (servsock != null)
                             servsock.close();
                 if (outputstream != null)
                             outputstream.close();
                 if (printstream != null)
                              printstream.close();
                 if (sock != null)
                                             sock.close();
        }
         catch(Exception e) {
                     e.printStackTrace();
         }
      }
    }
     
     
     
   
}