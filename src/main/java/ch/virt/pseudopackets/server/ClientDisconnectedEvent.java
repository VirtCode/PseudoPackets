package ch.virt.pseudopackets.server;

import java.util.UUID;

public interface ClientDisconnectedEvent {
    void disconnected(UUID uuid);
}
