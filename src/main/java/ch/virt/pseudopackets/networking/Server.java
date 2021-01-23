package ch.virt.pseudopackets.networking;

import ch.virt.bruhgame.networking.packeting.PacketEncoder;
import ch.virt.bruhgame.networking.packeting.Protocol;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author VirtCode
 * @version 1.0
 */
public class Server extends Thread{

    public static void main(String[] args) {
        new Server(1234);
    }

    private ServerSocket socket;

    private PacketEncoder encoder;
    private Protocol protocol;

    private boolean running;
    private int port;

    public Server(int port){
        protocol = new Protocol();
        encoder = new PacketEncoder(protocol);

        running = true;
        this.port = port;
        this.start();
    }

    @Override
    public void run(){
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Failed to open Server");
            e.printStackTrace();
        }

        System.out.println("Server Started!");

        while (running){
            try {
                new ClientWrapper(socket.accept(), encoder).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
