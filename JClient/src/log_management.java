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
    	//sentence �з�
    	System.out.println(str);
       StringTokenizer temp = new StringTokenizer(str,"\r\n");
      int countTokens = temp.countTokens();
    
      for(int i=0;i<countTokens;i++) {
         String sentence = temp.nextToken();
         
         if(sentence.contains("$")&&i==0) //ù��° ���� �޽����� ���켭�� jsonarray ���� �Ҵ�(new �Ѵ�) jsonarray �ѹ��� new
            sentence_parse(sentence,true,false);
         else if(sentence.contains("$")&&i==countTokens-2) // ������ ���ڿ� ���ؼ���  array��ü�� Object ȭ �Ѵ�.
            sentence_parse(sentence,false,true);
         else 
        	 sentence_parse(sentence,false,false); // �߰��� �޽������� ������ �迭�� �ִ´�.
        	

         
         
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
       //run �޼ҵ带 �������̵� �ϴ°�. 
       this.socket();         
    }
    
    //=========================================
     //=====socket ���� ���� �޽����� �Ľ��ؼ� json���� ����
     public void socket() {

         ServerSocket servsock = null;
         Socket sock = null;
         OutputStream outputstream = null;
         PrintStream printstream = null;
         
         
         try
         {
            byte[] data ; 
            
          servsock = new ServerSocket();
          //NameServer��  Listen �� ����
          
          InetSocketAddress ipep = new InetSocketAddress(6223);
          servsock.bind(ipep);
          
          while(true)
          {
        	  
         System.out.println("listening....");
        	  
          sock = servsock.accept();
          
        //Ŭ���̾�Ʈ�� ���� �޽��� �ޱ�
          InputStream inputstream = sock.getInputStream();
          data = new byte[2000];
          inputstream.read(data,0,data.length);
          
          
        //���� �޽���  'out'�� �ֱ� 
          String out = new String(data);
          System.out.println("Ŭ���̾�Ʈ �޽���: "+out);
       
          classify_sentence(out);
          
          data = null;
        
          
          //������ ���ؿ� �޽��� classify_sentence�Լ��� ������
             
         //Ŭ���̾�Ʈ�� �޽��� ������
          outputstream = sock.getOutputStream();
          printstream = new PrintStream(outputstream);
          printstream.println(myclient.UI_GPS);
          printstream.flush();
          System.out.println("TCP server UI_GPS : "+ myclient.UI_GPS);
          myclient.UI_GPS="";
          //������ �ϴ� ��Ʈ�� �ȿ� �����ִ� �����͸� ������ �������� ����
                
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