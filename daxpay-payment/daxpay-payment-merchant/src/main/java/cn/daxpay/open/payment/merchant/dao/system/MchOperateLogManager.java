package cn.daxpay.open.payment.merchant.dao.system;

import cn.daxpay.open.platform.capability.audit.log.dao.OperateLogDbMapper;
import cn.daxpay.open.platform.capability.audit.log.entity.OperateLogDb;
import cn.daxpay.open.platform.capability.audit.log.param.OperateLogQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 操作日志(商户端)
///
/// [QueryGenerator] 不支持 IN 条件, 商户白名单的双 IN OR 由本类手拼 wrapper 补充(列名走字符串, QueryWrapper 非 lambda 形态)。
@Repository
public class MchOperateLogManager extends BaseManager<OperateLogDbMapper, OperateLogDb> {

    /// 商户白名单分页: 注解驱动条件 + (operate_id IN userIds OR account IN accounts)
    ///
    /// userIds 与 accounts 任一为空时只拼另一条件; 两者都空的场景由 Service 层提前返回空页, 不进查询。
    public Page<OperateLogDb> pageByScope(PageParam pageParam, OperateLogQuery query, List<Long> userIds, List<String> accounts) {
        QueryWrapper<OperateLogDb> wrapper = QueryGenerator.generator(query);
        wrapper.and(w -> {
            if (userIds.isEmpty()) {
                w.in("account", accounts);
            } else if (accounts.isEmpty()) {
                w.in("operate_id", userIds);
            } else {
                w.in("operate_id", userIds).or().in("account", accounts);
            }
        });
        return this.page(MpUtil.getMpPage(pageParam), wrapper);
    }
}
