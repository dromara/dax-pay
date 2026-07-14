package cn.daxpay.open.payment.merchant.dao.gateway;

import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCashierItem;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 网关收银台支付项管理器
@Repository
public class GatewayCashierItemManager extends BaseManager<GatewayCashierItemMapper, GatewayCashierItem> {

    /// 按应用 + 收银台类型 + 客户端环境查询, 按 sort_no 升序
    ///
    /// WEB 的 clientEnv 传 null, 条件为 client_env is null。
    public List<GatewayCashierItem> listByAppAndBucket(String appId, String cashierType, String clientEnv) {
        return lambdaQuery()
                .eq(GatewayCashierItem::getAppId, appId)
                .eq(GatewayCashierItem::getCashierType, cashierType)
                .eq(StrUtil.isNotBlank(clientEnv), GatewayCashierItem::getClientEnv, clientEnv)
                .isNull(StrUtil.isBlank(clientEnv), GatewayCashierItem::getClientEnv)
                .orderByAsc(GatewayCashierItem::getSortNo)
                .orderByAsc(GatewayCashierItem::getId)
                .list();
    }
}
