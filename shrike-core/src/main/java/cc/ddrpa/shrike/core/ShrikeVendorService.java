package cc.ddrpa.shrike.core;


import cc.ddrpa.shrike.core.entity.ShrikeVendor;
import cc.ddrpa.shrike.core.exception.ShrikeSignatureException;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * 第三方服务商应用管理
 */
public interface ShrikeVendorService {
    /**
     * 添加应用
     * <p>
     * AppKey 和 AppSecret 是随机生成的
     *
     * @param description 应用描述
     * @return 应用详情
     */
    ShrikeVendor register(String description);

    /**
     * 列出所有应用，由于预计通常一个系统中的 Vendor 不会过多，所以暂时不做分页
     * List<ShrikeVendor> list(int pageNumber, int pageSize, boolean enabledOnly)
     *
     * @return
     */
    List<ShrikeVendor> list();

    /**
     * 重置指定应用的 Secret
     *
     * @param appKey 应用的 AppKey
     * @return 如果不存在该 AppKey 对应的应用，返回 Optional.empty
     */
    Optional<ShrikeVendor> resetAppSecret(String appKey);

    /**
     * 身份验证
     *
     * @param appKey   应用的 AppKey
     * @param appSecret 应用的 AppSecret
     * @return 是否验证通过，如果应用不存在或未启用，也返回 false
     */
    boolean authentication(String appKey, byte[] appSecret);

    /**
     * 计算签名密钥，如果应用不存在，返回 null
     *
     * @param appKey 应用的 AppKey
     * @param nonce 签名用 nonce
     * @return 签名密钥
     * @throws ShrikeSignatureException catch 加密算法相关的异常
     */
    byte[] getSignatureKey(String appKey, BigInteger nonce) throws ShrikeSignatureException;
}