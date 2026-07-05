package cn.daxpay.open.channel.wechat.dao.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchAppCapability;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/// # 微信服务商通道商户应用支付能力关联 Manager
///
/// 关联记录数据访问管理器,提供按通道商户号查询/删除、按能力查应用、按应用清理等方法。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvMchAppCapabilityManager extends BaseManager<WechatIsvMchAppCapabilityMapper, WechatIsvMchAppCapability> {

    /// 根据通道商户号查询全部关联(按创建时间升序)
    public List<WechatIsvMchAppCapability> listByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(WechatIsvMchAppCapability::getChannelMchNo, channelMchNo)
                .orderByAsc(WechatIsvMchAppCapability::getCreateTime)
                .orderByAsc(WechatIsvMchAppCapability::getId)
                .list();
    }

    /// 根据通道商户号与支付能力查询单条关联(支付时调用)
    public Optional<WechatIsvMchAppCapability> findOne(String channelMchNo, String capability) {
        return lambdaQuery()
                .eq(WechatIsvMchAppCapability::getChannelMchNo, channelMchNo)
                .eq(WechatIsvMchAppCapability::getCapability, capability)
                .oneOpt();
    }

    /// 根据通道商户号删除全部关联(批量保存时先清后插)
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(WechatIsvMchAppCapability::getChannelMchNo, channelMchNo)
                .remove();
    }

    /// 根据应用ID删除关联(应用被删除时级联清理,避免悬空引用)
    public void deleteByWechatIsvMchAppId(Long wechatIsvMchAppId) {
        lambdaUpdate()
                .eq(WechatIsvMchAppCapability::getWechatIsvMchAppId, wechatIsvMchAppId)
                .remove();
    }
}
