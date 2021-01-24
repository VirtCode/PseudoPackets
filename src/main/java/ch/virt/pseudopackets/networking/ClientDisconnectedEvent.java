package ch.virt.pseudopackets.networking;

import java.util.UUID;

public interface ClientDisconnectedEvent {
    void disconnected(UUID uuid);
}
