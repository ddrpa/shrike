package cc.ddrpa.shrike.showcase.springboot;


import cc.ddrpa.shrike.core.ShrikeVendorService;
import cc.ddrpa.shrike.core.entity.ShrikeVendor;
import cc.ddrpa.shrike.signature.Habitat;
import com.google.common.io.BaseEncoding;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class InternalController {
    private final ShrikeVendorService shrikeVendorService;

    InternalController(ShrikeVendorService shrikeVendorService) {
        this.shrikeVendorService = shrikeVendorService;
    }

    /**
     * 注册应用
     *
     * @return
     */
    @PostMapping("/register-vendor")
    public ShrikeVendor registerVendor() {
        return shrikeVendorService.register("add at " + LocalDateTime.now());
    }

    /**
     * 列出所有应用
     *
     * @return
     */
    @GetMapping("/list-vendor")
    public List<ShrikeVendor> listVendors() {
        return shrikeVendorService.list();
    }

    /**
     * 重置密钥
     *
     * @param appKey
     * @return
     */
    @PostMapping("/reset-secret/{app_key}")
    public ShrikeVendor resetAppSecret(@PathVariable("app_key") String appKey) {
        return shrikeVendorService.resetAppSecret(appKey).orElseThrow();
    }

    /**
     * 启用
     *
     * @param appKey
     */
    @PostMapping("/enable-vendor/{app_key}")
    public void enableVendor(@PathVariable("app_key") String appKey) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not implemented yet");
    }

    /**
     * 停用
     *
     * @param appKey
     */
    @PostMapping("/disable-vendor/{app_key}")
    public void disableVendor(@PathVariable("app_key") String appKey) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not implemented yet");
    }

    /**
     * 身份验证
     *
     * @param whatever
     * @return
     */
    @PostMapping("/authenticate")
    public Boolean authenticate(@RequestBody Map<String, String> whatever) {
        var appKey = whatever.get("appKey");
        var appSecret = Base64.getDecoder().decode(whatever.get("appSecret"));
        return shrikeVendorService.authentication(appKey, appSecret);
    }

    /**
     * 验证签名
     *
     * @param whatever
     * @return
     */
    @PostMapping("/validate-signature")
    public Boolean validateSignature(@RequestBody Map<String, String> whatever) {
        var appKey = whatever.get("appKey");
        var nonce = new BigInteger(whatever.get("nonce"));
        var timestamp = whatever.get("timestamp");

        var payload = "[{\"area\":\"002010\",\"event_cnt\":3,\"event_sum\":4}]";
        var endpoint = "/public-api/v1/general/reporting/os-civil-archives-label";

        var signKey = shrikeVendorService.getSignatureKey(appKey, nonce);
        byte[] signed = Habitat.calculateSignature(signKey, endpoint, timestamp, payload);

        var receivedSignature = BaseEncoding.base16().lowerCase().decode(whatever.get("signature"));
        return Arrays.equals(signed, receivedSignature);
    }
}