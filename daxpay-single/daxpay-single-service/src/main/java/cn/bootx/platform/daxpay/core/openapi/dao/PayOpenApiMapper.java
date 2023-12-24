package cn.bootx.platform.daxpay.core.openapi.dao;

import cn.bootx.platform.daxpay.core.openapi.entity.PayOpenApi;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 开放接口信息
 * @author xxm
 * @since 2023/12/22
 */
@Mapper
public interface PayOpenApiMapper extends BaseMapper<PayOpenApi> {
}
