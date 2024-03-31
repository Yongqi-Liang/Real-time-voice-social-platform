/**
 * Created with IntelliJ IDEA.
 *
 * @FileName: ServerStart.java
 * @Author: Yongqi Liang
 * @Email: my@liangyongqi.top
 * @Date: 2024/03/31/16:16
 * @Description: 启动类
 */

public class ServerStart {
    public static void main(String[] args) throws Exception {
        // 启动聊天室
        ChatSocket.startServer();
    }
}