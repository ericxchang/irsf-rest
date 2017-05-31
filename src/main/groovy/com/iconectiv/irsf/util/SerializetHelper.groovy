package com.iconectiv.irsf.util

/**
 * Created by echang on 5/31/2017.
 */
class SerializeHelper {
    static byte[] serialize(Object obj) {
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        ObjectOutputStream os = new ObjectOutputStream(out)
        os.writeObject(obj)
        return out.toByteArray()
    }

    static Object deserialize(byte[] data) {
        ByteArrayInputStream bin = new ByteArrayInputStream(data)
        ObjectInputStream is = new ObjectInputStream(bin)
        return is.readObject()
    }
}
