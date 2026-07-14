package cn.daxpay.open.channel.douyin.dao.direct;

import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectApp;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 抖音直连商户应用
///
/// - 配置态 CRUD: [#listByMchNoAndChannelMchNo]、[#existsByChannelMchNoAndDouyinAppId]
/// - 支付/回调（已装载 mchNo）: 租户内 [#findFirstByChannelMchNo]、[#findFirstByChannelMchNoAndAppType]
///
@Repository
public class DouyinDirectAppManager extends BaseManager<DouyinDirectAppMapper, DouyinDirectApp> {

    /// 根据商户号和通道商户号查询应用列表（按创建时间升序）
    public List<DouyinDirectApp> listByMchNoAndChannelMchNo(String mchNo, String channelMchNo) {
        return lambdaQuery()
                .eq(DouyinDirectApp::getMchNo, mchNo)
                .eq(DouyinDirectApp::getChannelMchNo, channelMchNo)
                .orderByAsc(DouyinDirectApp::getCreateTime)
                .orderByAsc(DouyinDirectApp::getId)
                .list();
    }

    /// 校验同一通道商户下douyinAppId是否已存在(排除自身)
    public boolean existsByChannelMchNoAndDouyinAppId(String mchNo, String channelMchNo, String douyinAppId, Long excludeId) {
        return lambdaQuery()
                .eq(DouyinDirectApp::getMchNo, mchNo)
                .eq(DouyinDirectApp::getChannelMchNo, channelMchNo)
                .eq(DouyinDirectApp::getDouyinAppId, douyinAppId)
                .ne(excludeId != null, DouyinDirectApp::getId, excludeId)
                .exists();
    }

    /// 根据通道商户号取首个应用（支付/回调，租户内）
    public Optional<DouyinDirectApp> findFirstByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(DouyinDirectApp::getChannelMchNo, channelMchNo)
                .orderByAsc(DouyinDirectApp::getCreateTime)
                .orderByAsc(DouyinDirectApp::getId)
                .last("limit 1")
                .oneOpt();
    }

    /// 根据通道商户号与应用类型取首个应用（支付/回调，租户内）
    public Optional<DouyinDirectApp> findFirstByChannelMchNoAndAppType(String channelMchNo, String appType) {
        return lambdaQuery()
                .eq(DouyinDirectApp::getChannelMchNo, channelMchNo)
                .eq(DouyinDirectApp::getAppType, appType)
                .orderByAsc(DouyinDirectApp::getCreateTime)
                .orderByAsc(DouyinDirectApp::getId)
                .last("limit 1")
                .oneOpt();
    }

}
