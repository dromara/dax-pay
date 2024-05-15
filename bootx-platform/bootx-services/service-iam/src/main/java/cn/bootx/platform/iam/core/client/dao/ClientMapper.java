package cn.bootx.platform.iam.core.client.dao;

import cn.bootx.platform.iam.core.client.entity.Client;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 认证应用
 *
 * @author xxm
 * @since 2022-06-27
 */
@Mapper
public interface ClientMapper extends BaseMapper<Client> {

}
