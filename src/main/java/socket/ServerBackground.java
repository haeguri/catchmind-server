package socket;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerBackground {
    private ServerSocket serverChatSocket;
    private Socket chatSocket;
    
    private String chatMsg;

    //  03. 사용자들의 정보를 저장하는 맵입니다.
    private Map<String, DataOutputStream> chatClientMap = new HashMap<String, DataOutputStream>();

    public void setting() {
        try {
            // 7777 포트를 서버로 사용하겠다. 이제 서버가 열린 것.
            Collections.synchronizedMap(chatClientMap); // 교통정리 해주는 코드^^
            serverChatSocket = new ServerSocket(7777);
            
            while(true) {
                System.out.println("대기중...");
                chatSocket = serverChatSocket.accept(); // 서버가 할일은 계속 반복해서 사용자의 접속을 받아야 한다.
                System.out.println("chatSocket" + chatSocket.getInetAddress()+" 에서 서버에 접속했습니다.");
                ChatReceiver chatReceiver = new ChatReceiver(chatSocket);
                chatReceiver.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //-----------------------------ChatSocket----------------------------//
    //-------------------------------------------------------------------//

    // 맵의 내용(클라이언트) 저장과 삭제.
    public void addChatClient(String nick, DataOutputStream out) {
        sendMsg(nick + "님이 접속하셨습니다. \n");
        chatClientMap.put(nick, out);
    }

    public void removeChatClient(String nick){
        sendMsg(nick + "님이 나가셨습니다. \n");
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

    // 두 번째 중요한 것. 리시버 혼자서 네트워크 처리. 계속 듣기.
    public class ChatReceiver extends Thread {

        private DataInputStream in;
        private DataOutputStream out;
        private String nick;

        public ChatReceiver(Socket chatSocket) throws IOException {

    		out = new DataOutputStream(chatSocket.getOutputStream());
    		in = new DataInputStream(chatSocket.getInputStream());

    		nick = in.readUTF();
            addChatClient(nick, out);    
        }

        // 리시버가 할일은 자기 혼자서 네트워크 처리 계속..듣기..처리해주는 것
        @Override
        public void run() {
            try {
                while(in!=null) {
                    chatMsg = in.readUTF();
                    sendMsg(chatMsg);
                }
            }catch (IOException e) {
//              사용자 접속종료시 여기서 에러 발생. 그럼 나간거다. 그럼 여기서 remove 클라이언트.
                removeChatClient(nick);
            }
        }
    }
}

