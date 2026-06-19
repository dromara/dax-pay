package cn.daxpay.open.channel.douyin.dao.direct;

import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectApp;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 抖音直连商户应用
///
/// 直连商户应用数据访问管理器，提供按商户号和通道商户号查询列表、同一通道下应用ID唯一性校验等方法。
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
}
