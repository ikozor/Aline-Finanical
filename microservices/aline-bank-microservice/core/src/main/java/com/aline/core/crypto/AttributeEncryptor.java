package com.aline.core.crypto;

import com.aline.core.config.AppConfig;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * The Attribute Encryptor encrypts a property
 * when being persisted into a database and then
 * decrypted when reading from the database.
 *
 * @apiNote This API has a special case. If the database column
 *          is an SSN column and the column data is already in
 *          SSN format then it will just return that instead of
 *          a decrypted string. However, this should never happen
 *          and this behavior was only taken into account when
 *          running integration tests as the mock data does not
 *          include an encrypted SSN.
 */
@Converter
@Slf4j(topic = "Attribute Encryptor")
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private static final String AES = "AES";

    private final Key key;
    private final Cipher cipher;

    public AttributeEncryptor(AppConfig config) throws NoSuchPaddingException, NoSuchAlgorithmException {
        key = new SecretKeySpec(config.getSecurity().getSecretKey().getBytes(), AES);
        cipher = Cipher.getInstance(AES);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null)
            return null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;

        // If the database data is already in SSN format
        // return it instead of decrypting it.
        // (This behavior should only happen in tests.)
        if (dbData.matches("^\\d{3}-\\d{2}-\\d{4}$"))
            return dbData;

        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
}
