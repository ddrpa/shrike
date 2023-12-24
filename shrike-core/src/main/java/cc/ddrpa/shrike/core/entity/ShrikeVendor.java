package cc.ddrpa.shrike.core.entity;

import cc.ddrpa.shrike.core.Chirping;

import java.io.Serial;
import java.io.Serializable;

/**
 * 第三方服务
 */
public class ShrikeVendor implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 应用的 AppKey，这是一个公开信息
    protected String appKey;
    // 应用的 AppSecret，除了第一次展示给用户，不应该可以再次获取
    protected byte[] appSecret;
    // 应用的描述信息
    protected String description;
    // 应用是否启用
    protected Boolean enabled;

    public ShrikeVendor() {
    }

    public ShrikeVendor(String description) {
        this.appKey = Chirping.nextAppKey();
        this.appSecret = Chirping.nextAppSecret();
        this.description = description;
        this.enabled = true;
    }

    public ShrikeVendor newAppSecret() {
        this.appSecret = Chirping.nextAppSecret();
        return this;
    }

    public String getAppKey() {
        return appKey;
    }

    public ShrikeVendor setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public byte[] getAppSecret() {
        return appSecret;
    }


    public String getDescription() {
        return description;
    }

    public ShrikeVendor setDescription(String description) {
        this.description = description;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public ShrikeVendor setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}