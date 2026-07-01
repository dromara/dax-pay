package cn.daxpay.open.channel.douyin.dao.direct;

import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectAppCapability;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 抖音直连商户应用支付能力关联 Mapper
///
/// 继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface DouyinDirectAppCapabilityMapper extends MPJBaseMapper<DouyinDirectAppCapability> {
}
