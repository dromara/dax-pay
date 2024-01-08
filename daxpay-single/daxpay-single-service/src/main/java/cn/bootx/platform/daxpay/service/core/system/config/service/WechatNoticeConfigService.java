package cn.bootx.platform.daxpay.service.core.system.config.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.service.core.system.config.entity.WechatNoticeConfig;
import cn.bootx.platform.daxpay.service.core.system.config.dao.WechatNoticeConfigManager;
import cn.bootx.platform.daxpay.service.param.system.config.WechatNoticeConfigParam;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信消息通知相关配置
 * @author xxm
 * @since 2024/1/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatNoticeConfigService {
    /** 默认配置ID */
    private static final long ID = 0L;

    private final WechatNoticeConfigManager wechatNoticeConfigManager;;

    /**
     * 获取支付平台配置
     */
    public WechatNoticeConfig getConfig(){
        return wechatNoticeConfigManager.findById(ID).orElseThrow(() -> new DataNotExistException("微信消息配置不存在"));
    }

    /**
     * 更新配置
     */
    public void update(WechatNoticeConfigParam param){
        WechatNoticeConfig config = wechatNoticeConfigManager.findById(ID).orElseThrow(() -> new DataNotExistException("微信消息配置不存在"));
        BeanUtil.copyProperties(param,config);
        wechatNoticeConfigManager.updateById(config);
    }
}
