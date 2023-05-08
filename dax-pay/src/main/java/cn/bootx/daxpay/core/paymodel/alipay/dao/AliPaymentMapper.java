package cn.bootx.daxpay.core.paymodel.alipay.dao;

import cn.bootx.daxpay.core.paymodel.alipay.entity.AliPayment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付宝支付
 *
 * @author xxm
 * @date 2021/2/26
 */
@Mapper
public interface AliPaymentMapper extends BaseMapper<AliPayment> {

}
