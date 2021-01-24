package ch.virt.pseudopackets.server;

import ch.virt.pseudopackets.handlers.ServerPacketHandler;
import ch.virt.pseudopackets.packets.Packet;
import ch.virt.pseudopackets.packets.PacketEncoder;
import ch.virt.pseudopackets.packets.Protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * @author VirtCode
 * @version 1.0
 */
public class Server extends Thread{

    private ServerSocket socket;
    private HashMap<UUID, ClientWrapper> acceptedClients;

    private PacketEncoder encoder;
    private ServerPacketHandler handler;

    private boolean running;
    private int port;

    public Server(Protocol protocol, ServerPacketHandler packets, int port){
        encoder = new PacketEncoder(protocol);
        this.handler = packets;
        this.acceptedClients = new HashMap<>();

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

        while (running){
            try {
                acceptClient(socket.accept());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() throws IOException {
        running = false;
        List<UUID> list = new ArrayList<>(acceptedClients.keySet());
        for (UUID uuid : list) {
            acceptedClients.get(uuid).disconnect();
        }
    }

    public void acceptClient(Socket socket){
        ClientWrapper wrapper = new ClientWrapper(socket, UUID.randomUUID(), encoder, handler, this::disconnectedClient);
        acceptedClients.put(wrapper.getUuid(), wrapper);
        wrapper.start();
    }

    public void disconnectClient(UUID uuid) throws IOException {
        acceptedClients.get(uuid).disconnect();
    }

    private void disconnectedClient(UUID uuid){
        acceptedClients.remove(uuid);
    }

    public void sendPacket(Packet packet, UUID uuid){
        acceptedClients.get(uuid).sendPacket(packet);
    }
}
