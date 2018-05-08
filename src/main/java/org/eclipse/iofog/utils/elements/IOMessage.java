package org.eclipse.iofog.utils.elements;

import org.eclipse.iofog.utils.ByteUtils;
import org.eclipse.iofog.utils.IOMessageUtils;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * IOMessage represent all message communication between ioFog and Containers.
 */
public class IOMessage {

    private final short VERSION = 4;

    public static final String ID_FIELD_NAME = "id";
    public static final String TAG_FIELD_NAME = "tag";
    public static final String GROUP_ID_FIELD_NAME = "groupid";
    public static final String SEQUENCE_NUMBER_FIELD_NAME = "sequencenumber";
    public static final String SEQUENCE_TOTAL_FIELD_NAME = "sequencetotal";
    public static final String PRIORITY_FIELD_NAME = "priority";
    public static final String TIMESTAMP_FIELD_NAME = "timestamp";
    public static final String PUBLISHER_FIELD_NAME = "publisher";
    public static final String AUTH_ID_FIELD_NAME = "authid";
    public static final String AUTH_GROUP_FIELD_NAME = "authgroup";
    public static final String VERSION_FIELD_NAME = "version";
    public static final String CHAIN_POSITION_FIELD_NAME = "chainposition";
    public static final String HASH_FIELD_NAME = "hash";
    public static final String PREVIOUS_HASH_FIELD_NAME = "previoushash";
    public static final String NONCE_FIELD_NAME = "nonce";
    public static final String DIFFICULTY_TARGET_FIELD_NAME = "difficultytarget";
    public static final String INFO_TYPE_FIELD_NAME = "infotype";
    public static final String INFO_FORMAT_FIELD_NAME = "infoformat";
    public static final String CONTEXT_DATA_FIELD_NAME = "contextdata";
    public static final String CONTENT_DATA_FIELD_NAME = "contentdata";

    private String id = ""; // required
    private String tag = "";
    private String groupId = "";
    private int sequenceNumber;
    private int sequenceTotal;
    private byte priority;
    private long timestamp; // required
    private String publisher = ""; // required
    private String authId = "";
    private String authGroup = "";
    private short version = VERSION; // required
    private long chainPosition;
    private String hash = "";
    private String previousHash = "";
    private String nonce = "";
    private int difficultyTarget;
    private String infoType = ""; // required
    private String infoFormat = ""; // required
    private byte[] contextData;
    private byte[] contentData; // required

    public IOMessage() {
    }

    public IOMessage(byte[] rawBytes) {
        convertBytesToMessage(null, rawBytes, 33);
    }

    public IOMessage(byte[] header, byte[] data) {
        convertBytesToMessage(header, data, 0);
    }

