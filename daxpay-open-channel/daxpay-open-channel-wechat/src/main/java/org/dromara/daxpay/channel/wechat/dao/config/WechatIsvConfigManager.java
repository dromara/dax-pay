package org.dromara.daxpay.channel.wechat.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.channel.wechat.entity.config.WechatIsvConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 微信服务商配置Manager
 * @author xxm
 * @since 2024/12/27
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class WechatIsvConfigManager extends BaseManager<WechatIsvConfigMapper, WechatIsvConfig> {

    /**
     * 根据服务商号查询
     */
    public Optional<WechatIsvConfig> findByIsvNo(String isvNo) {
        return this.findByField(WechatIsvConfig::getIsvNo, isvNo);
    }
}
