package crypto;

import crypto.exceptions.CiphertextException;
import crypto.exceptions.KeyNotLoadedException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.security.InvalidKeyException;

/**
 * A wrapper for basic cryptographic functionality found in Java APIs, e.g.
 * javax.crypto, as well as in third party APIs, such as org.bouncycastle.crypto
 *
 * Created by Matthew Kindy II and Matt Bernhard on 11/3/2014.
 */
public interface ICryptoType extends Serializable {

    /**
     * Decrypts a cipherText generated by this ICryptoType.
     *
     * @param cipherText    a byte array to be decrypted
     * @return              the plaintext decrypted from the ciphertext
     *
     * @throws java.security.InvalidKeyException
     * @throws CipherException
     * @throws crypto.exceptions.KeyNotLoadedException
     */
    public <T extends AHomomorphicCiphertext> byte[] decrypt(T cipherText) throws InvalidKeyException, KeyNotLoadedException, CipherException, CiphertextException;

    /**
     * Encrypts a plainText according to the ICryptoType's protocol.
     *
     * @param plainText     a byte array to be encrypted
     * @return              the ciphertext of the plaintext according to the protocol
     *
     * @throws java.security.InvalidKeyException
     * @throws CipherException
     * @throws crypto.exceptions.KeyNotLoadedException
     */
    public <T extends AHomomorphicCiphertext> T encrypt(byte[] plainText) throws InvalidKeyException, KeyNotLoadedException, CipherException;

    /**
     * Given a filePath, extracts the keys from the file
     *
     * @param filePaths      a String array specifying the locations of the keys to be loaded
     *
     * @throws java.io.FileNotFoundException
     */
    public void loadAllKeys(String[] filePaths) throws FileNotFoundException;


    public String toString();

}
