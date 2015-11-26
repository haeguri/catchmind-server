package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// 두 번째 중요한 것. 리시버 혼자서 네트워크 처리. 계속 듣기.
public class PaintReceiver implements Runnable {
	
	Thread paintThread;
    private ServerSocket serverPaintSocket;
    private Socket paintSocket;
    private String paintInfo;

    //  03. 사용자들의 정보를 저장하는 맵입니다.
    private Map<String, DataOutputStream> paintClientMap = new HashMap<String, DataOutputStream>();

    private DataInputStream in;
    private DataOutputStream out;
    private String nick;
        
    // 맵의 내용(클라이언트) 저장과 삭제.
    public void addPaintClient(String nick, DataOutputStream out) {
        sendPaintInfo("paintSocket" + nick + "님이 접속하셨습니다. \n");
        paintClientMap.put(nick, out);
    }

    public void removePaintClient(String nick){
        sendPaintInfo("paintSocket" + nick + "님이 나가셨습니다.");
        paintClientMap.remove(nick);
        System.out.println("paintSocket" + "접속 종료");
    }

    public void sendPaintInfo(String paintInfo) {
        Iterator<String> it = paintClientMap.keySet().iterator();
        String key = "";

        while(it.hasNext()) {
            key = it.next();
            try {
            	paintClientMap.get(key).writeUTF(paintInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public PaintReceiver() {
    	try {
			serverPaintSocket = new ServerSocket(7778);
			while(true) {

	        	Collections.synchronizedMap(paintClientMap); // 교통정리 해주는 코드^^
	        	
	        	paintThread = new Thread(this, "Paint Tread");
	        	
	        	System.out.println("대기중...");
        		paintSocket = serverPaintSocket.accept(); // 서버가 할일은 계속 반복해서 사용자의 접속을 받아야 한다.
        		System.out.println("paintSocket" + paintSocket.getInetAddress()+" 에서 서버에 접속했습니다.");
        		
        		out = new DataOutputStream(paintSocket.getOutputStream());
        		in = new DataInputStream(paintSocket.getInputStream());
        		
        		nick = in.readUTF();
        		addPaintClient(nick, out);

        		this.start();
	        	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void start() {
    	paintThread.start();
    }

    // 리시버가 할일은 자기 혼자서 네트워크 처리 계속..듣기..처리해주는 것
    public void run() {

        try {    		
            while(in!=null) {
            	System.out.println("Paint 런 안의 와일");
                paintInfo = in.readUTF();
                sendPaintInfo(paintInfo);
            }
        }catch (IOException e) {
            removePaintClient(nick);
        }
    }
}