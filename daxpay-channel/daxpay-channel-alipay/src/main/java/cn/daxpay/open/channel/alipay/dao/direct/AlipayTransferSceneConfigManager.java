package cn.daxpay.open.channel.alipay.dao.direct;

import cn.daxpay.open.channel.alipay.entity.direct.AlipayTransferSceneConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/// # 支付宝转账场景配置
///
/// 转账场景配置数据访问管理器,提供按通道商户号查询列表、查询默认场景、清空默认等方法。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayTransferSceneConfigManager extends BaseManager<AlipayTransferSceneConfigMapper, AlipayTransferSceneConfig> {

    /// 按通道商户号查询全部场景配置(默认项排前, 启用排前)
    public List<AlipayTransferSceneConfig> listByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(AlipayTransferSceneConfig::getChannelMchNo, channelMchNo)
                .orderByDesc(AlipayTransferSceneConfig::getIsDefault)
                .orderByDesc(AlipayTransferSceneConfig::getEnabled)
                .orderByDesc(AlipayTransferSceneConfig::getCreateTime)
                .list();
    }

    /// 查询通道商户的默认场景(默认场景必须启用)
    public Optional<AlipayTransferSceneConfig> findDefault(String channelMchNo) {
        return lambdaQuery()
                .eq(AlipayTransferSceneConfig::getChannelMchNo, channelMchNo)
                .eq(AlipayTransferSceneConfig::getIsDefault, true)
                .eq(AlipayTransferSceneConfig::getEnabled, true)
                .oneOpt();
    }

    /// 统计通道商户已启用的场景数量
    public long countEnabled(String channelMchNo) {
        return lambdaQuery()
                .eq(AlipayTransferSceneConfig::getChannelMchNo, channelMchNo)
                .eq(AlipayTransferSceneConfig::getEnabled, true)
                .count();
    }

    /// 按主键查询
    public Optional<AlipayTransferSceneConfig> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return lambdaQuery()
                .eq(AlipayTransferSceneConfig::getId, id)
                .oneOpt();
    }

    /// 按通道商户号与场景名称查询(场景行不存在时返回空, 由调用方按需创建)
    public Optional<AlipayTransferSceneConfig> findByChannelMchNoAndSceneName(String channelMchNo, String sceneName) {
        return lambdaQuery()
                .eq(AlipayTransferSceneConfig::getChannelMchNo, channelMchNo)
                .eq(AlipayTransferSceneConfig::getSceneName, sceneName)
                .oneOpt();
    }

    /// 按主键删除(逻辑删除, 由 deleted 字段标记)
    public void deleteById(Long id) {
        lambdaUpdate()
                .eq(AlipayTransferSceneConfig::getId, id)
                .remove();
    }

    /// 清空指定通道商户的默认标记(设新默认前调用)
    public void clearDefault(String channelMchNo) {
        lambdaUpdate()
                .eq(AlipayTransferSceneConfig::getChannelMchNo, channelMchNo)
                .eq(AlipayTransferSceneConfig::getIsDefault, true)
                .set(AlipayTransferSceneConfig::getIsDefault, false)
                .update();
    }

    /// 清空指定通道商户的默认标记(排除指定 id,用于更新自身为默认)
    public void clearDefaultExclude(String channelMchNo, Long excludeId) {
        lambdaUpdate()
                .eq(AlipayTransferSceneConfig::getChannelMchNo, channelMchNo)
                .eq(AlipayTransferSceneConfig::getIsDefault, true)
                .ne(AlipayTransferSceneConfig::getId, excludeId)
                .set(AlipayTransferSceneConfig::getIsDefault, false)
                .update();
    }
}
