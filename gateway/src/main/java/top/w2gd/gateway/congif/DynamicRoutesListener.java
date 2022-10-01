package top.w2gd.gateway.congif;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author w2gd
 */
@Slf4j
@Component
public class DynamicRoutesListener implements Listener {

    @Resource
    private  GatewayService gatewayService;

    @Override
    public Executor getExecutor() {
        log.info("getException");
        return null;
    }

    /**
     * 使用 JSON 转换，将 plan text 变为RouteDefinition
     * @param configInfo 配置信息
     */
    @Override
    public void receiveConfigInfo(String configInfo) {
        log.info("received routes changes {}", configInfo);
        // 接受远程JSON字符串， 获取路由规则, (读远程 yml 文件把这两行注释掉，读JSON文件则打开)
        // List<RouteDefinition> definitionList = JSON.parseArray(configInfo,RouteDefinition.class);
        // gatewayService.updateRoutes(definitionList);

    }
}
