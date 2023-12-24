package cc.ddrpa.shrike.core.entity;

import cc.ddrpa.shrike.core.Chirping;
import cc.ddrpa.shrike.core.enums.SymmetricEncryptionMethod;
import com.google.common.io.BaseEncoding;

import java.io.Serial;
import java.io.Serializable;

/**
 * 传递敏感信息使用的密钥
 */
public class ShrikeMessageSecret implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 应用
    private String appKey;
    // 密钥编号，AppKey + KeyId 具有唯一性
    private Integer keyId;
    // 密钥
    private byte[] key;
    // 加密方案
    private SymmetricEncryptionMethod method;

    public ShrikeMessageSecret() {
    }

    public ShrikeMessageSecret(String appKey, Integer keyId, String key, SymmetricEncryptionMethod method) {
        this.appKey = appKey;
        this.keyId = keyId;
        this.key = BaseEncoding.base16().lowerCase().decode(key);
        this.method = method;
    }

    /**
     * 创建新密钥
     *
     * @param appKey
     * @param keyId
     * @param method
     */
    public ShrikeMessageSecret(String appKey, Integer keyId, SymmetricEncryptionMethod method) {
        this.appKey = appKey;
        this.keyId = keyId;
        this.method = method;
        this.key = Chirping.nextSymmetricEncryptionKey(method);
    }
}