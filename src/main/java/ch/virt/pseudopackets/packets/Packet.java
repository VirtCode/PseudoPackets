package ch.virt.pseudopackets.packets;

/**
 * @author VirtCode
 * @version 1.0
 */
public abstract class Packet {
    protected int id;

    public Packet(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
