package cn.daxpay.open.payment.wx.dao;

import cn.daxpay.open.payment.wx.entity.WxChannelAppCapability;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 通道商户微信应用能力绑定 Manager
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class WxChannelAppCapabilityManager extends BaseManager<WxChannelAppCapabilityMapper, WxChannelAppCapability> {

    /// 根据通道商户号查询全部关联(按创建时间升序)
    public List<WxChannelAppCapability> listByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(WxChannelAppCapability::getChannelMchNo, channelMchNo)
                .orderByAsc(WxChannelAppCapability::getCreateTime)
                .orderByAsc(WxChannelAppCapability::getId)
                .list();
    }

    /// 按通道商户号 + 能力 + 档位查询单条绑定
    public Optional<WxChannelAppCapability> findByChannelMchNoAndCapabilityAndScope(
            String channelMchNo, String capability, String appScope) {
        return lambdaQuery()
                .eq(WxChannelAppCapability::getChannelMchNo, channelMchNo)
                .eq(WxChannelAppCapability::getCapability, capability)
                .eq(WxChannelAppCapability::getAppScope, appScope)
                .oneOpt();
    }

    /// 根据通道商户号删除全部关联(批量保存时先清后插)
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(WxChannelAppCapability::getChannelMchNo, channelMchNo)
                .remove();
    }

    /// 是否存在指向该主数据的通道绑定
    public boolean existsByScopeAndRefId(String appScope, Long wxAppRefId) {
        return lambdaQuery()
                .eq(WxChannelAppCapability::getAppScope, appScope)
                .eq(WxChannelAppCapability::getWxAppRefId, wxAppRefId)
                .exists();
    }

    /// 按档位与主数据主键删除绑定(主数据清理时用)
    public void deleteByScopeAndRefId(String appScope, Long wxAppRefId) {
        lambdaUpdate()
                .eq(WxChannelAppCapability::getAppScope, appScope)
                .eq(WxChannelAppCapability::getWxAppRefId, wxAppRefId)
                .remove();
    }
}
