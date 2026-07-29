package cn.daxpay.open.payment.douyin.dao.channel;

import cn.daxpay.open.payment.douyin.entity.channel.DyChannelAppCapability;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 通道商户抖音应用能力绑定 Manager
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class DyChannelAppCapabilityManager extends BaseManager<DyChannelAppCapabilityMapper, DyChannelAppCapability> {

    /// 根据通道商户号查询全部关联(按创建时间升序)
    public List<DyChannelAppCapability> listByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(DyChannelAppCapability::getChannelMchNo, channelMchNo)
                .orderByAsc(DyChannelAppCapability::getCreateTime)
                .orderByAsc(DyChannelAppCapability::getId)
                .list();
    }

    /// 按通道商户号 + 能力 + 档位查询单条绑定
    public Optional<DyChannelAppCapability> findByChannelMchNoAndCapabilityAndScope(
            String channelMchNo, String capability, String appScope) {
        return lambdaQuery()
                .eq(DyChannelAppCapability::getChannelMchNo, channelMchNo)
                .eq(DyChannelAppCapability::getCapability, capability)
                .eq(DyChannelAppCapability::getAppScope, appScope)
                .oneOpt();
    }

    /// 根据通道商户号删除全部关联(批量保存时先清后插)
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(DyChannelAppCapability::getChannelMchNo, channelMchNo)
                .remove();
    }

    /// 是否存在指向该主数据的通道绑定
    public boolean existsByScopeAndRefId(String appScope, Long dyAppRefId) {
        return lambdaQuery()
                .eq(DyChannelAppCapability::getAppScope, appScope)
                .eq(DyChannelAppCapability::getDyAppRefId, dyAppRefId)
                .exists();
    }

    /// 按档位与主数据主键删除绑定(主数据清理时用)
    public void deleteByScopeAndRefId(String appScope, Long dyAppRefId) {
        lambdaUpdate()
                .eq(DyChannelAppCapability::getAppScope, appScope)
                .eq(DyChannelAppCapability::getDyAppRefId, dyAppRefId)
                .remove();
    }
}
