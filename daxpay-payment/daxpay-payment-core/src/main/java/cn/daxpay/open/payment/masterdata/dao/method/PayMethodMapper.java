package cn.daxpay.open.payment.masterdata.dao.method;

import cn.daxpay.open.payment.masterdata.entity.method.PayMethod;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付方式
@Mapper
public interface PayMethodMapper extends MPJBaseMapper<PayMethod> {
}