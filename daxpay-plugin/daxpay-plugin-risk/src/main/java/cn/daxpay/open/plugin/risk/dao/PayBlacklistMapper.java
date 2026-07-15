package cn.daxpay.open.plugin.risk.dao;

import cn.daxpay.open.plugin.risk.entity.PayBlacklist;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 黑名单 Mapper
///
@Mapper
public interface PayBlacklistMapper extends MPJBaseMapper<PayBlacklist> {
}
