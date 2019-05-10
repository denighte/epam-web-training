package by.radchuk.task.controller.security.framework;

public interface Algorithm {
    byte[] createSignature(byte[] headerBytes, byte[] payloadBytes);
    boolean verifySignature(byte[] headerBytes, byte[] payloadBytes, byte[] signatureBytes);
}
