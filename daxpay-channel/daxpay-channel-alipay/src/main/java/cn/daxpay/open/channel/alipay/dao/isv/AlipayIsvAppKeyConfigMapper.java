package cn.daxpay.open.channel.alipay.dao.isv;

import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvAppKeyConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付宝服务商应用密钥配置
///
/// 支付宝服务商应用密钥配置 MyBatis-Plus Mapper，继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface AlipayIsvAppKeyConfigMapper extends MPJBaseMapper<AlipayIsvAppKeyConfig> {
}
