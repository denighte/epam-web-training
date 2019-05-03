package by.radchuk.task.controller.security;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@AllArgsConstructor
public class JWT {
    private static final byte JWT_PART_SEPARATOR = (byte)46;
    @Getter
    @NonNull private Algorithm algorithm;
    @NonNull private JsonObject headerJson;
    @NonNull private JsonObject payloadJson;

    public static Encoder encoder() {
        return new Encoder(HMAC256.getInstance());
    }

    public static Decoder decoder() {
        return new Decoder(HMAC256.getInstance());
    }

    public static class Encoder {
        private Algorithm algorithm;
        private JsonObject headerJson = new JsonObject();
        private JsonObject payloadJson = new JsonObject();;

        private Encoder(Algorithm alg) {
            algorithm = alg;
        }

        public Encoder withHeader(String property, String value) {
            headerJson.addProperty(property, value);
            return this;
        }

        public Encoder withClaim(String property, Character value) {
            payloadJson.addProperty(property, value);
            return this;
        }

        public Encoder withClaim(String property, String value) {
            payloadJson.addProperty(property, value);
            return this;
        }

        public Encoder withClaim(String property, Number value) {
            payloadJson.addProperty(property, value);
            return this;
        }

        public Encoder withClaim(String property, Boolean value) {
            payloadJson.addProperty(property, value);
            return this;
        }

        @SneakyThrows({UnsupportedEncodingException.class})
        private String sign() throws IOException {
            Gson gson = new Gson();
            byte[] header = Base64.getEncoder().encode(gson.toJson(headerJson).getBytes(StandardCharsets.UTF_8));
            byte[] payload = Base64.getEncoder().encode(gson.toJson(payloadJson).getBytes(StandardCharsets.UTF_8));

            byte[] signatureBytes = algorithm.createSignature(header, payload);
            byte[] signature = Base64.getEncoder().encode((signatureBytes));
            ByteArrayOutputStream token = new ByteArrayOutputStream(header.length + payload.length + signature.length + 2);
            token.write(header);
            token.write(JWT_PART_SEPARATOR);
            token.write(payload);
            token.write(JWT_PART_SEPARATOR);
            token.write(signature);
            return token.toString("UTF-8");
        }
    }

    public static class Decoder {
        private Algorithm algorithm;
        private byte[] header;
        private byte[] payload;
        private byte[] signature;
        private boolean isValid;

        private Decoder(Algorithm alg) {
            algorithm = alg;
            isValid = false;
        }

        public boolean verify(String token) {
            try {
                String[] parts = splitToken(token);
                header = Base64.getDecoder().decode(parts[0]);
                payload = Base64.getDecoder().decode(parts[1]);
                signature = Base64.getDecoder().decode(parts[2]);
                isValid = algorithm.verifySignature(header, payload, signature);
                return isValid;
            } catch (JWTDecodeException exception) {
                return false;
            }
        }

        public JWT decode(String token) throws JWTDecodeException {
            if (header == null) {
                verify(token);
            }
            if (!isValid) {
                throw new JWTDecodeException("JWT is invalid.");
            }
            JsonParser jsonParser = new JsonParser();
            String[] parts = splitToken(token);
            JsonObject headerJson = jsonParser.parse(new String(header, StandardCharsets.UTF_8)).getAsJsonObject();
            JsonObject payloadJson = jsonParser.parse(new String(payload, StandardCharsets.UTF_8)).getAsJsonObject();
            return new JWT(algorithm, headerJson, payloadJson);
        }

        private String[] splitToken(String token) throws JWTDecodeException {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new JWTDecodeException(String.format("The token was expected to have 3 parts, but got %s.", parts.length));
            }
            return parts;
        }
    }

    public static class JWTDecodeException extends Exception {
        /**
         * Constructs a new exception with {@code null} as its detail message.
         * The cause is not initialized, and may subsequently be initialized by a
         * call to {@link #initCause}.
         */
        public JWTDecodeException() {
            super();
        }

        /**
         * Constructs a new exception with the specified detail message.  The
         * cause is not initialized, and may subsequently be initialized by
         * a call to {@link #initCause}.
         *
         * @param   message   the detail message. The detail message is saved for
         *          later retrieval by the {@link #getMessage()} method.
         */
        public JWTDecodeException(String message) {
            super(message);
        }

        /**
         * Constructs a new exception with the specified detail message and
         * cause.  <p>Note that the detail message associated with
         * {@code cause} is <i>not</i> automatically incorporated in
         * this exception's detail message.
         *
         * @param  message the detail message (which is saved for later retrieval
         *         by the {@link #getMessage()} method).
         * @param  cause the cause (which is saved for later retrieval by the
         *         {@link #getCause()} method).  (A <tt>null</tt> value is
         *         permitted, and indicates that the cause is nonexistent or
         *         unknown.)
         */
        public JWTDecodeException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static void main(String[] argv) throws Exception {
        // Change encoding logic
        var builder = JWT.encoder();
        var token = builder.withClaim("num", 111).sign();
        var jwt = JWT.decoder().decode(token);
        System.out.println(jwt.payloadJson);
    }

}
