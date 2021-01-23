package ch.virt.pseudopackets.networking.packeting;

import ch.virt.pseudopackets.exceptions.InvalidPacketException;
import com.google.gson.Gson;

import java.util.Base64;

/**
 * @author VirtCode
 * @version 1.0
 */
public class PacketEncoder {

    private final Gson gson;
    private final Protocol protocol;

    public PacketEncoder(Protocol protocol){
        this.protocol = protocol;
        gson = new Gson();
    }

    public Packet decode(String s){
        String[] parts = s.split("\\.");
        if (parts.length != 2) throw new InvalidPacketException();
        if (!parts[0].matches("[0-9]+")) throw new InvalidPacketException();

        int id = Integer.parseInt(parts[0]);
        String decoded = new String(Base64.getDecoder().decode(parts[1]));

        try {
            return gson.fromJson(decoded, protocol.getClass(id));
        } catch (Exception e){
            throw new InvalidPacketException();
        }
    }

    public String encode(Packet p){
        String json = gson.toJson(p);
        String encoded = Base64.getEncoder().encodeToString(json.getBytes());
        return p.getId() + "." + encoded;
    }
}
