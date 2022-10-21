package thederpgamer.superstructures.data;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;

import java.io.IOException;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @version 1.0 - [12/06/2021]
 */
public interface DataSerializer {

    void serialize(PacketWriteBuffer packetWriteBuffer) throws IOException;
    void deserialize(PacketReadBuffer packetReadBuffer) throws IOException;
}
