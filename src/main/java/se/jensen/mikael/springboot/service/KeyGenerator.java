package se.jensen.mikael.springboot.service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

/**
 * Hjälpklass för att generera ett RSA-nyckelpar (private & public key).
 * Används för att skapa nycklar som kan användas för JWT-signering i applikationen.
 * Körs som en vanlig Java-applikation och skriver ut Base64-kodade nycklar till konsolen.
 */
public class KeyGenerator {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        KeyPair kp = gen.generateKeyPair();

        System.out.println("PRIVATE_KEY:");
        System.out.println(Base64.getEncoder().encodeToString(
                kp.getPrivate().getEncoded()));

        System.out.println("\nPUBLIC_KEY:");
        System.out.println(Base64.getEncoder().encodeToString(
                kp.getPublic().getEncoded()));
    }
}

