package top.w2gd.gateway.congif;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author w2gd
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DynamicRoutesLoader implements InitializingBean {
    @Resource
    private NacosConfigManager configManager;
    @Resource
    private NacosConfigProperties configProps;
    @Resource
    private DynamicRoutesListener dynamicRoutesListener;

    private static final String ROUTES_CONFIG = "gateway-service.yml";

    @Override
    public void afterPropertiesSet() throws Exception {
        // 首次加载配置
        String routes = configManager.getConfigService().getConfig(ROUTES_CONFIG,configProps.getGroup(),10000);
        dynamicRoutesListener.receiveConfigInfo(routes);

        // 注册监听器
        configManager.getConfigService().addListener(ROUTES_CONFIG,configProps.getGroup(),dynamicRoutesListener);
    }
}
