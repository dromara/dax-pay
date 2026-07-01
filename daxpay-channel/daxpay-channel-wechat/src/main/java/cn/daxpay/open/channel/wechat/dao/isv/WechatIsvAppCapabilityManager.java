package cn.daxpay.open.channel.wechat.dao.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvAppCapability;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/// # 微信服务商应用支付能力关联 Manager
///
/// 关联记录数据访问管理器，提供全量查询、按能力查应用、全量删除、按应用清理等方法。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvAppCapabilityManager extends BaseManager<WechatIsvAppCapabilityMapper, WechatIsvAppCapability> {

    /// 查询全部关联(按创建时间升序)
    public List<WechatIsvAppCapability> listAll() {
        return lambdaQuery()
                .orderByAsc(WechatIsvAppCapability::getCreateTime)
                .orderByAsc(WechatIsvAppCapability::getId)
                .list();
    }

    /// 根据支付能力查询单条关联(支付时调用)
    public Optional<WechatIsvAppCapability> findOne(String capability) {
        return lambdaQuery()
                .eq(WechatIsvAppCapability::getCapability, capability)
                .oneOpt();
    }

    /// 全量删除(批量保存时先清后插)
    public void deleteAll() {
        lambdaUpdate().remove();
    }

    /// 根据应用ID删除关联(应用被删除时级联清理，避免悬空引用)
    public void deleteByWechatIsvAppId(Long wechatIsvAppId) {
        lambdaUpdate()
                .eq(WechatIsvAppCapability::getWechatIsvAppId, wechatIsvAppId)
                .remove();
    }
}
