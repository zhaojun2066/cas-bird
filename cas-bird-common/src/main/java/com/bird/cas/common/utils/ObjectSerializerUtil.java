package com.bird.cas.common.utils;

import java.io.*;

/**
 * @program:cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-14 16:38
 **/
public class ObjectSerializerUtil {

    /**
     * 序列化
     * @param obj
     * @return
     * @throws IOException
     */
    public static byte[] serialize(Object obj) throws IOException {
        byte[] bytes;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            bytes = baos.toByteArray();
        } finally {
            if(null != oos) {
                oos.close();
            }
            if(null != baos) {
                baos.close();
            }
        }
        return bytes;
    }

    /**
     * 反序列化
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        Object obj;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } finally {
            if(null != ois) {
                ois.close();
            }
            if(null != bais) {
                bais.close();
            }
        }
        return obj;
    }
}
