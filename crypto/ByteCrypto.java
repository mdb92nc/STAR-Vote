package crypto;

import crypto.exceptions.BadKeyException;
import crypto.exceptions.KeyNotLoadedException;
import crypto.exceptions.UninitialisedException;

import java.io.FileNotFoundException;
import java.security.InvalidKeyException;

/**
 * A crypto class used as a black box operating over byte arrays performing
 * cryptographic functions. Behaviour depends on a specified cryptographic
 * protocol contained within the cryptoType field which is set upon construction of
 * BallotCrypto.
 *
 * Created by Matthew Kindy II and Matt Bernhard on 11/3/2014.
 */
public class ByteCrypto {

    private ICryptoType cryptoType;

    public ByteCrypto(ICryptoType cryptoType){
        this.cryptoType = cryptoType;
    }

    public byte[] decrypt(byte[] cipherText) throws UninitialisedException, KeyNotLoadedException, InvalidKeyException, CipherException {

       return cryptoType.decrypt(cipherText);
    }

    public byte[] encrypt(byte[] plainText) throws UninitialisedException, KeyNotLoadedException, InvalidKeyException, CipherException {

        return cryptoType.encrypt(plainText);
    }

    public void loadKeys(String... filePaths) throws FileNotFoundException, BadKeyException, UninitialisedException {

         cryptoType.loadKeys(filePaths);
    }

    public String toString() {
        return "CryptoType: " + cryptoType.toString();
    }

}
