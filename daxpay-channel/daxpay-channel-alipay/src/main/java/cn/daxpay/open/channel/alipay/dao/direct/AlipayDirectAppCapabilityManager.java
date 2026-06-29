package cn.daxpay.open.channel.alipay.dao.direct;

import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppCapability;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/// # 支付宝直连商户应用支付能力关联
///
/// 关联记录数据访问管理器，提供按通道商户号查询/删除、按能力查应用、按应用清理等方法。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectAppCapabilityManager extends BaseManager<AlipayDirectAppCapabilityMapper, AlipayDirectAppCapability> {

    /// 根据通道商户号查询全部关联(按创建时间升序)
    public List<AlipayDirectAppCapability> listByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(AlipayDirectAppCapability::getChannelMchNo, channelMchNo)
                .orderByAsc(AlipayDirectAppCapability::getCreateTime)
                .orderByAsc(AlipayDirectAppCapability::getId)
                .list();
    }

    /// 根据通道商户号与支付能力查询单条关联(支付时调用)
    public Optional<AlipayDirectAppCapability> findOne(String channelMchNo, String capability) {
        return lambdaQuery()
                .eq(AlipayDirectAppCapability::getChannelMchNo, channelMchNo)
                .eq(AlipayDirectAppCapability::getCapability, capability)
                .oneOpt();
    }

    /// 根据通道商户号删除全部关联(批量保存时先清后插)
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(AlipayDirectAppCapability::getChannelMchNo, channelMchNo)
                .remove();
    }

    /// 根据应用ID删除关联(应用被删除时级联清理，避免悬空引用)
    public void deleteByAlipayDirectAppId(Long alipayDirectAppId) {
        lambdaUpdate()
                .eq(AlipayDirectAppCapability::getAlipayDirectAppId, alipayDirectAppId)
                .remove();
    }
}
