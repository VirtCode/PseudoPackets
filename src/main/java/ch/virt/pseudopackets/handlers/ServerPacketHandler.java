package ch.virt.pseudopackets.handlers;

import ch.virt.pseudopackets.packets.Packet;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author VirtCode
 * @version 1.0
 */
public abstract class ServerPacketHandler {

    public abstract void connected(UUID client);

    public abstract void disconnected(UUID client);

    public void handle(Packet packet, UUID client){
        try {
            Method method = this.getClass().getMethod("handle", packet.getClass(), UUID.class);
            method.invoke(this, packet, client);
        } catch (Exception e) {
            System.err.println("Failed to handle Packet: " + packet.getClass());
        }
    }
}
