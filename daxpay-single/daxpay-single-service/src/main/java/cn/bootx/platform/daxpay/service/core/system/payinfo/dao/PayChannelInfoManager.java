package cn.bootx.platform.daxpay.service.core.system.payinfo.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.system.payinfo.entity.PayChannelInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * @author xxm
 * @since 2024/1/8
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayChannelInfoManager extends BaseManager<PayChannelInfoMapper, PayChannelInfo> {

    /**
     * 根据code查询
     */
    public Optional<PayChannelInfo> findByCode(String code){
        return findByField(PayChannelInfo::getCode, code);
    }
}
