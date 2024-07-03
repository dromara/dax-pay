package cn.bootx.platform.iam.dao.client;

import cn.bootx.platform.iam.entity.client.Client;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 认证应用
 *
 * @author xxm
 * @since 2022-06-27
 */
@Mapper
public interface ClientMapper extends MPJBaseMapper<Client> {

}
