public class jettyclient{

	public static void main(String[] args) {
		int i=0;
		Myclient myclient = new Myclient();
		
		System.out.println("hi");
        log_management log_ma = new log_management();
        log_ma.start();

        try {
           while(true){
           System.out.println("main : "+ i);
           myclient.init();
           myclient.sendreq("shipID","GET");
                 Thread.sleep(1000);
                 i++;
              } 
           }catch (InterruptedException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
              }
    }
	
}
