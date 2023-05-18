package cn.bootx.platform.daxpay.core.merchant.dao;

import cn.bootx.platform.daxpay.core.merchant.entity.MerchantInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商户
 * @author xxm
 * @date 2023-05-17
 */
@Mapper
public interface MerchantInfoMapper extends BaseMapper<MerchantInfo> {
}