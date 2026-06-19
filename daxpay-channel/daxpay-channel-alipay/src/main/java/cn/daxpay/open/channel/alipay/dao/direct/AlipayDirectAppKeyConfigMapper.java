package cn.daxpay.open.channel.alipay.dao.direct;

import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppKeyConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付宝直连商户应用密钥配置
///
/// 支付宝直连商户应用密钥配置 MyBatis-Plus Mapper，继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface AlipayDirectAppKeyConfigMapper extends MPJBaseMapper<AlipayDirectAppKeyConfig> {
}
