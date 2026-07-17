package cn.daxpay.open.platform.capability.audit.log.dao;

import cn.daxpay.open.platform.capability.audit.log.entity.UnipayApiLogDb;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 统一支付接口审计日志 Mapper
///
@Mapper
public interface UnipayApiLogDbMapper extends MPJBaseMapper<UnipayApiLogDb> {
}
