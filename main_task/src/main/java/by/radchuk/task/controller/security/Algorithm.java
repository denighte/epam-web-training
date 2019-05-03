package by.radchuk.task.controller.security;

public interface Algorithm {
    byte[] createSignature(byte[] headerBytes, byte[] payloadBytes);
    boolean verifySignature(byte[] headerBytes, byte[] payloadBytes, byte[] signatureBytes);
}
