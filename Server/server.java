
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
class serverclient{
	ServerSocket serverSocket;
	
	public void pasteClipboard(Robot robot) 
	{
	robot.keyPress(KeyEvent.VK_CONTROL);
	robot.keyPress(KeyEvent.VK_V);
	robot.delay(50);
	robot.keyRelease(KeyEvent.VK_V);
	robot.keyRelease(KeyEvent.VK_CONTROL); 
	
	}

	public void type(String text,Robot robot)
	{ 
	writeToClipboard(text);
	pasteClipboard(robot);
	}

	private void writeToClipboard(String s)
	{
	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	Transferable transferable = new StringSelection(s);
	clipboard.setContents(transferable, null);
	}

	public void getClient(){
		int currX;
        int currY;    
        int yloc;
        char keyp;
        byte[] buff = new byte[1000];
        Robot robot=null;
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DatagramPacket receivepacket;
		DatagramSocket socket=null;
		Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
            socket = new DatagramSocket(4445);
            System.out.println("Connection Accepted");
            System.out.println("DatagramSocket opened.");
            
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        
        String inputLine;
	PointerInfo a = MouseInfo.getPointerInfo();
        		Point b = a.getLocation();
			int scDown=0,scUp=0;
			float disp;
        		currX = (int)b.getX();
        		currY=(int)b.getY();
        		
        try {
			while (!clientSocket.isClosed()) {
				 receivepacket = new DatagramPacket(buff, buff.length);
                 socket.receive(receivepacket);
                 inputLine= new String(receivepacket.getData(),0,receivepacket.getLength());
				if(inputLine.startsWith("#sc")){	
					
					disp = Float.parseFloat(inputLine.substring(3));
					int dis = (int)(disp/2);

					if(disp>0&&disp<2)
					scDown++;
					else if(disp>-2&&disp<0)
					scUp++;

					if(scUp==3)
					{     robot.mouseWheel(-1); scUp=0; }
					else if(scDown==3)
   				        {    robot.mouseWheel(1);  scDown=0; }
 					
					robot.mouseWheel(dis);
				}
				
				else if(inputLine.startsWith(":")){
					type(inputLine.substring(1), robot);
				}
				else if(inputLine.startsWith("back")){
					robot.keyPress(KeyEvent.VK_BACK_SPACE);
					robot.delay(50);
					robot.keyRelease(KeyEvent.VK_BACK_SPACE);
				}
				else if(inputLine.startsWith("enter")){
					robot.keyPress(KeyEvent.VK_ENTER);
					robot.delay(50);
					robot.keyRelease(KeyEvent.VK_ENTER);
				}
				else if(inputLine.equals("LeftClick")){
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
				}
				else if(inputLine.equals("RightClick")){
					robot.mousePress(InputEvent.BUTTON3_MASK);
					robot.mouseRelease(InputEvent.BUTTON3_MASK);
				}
				else if(inputLine.equals("lpress")){
					robot.mousePress(InputEvent.BUTTON1_MASK);
				}
				else if(inputLine.equals("lrelease")){
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
				}
				else{
					yloc = inputLine.indexOf("Y");
					int xcord = (int)Float.parseFloat(inputLine.substring(2,yloc));	
					int ycord = (int)Float.parseFloat(inputLine.substring(yloc+2));
				currX+=2*xcord;
				currY+=2*ycord;
					robot.mouseMove(currX,currY); 
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
        System.out.println("Client Disconnected");
        try {
			
			clientSocket.close();
	        serverSocket.close();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			setServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	public void setServer()throws Exception{
		serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
            System.out.println("Listening for connections.");
            
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4433.");
            System.exit(1);
        }
        getClient();
	}
}
public class server {
	
	public static void main(String[] args) throws Exception  {
       serverclient s= new serverclient();
		s.setServer();
    }
}