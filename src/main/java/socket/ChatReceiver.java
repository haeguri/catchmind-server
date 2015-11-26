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
public class ChatReceiver implements Runnable {
	
	Thread chatThread;
	private ServerSocket serverChatSocket;
    private ServerSocket serverPaintSocket;
    private Socket chatSocket;
    private Socket paintSocket;
    
    private String chatMsg;
    private String paintInfo;

    //  03. 사용자들의 정보를 저장하는 맵입니다.
    private Map<String, DataOutputStream> chatClientMap = new HashMap<String, DataOutputStream>();
    private Map<String, DataOutputStream> paintClientMap = new HashMap<String, DataOutputStream>();

    private DataInputStream in;
    private DataOutputStream out;
    private String nick;
        
    // 맵의 내용(클라이언트) 저장과 삭제.
    public void addChatClient(String nick, DataOutputStream out) {
        sendMsg("chatSocket" + nick + "님이 접속하셨습니다. \n");
        chatClientMap.put(nick, out);
    }

    public void removeChatClient(String nick){
        sendMsg("chatSocket" + nick + "님이 나가셨습니다.");
        chatClientMap.remove(nick);
        System.out.println("chatSocket" + "접속 종료");
    }

    public void sendMsg(String msg) {
        Iterator<String> it = chatClientMap.keySet().iterator();
        String key = "";

        while(it.hasNext()) {
            key = it.next();
            try {
            	chatClientMap.get(key).writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public ChatReceiver() {
    	try {
			serverChatSocket = new ServerSocket(7777);
			while(true) {

	        	Collections.synchronizedMap(chatClientMap); // 교통정리 해주는 코드^^
	        	
	        	chatThread = new Thread(this, "Chat Tread");
	        	
	        	System.out.println("대기중...");
        		chatSocket = serverChatSocket.accept(); // 서버가 할일은 계속 반복해서 사용자의 접속을 받아야 한다.
        		System.out.println("chatSocket" + chatSocket.getInetAddress()+" 에서 서버에 접속했습니다.");
        		
        		out = new DataOutputStream(chatSocket.getOutputStream());
        		in = new DataInputStream(chatSocket.getInputStream());
        		
        		nick = in.readUTF();
        		addChatClient(nick, out);

        		this.start();
	        	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void start() {
    	chatThread.start();
    }

    // 리시버가 할일은 자기 혼자서 네트워크 처리 계속..듣기..처리해주는 것
    public void run() {

        try {    		
            while(in!=null) {
            	System.out.println("런 안의 와일");
                chatMsg = in.readUTF();
                sendMsg(chatMsg);
            }
        }catch (IOException e) {
            removeChatClient(nick);
        }
    }
}