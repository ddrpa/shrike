package cc.ddrpa.shrike.showcase.springboot;

import cc.ddrpa.shrike.core.Chirping;
import cc.ddrpa.shrike.signature.Habitat;
import com.google.common.io.BaseEncoding;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicController {
    /**
     * 展示第三方在发起请求时，如何计算签名
     *
     * @return
     */
    @PostMapping("/signature")
    public Object signatureShowcase(@RequestBody Map<String, String> whatever) {
        var appSecret = Base64.getDecoder().decode(whatever.get("appSecret"));

        var endpoint = "/public-api/v1/general/reporting/os-civil-archives-label";
        var timestamp = Chirping.timestamp();
        var payload = "[{\"area\":\"002010\",\"event_cnt\":3,\"event_sum\":4}]";

        Pair<byte[], BigInteger> signKeyAndNoncePair = Habitat.calculateSignatureKey(appSecret, null);
        var signature = Habitat.calculateSignature(signKeyAndNoncePair.getLeft(), endpoint, timestamp, payload);

        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("appKey", whatever.get("appKey"));
        resultMap.put("endpoint", endpoint);
        resultMap.put("timestamp", timestamp);
        resultMap.put("payload", payload);
        resultMap.put("nonce", signKeyAndNoncePair.getRight().toString());
        resultMap.put("signature", BaseEncoding.base16().lowerCase().encode(signature));

        return resultMap;
    }
}