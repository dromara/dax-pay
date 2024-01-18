package cn.bootx.platform.daxpay.service.core.system.config.dao;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.system.config.entity.PayChannelConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author xxm
 * @since 2024/1/8
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayChannelConfigManager extends BaseManager<PayChannelConfigMapper, PayChannelConfig> {

    /**
     * 根据code查询
     */
    public Optional<PayChannelConfig> findByCode(String code){
        return findByField(PayChannelConfig::getCode, code);
    }

    /**
     * 查询全部
     */
    @Override
    public List<PayChannelConfig> findAll() {
        return lambdaQuery().orderByAsc(MpIdEntity::getId).list();
    }
}
