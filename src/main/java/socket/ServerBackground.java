package socket;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerBackground {
    private ServerSocket serverSocket;
    private Socket socket;
//    private ServerGUI gui;
    private String msg;

    //  03. 사용자들의 정보를 저장하는 맵입니다.
    private Map<String, DataOutputStream> clientMap = new HashMap<String, DataOutputStream>();

//    public void setGui(ServerGUI gui) {
//        this.gui = gui;
//    }

//  다중 채팅
//  서버에서는 클라이언트가 접속을 하면, 서버측에서는 소켓이 발생한다.
//  이 리시버는 하나의 클래스이자, 쓰레드 역할로 돌아간다.
//  이것들이 여러개가 돌아가며 각각의 소켓을 가지고 있는데.
//  클라이언트가 해주는 말을 듣고 있다가 메세지가 들어오면 서버에 전송을 시켜주면서.
//  서버는 클라이언트들이 접속할 때 가지고 있던 소켓정보들을 맵에다 저장해둔다.
//  그 맵에다 이 메세지를 뿌려주게 되면 맵에 있는 클라이언트 주소를 통해
//  다시 메세지를 보내주는 역할을 한다.

    public void setting() {
        try {
            // 7777 포트를 서버로 사용하겠다. 이제 서버가 열린 것.
            Collections.synchronizedMap(clientMap); // 교통정리 해주는 코드^^
            serverSocket = new ServerSocket(7777);
            while(true) {
                // 첫번째 중요한 것. 서버가 할일 분담. 계속 접속 받는 것
                System.out.println("대기중...");
                //대기 타는 것. 기방... 클라이언트가 들어올 때까지 대기를 한다.
                socket = serverSocket.accept(); // 서버가 할일은 계속 반복해서 사용자의 접속을 받아야 한다.
                System.out.println(socket.getInetAddress()+" 에서 서버에 접속했습니다.");
//              여기서 새로운 사용자 쓰레드 클래스 생성해서 소켓정보를 넣어줘야함 ! ! !
//              같은 변수명으로 생성해주는 것 같지만 반복하면서 다른 이름의 쓰레드를 생성한다.
                Receiver receiver = new Receiver(socket);
                receiver.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 맵의 내용(클라이언트) 저장과 삭제.
    public void addClient(String nick, DataOutputStream out) {
        sendMsg(nick + "님이 접속하셨습니다.");
        clientMap.put(nick, out);
    }

    public void removeClient(String nick){
        sendMsg(nick + "님이 나가셨습니다.");
        clientMap.remove(nick);
    }

    public void sendMsg(String msg) {
        Iterator<String> it = clientMap.keySet().iterator();
        String key = "";

        while(it.hasNext()) {
            key = it.next();
            try {
                clientMap.get(key).writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //------------------------------------------------------------//
    // 두 번째 중요한 것. 리시버 혼자서 네트워크 처리. 계속 듣기.
    class Receiver extends Thread {

        private DataInputStream in;
        private DataOutputStream out;
        private String nick;

        public Receiver(Socket socket) {
            try {
                // 서버와 클라이언트가 통신할 통로(스트림)이 열렸다.
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                nick = in.readUTF();
                addClient(nick, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 리시버가 할일은 자기 혼자서 네트워크 처리 계속..듣기..처리해주는 것
        @Override
        public void run() {

            try {
                while(in!=null) {
                    msg = in.readUTF();
                    sendMsg(msg);
//                    gui.appendMsg(msg);
                }
            }catch (IOException e) {
//              사용자 접속종료시 여기서 에러 발생. 그럼 나간거다. 그럼 여기서 remove 클라이언트.
                removeClient(nick);
            }
        }
    }



}

