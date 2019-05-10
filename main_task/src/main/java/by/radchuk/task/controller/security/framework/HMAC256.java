package by.radchuk.task.controller.security.framework;

import lombok.SneakyThrows;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HMAC256  implements Algorithm {
    /**
     * algorithm secret.
     */
    private static final byte[] SECRET_BYTES = new byte[]{-14, -12, -109, 98, -12, 2, 87, 126, 32, 85, -78, 120, -5, -72, -15, -56, -53, 7, 61, 52, 127, -20, -38, 91, -22, 32, -83, 55, 0, -89, -74, 11};
    private static final byte JWT_PART_SEPARATOR = (byte)46;
    private static final String ALGORITHM = "HmacSHA256";
    private static final HMAC256 INSTANCE = new HMAC256();

    public static HMAC256 getInstance() {
        return INSTANCE;
    }

    /**
     * Create signature for JWT header and payload.
     *
     * @param headerBytes JWT header.
     * @param payloadBytes JWT payload.
     * @return the signature bytes.
     * @throws NoSuchAlgorithmException if the algorithm is not supported.
     * @throws InvalidKeyException if the given key is inappropriate for initializing the specified algorithm.
     */
    @Override
    @SneakyThrows({NoSuchAlgorithmException.class, InvalidKeyException.class})
    public byte[] createSignature(byte[] headerBytes, byte[] payloadBytes) {
        final Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(new SecretKeySpec(SECRET_BYTES, ALGORITHM));
        mac.update(headerBytes);
        mac.update(JWT_PART_SEPARATOR);
        return mac.doFinal(payloadBytes);
    }

    /**
     * Verify signature for JWT header and payload.
     *
     * @param headerBytes JWT header.
     * @param payloadBytes JWT payload.
     * @param signatureBytes JWT signature.
     * @return true if signature is valid.
     * @throws NoSuchAlgorithmException if the algorithm is not supported.
     * @throws InvalidKeyException if the given key is inappropriate for initializing the specified algorithm.
     */
    @Override
    public boolean verifySignature(byte[] headerBytes, byte[] payloadBytes, byte[] signatureBytes) {
        return MessageDigest.isEqual(createSignature(headerBytes, payloadBytes), signatureBytes);
    }

}
