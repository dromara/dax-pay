package cn.daxpay.open.channel.alipay.dao.direct;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectApp;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/// # 支付宝直连商户应用
///
/// - 配置态 CRUD: [#listByMchNoAndChannelMchNo]、[#existsByChannelMchNoAndAliAppId]、[#findFirstByMchNo]
/// - 支付/回调（已装载 mchNo）: 租户内 [#findFirstByChannelMchNo]、[#findFirstByChannelMchNoAndAppType]
/// - 认证引导: 方法名带 NotTenant
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

    /// 根据通道商户号与应用类型查询首个应用（支付/回调，租户内）
    public Optional<AlipayDirectApp> findFirstByChannelMchNoAndAppType(String channelMchNo, String appType) {
        return firstOpt(q -> q
                .eq(AlipayDirectApp::getChannelMchNo, channelMchNo)
                .eq(AlipayDirectApp::getAppType, appType)
                .orderByAsc(AlipayDirectApp::getCreateTime)
                .orderByAsc(AlipayDirectApp::getId));
    }

    /// 根据通道商户号查询首个应用（支付/回调，租户内）
    public Optional<AlipayDirectApp> findFirstByChannelMchNo(String channelMchNo) {
        return firstOpt(q -> q
                .eq(AlipayDirectApp::getChannelMchNo, channelMchNo)
                .orderByAsc(AlipayDirectApp::getCreateTime)
                .orderByAsc(AlipayDirectApp::getId));
    }

    /// 根据通道商户号与应用类型查询首个应用（认证引导，忽略租户）
    @IgnoreTenant
    public Optional<AlipayDirectApp> findFirstByChannelMchNoAndAppTypeNotTenant(String channelMchNo, String appType) {
        return findFirstByChannelMchNoAndAppType(channelMchNo, appType);
    }

    /// 根据通道商户号查询首个应用（认证引导，忽略租户）
    @IgnoreTenant
    public Optional<AlipayDirectApp> findFirstByChannelMchNoNotTenant(String channelMchNo) {
        return findFirstByChannelMchNo(channelMchNo);
    }

}
