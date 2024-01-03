package cn.bootx.platform.daxpay.service.core.system.dao;

import cn.bootx.platform.daxpay.service.core.system.entity.PayApiConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 开放接口信息
 * @author xxm
 * @since 2023/12/22
 */
@Mapper
public interface PayApiConfigMapper extends BaseMapper<PayApiConfig> {
}
