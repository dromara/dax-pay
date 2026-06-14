package org.dromara.daxpay.channel.alipay.dao.app;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.channel.alipay.entity.app.AlipayMchApp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 支付宝直连商户应用
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayMchAppManager extends BaseManager<AlipayMchAppMapper, AlipayMchApp> {

    /// 根据商户号和通道商户号查询应用列表（按创建时间升序）
    public List<AlipayMchApp> listByMchNoAndChannelMchNo(String mchNo, String channelMchNo) {
        return lambdaQuery()
                .eq(AlipayMchApp::getMchNo, mchNo)
                .eq(AlipayMchApp::getChannelMchNo, channelMchNo)
                .orderByAsc(AlipayMchApp::getCreateTime)
                .orderByAsc(AlipayMchApp::getId)
                .list();
    }

    /// 校验同一通道商户下aliAppId是否已存在(排除自身)
    public boolean existsByChannelMchNoAndAliAppId(String mchNo, String channelMchNo, String aliAppId, Long excludeId) {
        return lambdaQuery()
                .eq(AlipayMchApp::getMchNo, mchNo)
                .eq(AlipayMchApp::getChannelMchNo, channelMchNo)
                .eq(AlipayMchApp::getAliAppId, aliAppId)
                .ne(excludeId != null, AlipayMchApp::getId, excludeId)
                .exists();
    }
}
