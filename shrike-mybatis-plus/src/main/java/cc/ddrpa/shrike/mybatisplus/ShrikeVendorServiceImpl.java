package cc.ddrpa.shrike.mybatisplus;

import cc.ddrpa.shrike.core.ShrikeVendorService;
import cc.ddrpa.shrike.core.entity.ShrikeVendor;
import cc.ddrpa.shrike.core.exception.ShrikeSignatureException;
import cc.ddrpa.shrike.mybatisplus.mapper.ShrikeVendorMapper;
import cc.ddrpa.shrike.signature.Habitat;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public class ShrikeVendorServiceImpl implements ShrikeVendorService {
    private static final Logger logger = LoggerFactory.getLogger(ShrikeVendorServiceImpl.class);

    private final ShrikeVendorMapper vendorMapper;

    public ShrikeVendorServiceImpl(ShrikeVendorMapper vendorMapper) {
        this.vendorMapper = vendorMapper;
    }

    @Override
    public ShrikeVendor register(String description) {
        ShrikeVendor shrikeVendor = new ShrikeVendor(description);
        vendorMapper.insert(shrikeVendor);
        logger.info("shrike:vendor:register:{}", shrikeVendor.getAppKey());
        return shrikeVendor;
    }

    @Override
    public List<ShrikeVendor> list() {
        return vendorMapper.selectList(Wrappers.<ShrikeVendor>lambdaQuery()
                .select(ShrikeVendor::getAppKey, ShrikeVendor::getDescription, ShrikeVendor::getEnabled));
    }

    @Override
    public Optional<ShrikeVendor> resetAppSecret(String appKey) {
        var vendors = vendorMapper.selectList(Wrappers.<ShrikeVendor>lambdaQuery()
                .eq(ShrikeVendor::getAppKey, appKey));
        if (vendors.size() != 1) {
            return Optional.empty();
        } else {
            var vendor = vendors.get(0);
            vendor.newAppSecret();
            vendorMapper.update(null, Wrappers.<ShrikeVendor>lambdaUpdate()
                    .eq(ShrikeVendor::getAppKey, appKey)
                    .set(ShrikeVendor::getAppSecret, vendor.getAppSecret()));
            logger.info("shrike:vendor:reset-app-secret:{}", appKey);
            return Optional.of(vendor);
        }
    }

    @Override
    public boolean authentication(String appKey, byte[] appSecret) {
        return vendorMapper.selectCount(Wrappers.<ShrikeVendor>lambdaQuery()
                .eq(ShrikeVendor::getEnabled, true)
                .eq(ShrikeVendor::getAppKey, appKey)
                .eq(ShrikeVendor::getAppSecret, appSecret)) > 0;
    }

    @Override
    public byte[] getSignatureKey(String appKey, BigInteger nonce) throws ShrikeSignatureException {
        var vendors = vendorMapper.selectList(Wrappers.<ShrikeVendor>lambdaQuery()
                .eq(ShrikeVendor::getEnabled, true)
                .eq(ShrikeVendor::getAppKey, appKey)
                .select(ShrikeVendor::getAppSecret));
        if (vendors.size() == 1) {
            return Habitat.calculateSignatureKey(vendors.get(0).getAppSecret(), nonce).getLeft();
        }
        return new byte[0];
    }
}