    public IOMessage(JsonObject json) {
        if (json.containsKey(ID_FIELD_NAME)) {
            id = json.getString(ID_FIELD_NAME);
        }
        if (json.containsKey(TAG_FIELD_NAME)) {
            tag = json.getString(TAG_FIELD_NAME);
        }
        if (json.containsKey(GROUP_ID_FIELD_NAME)) {
            groupId = json.getString(GROUP_ID_FIELD_NAME);
        }
        if (json.containsKey(SEQUENCE_NUMBER_FIELD_NAME)) {
            sequenceNumber = json.getInt(SEQUENCE_NUMBER_FIELD_NAME);
        }
        if (json.containsKey(SEQUENCE_TOTAL_FIELD_NAME)) {
            sequenceTotal = json.getInt(SEQUENCE_TOTAL_FIELD_NAME);
        }
        if (json.containsKey(PRIORITY_FIELD_NAME)) {
            priority = (byte) json.getInt(PRIORITY_FIELD_NAME);
        }
        if (json.containsKey(TIMESTAMP_FIELD_NAME)) {
            timestamp = json.getJsonNumber(TIMESTAMP_FIELD_NAME).longValue();
        }
        if (json.containsKey(PUBLISHER_FIELD_NAME)) {
            publisher = json.getString(PUBLISHER_FIELD_NAME);
        }
        if (json.containsKey(AUTH_ID_FIELD_NAME)) {
            authId = json.getString(AUTH_ID_FIELD_NAME);
        }
        if (json.containsKey(AUTH_GROUP_FIELD_NAME)) {
            authGroup = json.getString(AUTH_GROUP_FIELD_NAME);
        }
        if (json.containsKey(CHAIN_POSITION_FIELD_NAME)) {
            chainPosition = json.getJsonNumber(CHAIN_POSITION_FIELD_NAME).longValue();
        }
        if (json.containsKey(HASH_FIELD_NAME)) {
            hash = json.getString(HASH_FIELD_NAME);
        }
        if (json.containsKey(PREVIOUS_HASH_FIELD_NAME)) {
            previousHash = json.getString(PREVIOUS_HASH_FIELD_NAME);
        }
        if (json.containsKey(NONCE_FIELD_NAME)) {
            nonce = json.getString(NONCE_FIELD_NAME);
        }
        if (json.containsKey(DIFFICULTY_TARGET_FIELD_NAME)) {
            difficultyTarget = json.getInt(DIFFICULTY_TARGET_FIELD_NAME);
        }
        if (json.containsKey(INFO_TYPE_FIELD_NAME)) {
            infoType = json.getString(INFO_TYPE_FIELD_NAME);
        }
        if (json.containsKey(INFO_FORMAT_FIELD_NAME)) {
            infoFormat = json.getString(INFO_FORMAT_FIELD_NAME);
        }
        if (json.containsKey(CONTEXT_DATA_FIELD_NAME)) {
            contextData = IOMessageUtils.decodeBase64(json.getString(CONTEXT_DATA_FIELD_NAME).getBytes());
        }
        if (json.containsKey(CONTENT_DATA_FIELD_NAME)) {
            contentData = IOMessageUtils.decodeBase64(json.getString(CONTENT_DATA_FIELD_NAME).getBytes());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getSequenceTotal() {
        return sequenceTotal;
    }

    public void setSequenceTotal(int sequenceTotal) {
        this.sequenceTotal = sequenceTotal;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getAuthGroup() {
        return authGroup;
    }

    public void setAuthGroup(String authGroup) {
        this.authGroup = authGroup;
    }

    public short getVersion() {
        return version;
    }

    public long getChainPosition() {
        return chainPosition;
    }

    public void setChainPosition(long chainPosition) {
        this.chainPosition = chainPosition;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public int getDifficultyTarget() {
        return difficultyTarget;
    }

    public void setDifficultyTarget(int difficultyTarget) {
        this.difficultyTarget = difficultyTarget;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getInfoFormat() {
        return infoFormat;
    }

    public void setInfoFormat(String infoFormat) {
        this.infoFormat = infoFormat;
    }

    public byte[] getContextData() {
        return contextData;
    }

    public void setContextData(byte[] contextData) {
        this.contextData = contextData;
    }

    public byte[] getContentData() {
        return contentData;
    }

    public void setContentData(byte[] contentData) {
        this.contentData = contentData;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                   .add(ID_FIELD_NAME, id)
                   .add(TAG_FIELD_NAME, tag)
                   .add(GROUP_ID_FIELD_NAME, groupId)
                   .add(SEQUENCE_NUMBER_FIELD_NAME, sequenceNumber)
                   .add(SEQUENCE_TOTAL_FIELD_NAME, sequenceTotal)
                   .add(PRIORITY_FIELD_NAME, priority)
                   .add(TIMESTAMP_FIELD_NAME, timestamp)
                   .add(PUBLISHER_FIELD_NAME, publisher)
                   .add(AUTH_ID_FIELD_NAME, authId)
                   .add(AUTH_GROUP_FIELD_NAME, authGroup)
                   .add(VERSION_FIELD_NAME, version)
                   .add(CHAIN_POSITION_FIELD_NAME, chainPosition)
                   .add(HASH_FIELD_NAME, hash)
                   .add(PREVIOUS_HASH_FIELD_NAME, previousHash)
                   .add(NONCE_FIELD_NAME, nonce)
                   .add(DIFFICULTY_TARGET_FIELD_NAME, difficultyTarget)
                   .add(INFO_TYPE_FIELD_NAME, infoType)
                   .add(INFO_FORMAT_FIELD_NAME, infoFormat)
                   .add(CONTEXT_DATA_FIELD_NAME, new String(IOMessageUtils.encodeBase64(contextData)))
                   .add(CONTENT_DATA_FIELD_NAME, new String(IOMessageUtils.encodeBase64(contentData)))
                   .build();
    }

    public byte[] toBytes() {
        try (ByteArrayOutputStream headerBytes = new ByteArrayOutputStream();
             ByteArrayOutputStream dataBytes = new ByteArrayOutputStream();
             ByteArrayOutputStream result = new ByteArrayOutputStream()) {


            headerBytes.write(ByteUtils.shortToBytes(VERSION));
            int len = ByteUtils.getLength(id);
            headerBytes.write((byte) len);
            if (len > 0) {
                dataBytes.write(ByteUtils.stringToBytes(getId()));
            }

            len = ByteUtils.getLength(tag);
            headerBytes.write(ByteUtils.shortToBytes((short) len));
            if (len > 0) {
                dataBytes.write(ByteUtils.stringToBytes(getTag()));
            }

            len = ByteUtils.getLength(groupId);
            headerBytes.write((byte) len);
            if (len > 0) {
                dataBytes.write(ByteUtils.stringToBytes(groupId));
            }

            if (getSequenceNumber() == 0) {
                headerBytes.write(0);
            } else {
                dataBytes.write(ByteUtils.integerToBytes(sequenceNumber));
                headerBytes.write(4);
            }

            if (getSequenceTotal() == 0) {
                headerBytes.write(0);
            } else {
                dataBytes.write(ByteUtils.integerToBytes(sequenceTotal));
                headerBytes.write(4);
            }

            if (getPriority() == 0) {
                headerBytes.write(0);
            } else {
                headerBytes.write(1);
                dataBytes.write(getPriority());
            }

            if (getTimestamp() == 0) {
                headerBytes.write(0);
            } else {
                headerBytes.write(8);
                dataBytes.write(ByteUtils.longToBytes(timestamp));
            }

            len = ByteUtils.getLength(getPublisher());
            headerBytes.write((byte) (len & 0xff));
            if (len > 0) {
                dataBytes.write(ByteUtils.stringToBytes(publisher));
            }

            len = ByteUtils.getLength(getAuthId());
            headerBytes.write(ByteUtils.shortToBytes((short) len));
            if (len > 0) {
                dataBytes.write(ByteUtils.stringToBytes(authId));
            }

            len = ByteUtils.getLength(authGroup);
            headerBytes.write(ByteUtils.shortToBytes((short) len));
            if (len > 0) {
                dataBytes.write(ByteUtils.stringToBytes(authGroup));
            }

            if (getChainPosition() == 0) {
                headerBytes.write(0);
            } else {
                headerBytes.write(8);
                dataBytes.write(ByteUtils.longToBytes(chainPosition));
            }

            len = ByteUtils.getLength(hash);
            headerBytes.write(ByteUtils.shortToBytes((short) len));
            if (len > 0) {
                dataBytes.write(ByteUtils.stringToBytes(hash));
            }

            len = ByteUtils.getLength(previousHash);
            headerBytes.write(ByteUtils.shortToBytes((short) len));
            if (len > 0) {
                dataBytes.write(ByteUtils.stringToBytes(previousHash));
            }

            len = ByteUtils.getLength(getNonce());
            headerBytes.write(ByteUtils.shortToBytes((short) len));
            if (len > 0) {
                dataBytes.write(ByteUtils.stringToBytes(nonce));
            }

            if (getDifficultyTarget() == 0) {
                headerBytes.write(0);
            } else {
                headerBytes.write(4);
                dataBytes.write(ByteUtils.integerToBytes(difficultyTarget));
            }

            len = ByteUtils.getLength(getInfoType());
            headerBytes.write((byte) len);
            if (len > 0) {
                dataBytes.write(ByteUtils.stringToBytes(infoType));
            }

            len = ByteUtils.getLength(infoFormat);
            headerBytes.write((byte) len);
            if (len > 0) {
                dataBytes.write(ByteUtils.stringToBytes(infoFormat));
            }

            if (getContextData() == null) {
                headerBytes.write(ByteUtils.integerToBytes(0));
            } else {
                headerBytes.write(ByteUtils.integerToBytes(contextData.length));
                dataBytes.write(contextData);
            }

            if (getContentData() == null) {
                headerBytes.write(ByteUtils.integerToBytes(0));
            } else {
                headerBytes.write(ByteUtils.integerToBytes(contentData.length));
                dataBytes.write(contentData);
            }

            headerBytes.writeTo(result);
            dataBytes.writeTo(result);
            return result.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    private void convertBytesToMessage(byte[] header, byte[] data, int pos) {
        if (header == null || header.length == 0) {
            header = data;
        }
        version = ByteUtils.bytesToShort(Arrays.copyOfRange(header, 0, 2));

        if (version != VERSION) {
            // TODO: incompatible version
            return;
        }

        int size = header[2];
        if (size > 0) {
            setId(ByteUtils.bytesToString(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = ByteUtils.bytesToShort(Arrays.copyOfRange(header, 3, 5));
        if (size > 0) {
            setTag(ByteUtils.bytesToString(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = header[5];
        if (size > 0) {
            setGroupId(ByteUtils.bytesToString(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = header[6];
        if (size > 0) {
            setSequenceNumber(ByteUtils.bytesToInteger(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = header[7];
        if (size > 0) {
            setSequenceTotal(ByteUtils.bytesToInteger(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = header[8];
        if (size > 0) {
            setPriority(data[pos]);
            pos += size;
        }

        size = header[9];
        if (size > 0) {
            setTimestamp(ByteUtils.bytesToLong(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = header[10];
        if (size > 0) {
            setPublisher(ByteUtils.bytesToString(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = ByteUtils.bytesToShort(Arrays.copyOfRange(header, 11, 13));
        if (size > 0) {
            setAuthId(ByteUtils.bytesToString(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = ByteUtils.bytesToShort(Arrays.copyOfRange(header, 13, 15));
        if (size > 0) {
            setAuthGroup(ByteUtils.bytesToString(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = header[15];
        if (size > 0) {
            setChainPosition(ByteUtils.bytesToLong(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = ByteUtils.bytesToShort(Arrays.copyOfRange(header, 16, 18));
        if (size > 0) {
            setHash(ByteUtils.bytesToString(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = ByteUtils.bytesToShort(Arrays.copyOfRange(header, 18, 20));
        if (size > 0) {
            setPreviousHash(ByteUtils.bytesToString(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = ByteUtils.bytesToShort(Arrays.copyOfRange(header, 20, 22));
        if (size > 0) {
            setNonce(ByteUtils.bytesToString(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = header[22];
        if (size > 0) {
            setDifficultyTarget(ByteUtils.bytesToInteger(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = header[23];
        if (size > 0) {
            setInfoType(ByteUtils.bytesToString(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = header[24];
        if (size > 0) {
            setInfoFormat(ByteUtils.bytesToString(Arrays.copyOfRange(data, pos, pos + size)));
            pos += size;
        }

        size = ByteUtils.bytesToInteger(Arrays.copyOfRange(header, 25, 29));
        if (size > 0) {
            setContextData(Arrays.copyOfRange(data, pos, pos + size));
            pos += size;
        }

        size = ByteUtils.bytesToInteger(Arrays.copyOfRange(header, 29, 33));
        if (size > 0) {
            setContentData(Arrays.copyOfRange(data, pos, pos + size));
        }
    }

    @Override
    public String toString() {
        return "IOMessage{ " + toJSON().toString() + " }";
    }

}
