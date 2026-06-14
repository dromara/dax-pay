package org.dromara.daxpay.channel.alipay.dao.direct;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectApp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 支付宝直连商户应用
///
/// 直连商户应用数据访问管理器，提供按商户号和通道商户号查询列表、同一通道下应用ID唯一性校验等方法。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectAppManager extends BaseManager<AlipayDirectAppMapper, AlipayDirectApp> {

    /// 根据商户号和通道商户号查询应用列表（按创建时间升序）
    public List<AlipayDirectApp> listByMchNoAndChannelMchNo(String mchNo, String channelMchNo) {
        return lambdaQuery()
                .eq(AlipayDirectApp::getMchNo, mchNo)
                .eq(AlipayDirectApp::getChannelMchNo, channelMchNo)
                .orderByAsc(AlipayDirectApp::getCreateTime)
                .orderByAsc(AlipayDirectApp::getId)
                .list();
    }

    /// 校验同一通道商户下aliAppId是否已存在(排除自身)
    public boolean existsByChannelMchNoAndAliAppId(String mchNo, String channelMchNo, String aliAppId, Long excludeId) {
        return lambdaQuery()
                .eq(AlipayDirectApp::getMchNo, mchNo)
                .eq(AlipayDirectApp::getChannelMchNo, channelMchNo)
                .eq(AlipayDirectApp::getAliAppId, aliAppId)
                .ne(excludeId != null, AlipayDirectApp::getId, excludeId)
                .exists();
    }
}
