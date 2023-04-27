package br.com.core.utils.serialization;

import java.io.*;
import java.util.Base64;

public class GenericSerialization {

    public static String serializeToBase64(Object object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);

            byte[] bytes = byteArrayOutputStream.toByteArray();

            return Base64.getEncoder().encodeToString(bytes);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static Object deserializeFromBase64(String base64) {

        byte[] bytes = Base64.getDecoder().decode(base64);

        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {

            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
