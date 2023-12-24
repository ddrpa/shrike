package cc.ddrpa.shrike.core;

import cc.ddrpa.shrike.core.enums.SymmetricEncryptionMethod;
import com.google.common.io.BaseEncoding;

import java.security.SecureRandom;

/**
 * 用于生成各类随机信息的工具类
 */
public class Chirping {
    private Chirping() {
        throw new IllegalStateException("Utility class");
    }

    private static final SecureRandom random = new SecureRandom();

    private static byte[] randomByteArray(int byteLength) {
        byte[] bytes = new byte[byteLength];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * 生成 Vendor 的 AppKey
     *
     * @return AppKey, 8 Bytes，使用十六进制小写字母表示
     */
    public static String nextAppKey() {
        return BaseEncoding.base16().lowerCase().encode(randomByteArray(8));
    }

    /**
     * 生成 Vendor 的 AppSecret
     *
     * @return AppSecret, 32 Bytes
     */
    public static byte[] nextAppSecret() {
        return randomByteArray(32);
    }

    /**
     * 生成签名用 nonce
     *
     * @return 整型
     */
    public static int nextSignatureNonce() {
        return random.nextInt(Integer.MAX_VALUE);
    }


    /**
     * 获取以秒为单位的时间戳，用于接口签名
     *
     * @return timestamp
     */
    public static String timestamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }


    /**
     * 生成对称加密密钥
     *
     * @param method 对称加密算法枚举
     * @return 密钥，长度由对应的加密算法指定
     */
    public static byte[] nextSymmetricEncryptionKey(SymmetricEncryptionMethod method) {
        switch (method) {
            case SM4:
                return randomByteArray(16);
            default:
                throw new IllegalArgumentException();
        }
    }
}