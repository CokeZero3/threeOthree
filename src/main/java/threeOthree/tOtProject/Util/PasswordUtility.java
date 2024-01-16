package threeOthree.tOtProject.Util;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@Component
public class PasswordUtility {

    private final AesBytesEncryptor encryptor;

    public boolean checkEncoding(String decode, String encode){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(decode, encode);
    }

    public String encrpyt(String plainString){
        byte[] encrypt = encryptor.encrypt(plainString.getBytes(StandardCharsets.UTF_8));
        return byteArrayToString(encrypt);

    }
    public String decrypt(String decryptString) {
        byte[] decryptBytes = stringToByteArray(decryptString);
        byte[] decrypt = encryptor.decrypt(decryptBytes);
        return new String(decrypt, StandardCharsets.UTF_8);
    }

    public String byteArrayToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte abyte :bytes){
            sb.append(abyte);
            sb.append(" ");
        }
        return sb.toString();
    }

    public byte[] stringToByteArray(String byteString) {
        String[] split = byteString.split("\\s");
        ByteBuffer buffer = ByteBuffer.allocate(split.length);
        for (String s : split) {
            buffer.put((byte) Integer.parseInt(s));
        }
        return buffer.array();
    }
}
