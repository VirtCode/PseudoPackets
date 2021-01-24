package ch.virt.pseudopackets.networking;

import ch.virt.pseudopackets.exceptions.InvalidPacketException;
import ch.virt.pseudopackets.handlers.ClientPacketHandler;
import ch.virt.pseudopackets.packets.Packet;
import ch.virt.pseudopackets.packets.PacketEncoder;
import ch.virt.pseudopackets.packets.Protocol;

import java.io.*;
import java.net.Socket;

/**
 * @author VirtCode
 * @version 1.0
 */
public class Client extends Thread {

    private final ClientPacketHandler handler;

    private final PacketEncoder encoder;

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public Client(Protocol protocol, ClientPacketHandler handler) {
        this.handler = handler;

        this.encoder = new PacketEncoder(protocol);
    }

    public void connect(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);

        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.start();
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                String s = reader.readLine();
                if (s == null || s.equals("")) continue;

                Packet packet = encoder.decode(s);

                handler.handle(packet);

            } catch (InvalidPacketException e) {
                System.err.println("Failed to read Packet!");
            } catch (IOException ignored){ }
        }
    }

    public void close() throws IOException {
        socket.close();
    }

    public void sendPacket(Packet packet) {
        writer.println(encoder.encode(packet));
        writer.flush();
    }
}
