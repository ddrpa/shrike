package cc.ddrpa.shrike.signature;

import cc.ddrpa.shrike.core.Chirping;
import cc.ddrpa.shrike.core.exception.ShrikeSignatureException;
import com.google.common.io.BaseEncoding;
import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Habitat {
    private Habitat() {
        throw new IllegalStateException("Utility class");
    }
    private static final String DEFAULT_SIGNATURE_ALGORITHM = "HMAC-SHA256";
    private static final String DEFAULT_HASH_ALGORITHM = "SHA-256";
    /**
     * 计算接口签名密钥
     * 如果是接口请求方，令 nullableNonce 为 null，可以自动生成一个 nonce 参与计算
     * 如果是验证签名，传入收到的 nonce
     *
     * @param appSecret   Vendor 的 AppSecret
     * @param nullableNonce nonce
     * @return 签名密钥和 nonce Pair
     * @throws ShrikeSignatureException
     */
    public static ImmutablePair<byte[], BigInteger> calculateSignatureKey(byte[] appSecret,
                                                                          BigInteger nullableNonce) throws ShrikeSignatureException {
        if (nullableNonce == null) {
            nullableNonce = BigInteger.valueOf(Chirping.nextSignatureNonce());
        }
        try {
            Mac hmac = Mac.getInstance(DEFAULT_SIGNATURE_ALGORITHM);
            hmac.init(new SecretKeySpec(appSecret, DEFAULT_SIGNATURE_ALGORITHM));
            byte[] signKey = hmac.doFinal(nullableNonce.toByteArray());
            return new ImmutablePair<>(signKey, nullableNonce);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new ShrikeSignatureException(e.getMessage());
        }
    }

    /**
     * 计算签名
     *
     * @param signatureKey 签名密钥
     * @param endpoint    接口端点
     * @param timestamp 时间戳
     * @param compressedPayloadAsString 压缩后的请求体
     * @return
     * @throws ShrikeSignatureException
     */
    public static byte[] calculateSignature(byte[] signatureKey,
                                            String endpoint,
                                            String timestamp,
                                            String compressedPayloadAsString) throws ShrikeSignatureException {
        // digest request payload with SHA256
        byte[] digest;
        byte[] signed;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(DEFAULT_HASH_ALGORITHM);
            digest = messageDigest.digest(compressedPayloadAsString.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new ShrikeSignatureException(e.getMessage());
        }
        // concatenate strings
        var concatenated = String.join(ShrikeConstant.SHRIKE_SIGNATURE_DELIMITER,
                ShrikeConstant.SHRIKE_SIGNATURE_ALGORITHM,
                endpoint,
                timestamp,
                BaseEncoding.base16().lowerCase().encode(digest));
        // signature
        try {
            Mac hmac = Mac.getInstance(DEFAULT_SIGNATURE_ALGORITHM);
            hmac.init(new SecretKeySpec(signatureKey, DEFAULT_SIGNATURE_ALGORITHM));
            signed = hmac.doFinal(concatenated.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new ShrikeSignatureException(e.getMessage());
        }
        return signed;
    }
}