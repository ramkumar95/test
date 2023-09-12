package shellscript;

import java.io.InputStream;
import com.jcraft.jsch.*;

public class script {
    public static void main(String[] args) {
        String host = "172.30.156.222"; // Replace with your remote host
        String user = "root";    // Replace with your username
        String password = "password"; // Replace with your password
        int port = 22;               // SSH port is usually 22

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();

            String command = "ip addr"; // Replace with your desired command

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int bytesRead = in.read(tmp, 0, 1024);
                    if (bytesRead < 0) break;
                    System.out.print(new String(tmp, 0, bytesRead));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    System.out.println("Exit Status: " + channel.getExitStatus());
                    break;
                }
                Thread.sleep(1000);
            }

            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
