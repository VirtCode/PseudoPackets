package ch.virt.pseudopackets.server;

import ch.virt.pseudopackets.exceptions.InvalidPacketException;
import ch.virt.pseudopackets.handlers.ServerPacketHandler;
import ch.virt.pseudopackets.packets.Packet;
import ch.virt.pseudopackets.packets.PacketEncoder;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

/**
 * @author VirtCode
 * @version 1.0
 */
public class ClientWrapper extends Thread {
    private final Socket socket;
    private final UUID id;
    private final PacketEncoder encoder;
    private final ServerPacketHandler receiver;
    private final ClientDisconnectedEvent disconnectRemover;

    private PrintWriter writer;
    private BufferedReader reader;

    private boolean disconnected;

    public ClientWrapper(Socket socket, UUID id, PacketEncoder encoder, ServerPacketHandler receiver, ClientDisconnectedEvent disconnectRemover) {
        this.socket = socket;
        this.id = id;
        this.encoder = encoder;
        this.receiver = receiver;
        this.disconnectRemover = disconnectRemover;
    }

    public UUID getUuid() {
        return id;
    }

    @Override
    public void run() {
        disconnected = false;
        try {
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            receiver.connected(id);

            while (socket.isConnected()) {
                try {
                    String s = reader.readLine();
                    if (s == null) break;
                    if (s.equals("")) continue;

                    Packet packet = encoder.decode(s);

                    receiver.handle(packet, id);

                } catch (InvalidPacketException e) {
                    System.err.println("Failed to read Packet!");
                } catch (IOException ignored){ break; }
            }

            if(!disconnected) disconnect();
        } catch (IOException e) {
            System.err.println("Failed to connect with Client!");
        }
    }

    public void disconnect() throws IOException {
        receiver.disconnected(id);
        socket.close();
        disconnectRemover.disconnected(id);
        disconnected = true;
    }

    public void sendPacket(Packet packet){
        writer.println(encoder.encode(packet));
        writer.flush();
    }
}
