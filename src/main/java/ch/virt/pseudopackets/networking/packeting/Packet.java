package ch.virt.pseudopackets.networking.packeting;

import lombok.Getter;

/**
 * @author VirtCode
 * @version 1.0
 */
public abstract class Packet {
    @Getter
    protected int id;

    public Packet(int id) {
        this.id = id;
    }
}
