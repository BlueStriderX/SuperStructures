package thederpgamer.superstructures.data.structures;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import thederpgamer.superstructures.data.DataSerializer;

import java.io.IOException;

/**
 * [Description]
 *
 * @author TheDerpGamer (TheDerpGamer#0027)
 */
public class SSLogEvent implements DataSerializer {

	public static final int LOG_EVENT_INFO = 0;
	public static final int LOG_EVENT_WARNING = 1;
	public static final int LOG_EVENT_ERROR = 2;

	private long timestamp;
	private int type;
	private String message;

	public SSLogEvent(int type, String message) {
		this.timestamp = System.currentTimeMillis();
		this.type = type;
		this.message = message;
	}

	public SSLogEvent(PacketReadBuffer packetReadBuffer) throws IOException {
		deserialize(packetReadBuffer);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getTime() {
		return String.format("%02d:%02d:%02d", (timestamp / 3600000) % 24, (timestamp / 60000) % 60, (timestamp / 1000) % 60);
	}

	public int getType() {
		return type;
	}

	public String getTypeString() {
		switch(type) {
			case LOG_EVENT_INFO:
				return "[INFO]: ";
			case LOG_EVENT_WARNING:
				return "[WARNING]: ";
			case LOG_EVENT_ERROR:
				return "[ERROR]: ";
			default:
				throw new IllegalArgumentException("Invalid log event type: " + type);
		}
	}

	public String getMessage() {
		return message;
	}

	@Override
	public void serialize(PacketWriteBuffer packetWriteBuffer) throws IOException {
		packetWriteBuffer.writeLong(timestamp);
		packetWriteBuffer.writeInt(type);
		packetWriteBuffer.writeString(message);
	}

	@Override
	public void deserialize(PacketReadBuffer packetReadBuffer) throws IOException {
		timestamp = packetReadBuffer.readLong();
		type = packetReadBuffer.readInt();
		message = packetReadBuffer.readString();
	}

	@Override
	public String toString() {
		return getTime() + " - " + getTypeString() + message;
	}
}
