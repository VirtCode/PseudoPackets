package ch.virt.pseudopackets.handlers;

import ch.virt.pseudopackets.packets.Packet;

import java.lang.reflect.Method;

/**
 * @author VirtCode
 * @version 1.0
 */
public abstract class ClientPacketHandler {

    public abstract void connected();

    public abstract void disconnected();

    public void handle(Packet packet){
        try {
            Method method = this.getClass().getMethod("handle", packet.getClass());
            method.invoke(this, packet);
        } catch (Exception e) {
            System.err.println("Failed to handle Packet: " + packet.getClass());
        }
    }
}
