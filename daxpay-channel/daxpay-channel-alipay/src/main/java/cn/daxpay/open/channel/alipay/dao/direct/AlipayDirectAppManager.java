package cn.daxpay.open.channel.alipay.dao.direct;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectApp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    /// 根据商户号查询首个直连应用(单应用场景, 按创建时间升序取第一条)
    public Optional<AlipayDirectApp> findFirstByMchNo(String mchNo) {
        return firstOpt(q -> q
                .eq(AlipayDirectApp::getMchNo, mchNo)
                .orderByAsc(AlipayDirectApp::getCreateTime)
                .orderByAsc(AlipayDirectApp::getId));
    }

    /// 根据通道商户号与应用类型查询首个应用(appType自动推导时使用, 按创建时间升序取第一条)
    public Optional<AlipayDirectApp> findFirstByChannelMchNoAndAppType(String channelMchNo, String appType) {
        return firstOpt(q -> q
                .eq(AlipayDirectApp::getChannelMchNo, channelMchNo)
                .eq(AlipayDirectApp::getAppType, appType)
                .orderByAsc(AlipayDirectApp::getCreateTime)
                .orderByAsc(AlipayDirectApp::getId));
    }

    /// 根据通道商户号查询首个应用(兜底回退使用, 按创建时间升序取第一条)
    public Optional<AlipayDirectApp> findFirstByChannelMchNo(String channelMchNo) {
        return firstOpt(q -> q
                .eq(AlipayDirectApp::getChannelMchNo, channelMchNo)
                .orderByAsc(AlipayDirectApp::getCreateTime)
                .orderByAsc(AlipayDirectApp::getId));
    }
}
