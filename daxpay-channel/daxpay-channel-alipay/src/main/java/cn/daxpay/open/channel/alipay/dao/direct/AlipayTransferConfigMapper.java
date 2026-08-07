package cn.daxpay.open.channel.alipay.dao.direct;

import cn.daxpay.open.channel.alipay.entity.direct.AlipayTransferConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付宝转账配置
///
/// 支付宝转账配置 MyBatis-Plus Mapper, 继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface AlipayTransferConfigMapper extends MPJBaseMapper<AlipayTransferConfig> {
}
