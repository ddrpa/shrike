package cc.ddrpa.shrike.spring.autoconfigure;

import cc.ddrpa.shrike.core.ShrikeVendorService;
import cc.ddrpa.shrike.mybatisplus.ShrikeVendorServiceImpl;
import cc.ddrpa.shrike.mybatisplus.mapper.ShrikeVendorMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("cc.ddrpa.shrike.mybatisplus.mapper")
@EnableConfigurationProperties(ShrikeProperties.class)
public class ShrikeAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    ShrikeVendorService shrikeVendorService(ShrikeVendorMapper vendorMapper) {
        return new ShrikeVendorServiceImpl(vendorMapper);
    }
}