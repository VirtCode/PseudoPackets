package ch.virt.pseudopackets.networking.packeting;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VirtCode
 * @version 1.0
 */
public class Protocol {

    private Map<Integer, Class<? extends Packet>> map;

    public Protocol() {
        map = new HashMap<>();
    }

    public void addPacket(Class<? extends Packet> packetClass, int id){
        map.put(id, packetClass);
    }

    public Class<? extends Packet> getClass(int id){
        return map.get(id);
    }
}
