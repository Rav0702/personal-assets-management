package allcount.poc.openbankingoauth.library;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;

/**
 * This class is responsible for generating a random code verifier and a code challenge based on the code verifier.
 */
public class CodeVerifierLibrary {

    public static final int CODE_VERIFIER_BYTE_LENGTH = 32;
    public static final String ALGORITHM_SHA_256 = "SHA-256";
    public static final int OFFSET_DEFAULT = 0;

    /**
     * Generates a random Base64 encoded code verifier.
     *
     * @return the generated code verifier
     */
    public static String generateRandomCodeVerifier() {
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[CODE_VERIFIER_BYTE_LENGTH];
        sr.nextBytes(code);

        return Base64.encodeBase64URLSafeString(code); //java.util.Base64.getEncoder().encodeToString(code);
    }

    /**
     * Produces a code challenge from a code verifier, to be hashed with SHA-256 and encode it with Base64 to be URL safe.
     *
     * @param codeVerifier the code verifier
     * @return the generated code challenge
     */
    public static String generateCodeChallenge(String codeVerifier) {
        try {
            byte[] bytes = codeVerifier.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance(ALGORITHM_SHA_256);
            md.update(bytes, OFFSET_DEFAULT, bytes.length);
            byte[] digest = md.digest();

            return Base64.encodeBase64URLSafeString(digest);

        } catch (NoSuchAlgorithmException e2) {
            System.out.println("Wrong algorithm to encode: " + e2);

            return null;
        }
    }
}
