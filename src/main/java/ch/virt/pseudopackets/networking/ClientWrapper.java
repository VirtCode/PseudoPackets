package ch.virt.pseudopackets.networking;

import ch.virt.bruhgame.networking.packeting.InvalidPacketException;
import ch.virt.bruhgame.networking.packeting.Packet;
import ch.virt.bruhgame.networking.packeting.PacketEncoder;
import ch.virt.bruhgame.networking.packets.LoginPacket;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author VirtCode
 * @version 1.0
 */
public class ClientWrapper extends Thread {
    private final Socket socket;
    private final PacketEncoder encoder;

    private PrintWriter writer;
    private BufferedReader reader;

    public ClientWrapper(Socket socket, PacketEncoder encoder) {
        this.socket = socket;
        this.encoder = encoder;
    }

    @SneakyThrows
    @Override
    public void run() {
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println("Accepted Client!");

        while (socket.isConnected()){
            String s = reader.readLine();
            if (s == null || s.equals("")) continue;

            try {
                Packet packet = encoder.decode(s);

            } catch (InvalidPacketException e) {
                System.err.println("Failed to read Packet!");
            }
        }

        System.out.println("Closed Client!");
        socket.close();
    }

    private void sendPacket(Packet packet){
        writer.println(encoder.encode(packet));
        writer.flush();
    }
}
