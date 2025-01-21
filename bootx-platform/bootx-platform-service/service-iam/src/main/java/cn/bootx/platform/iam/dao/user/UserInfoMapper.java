package cn.bootx.platform.iam.dao.user;

import cn.bootx.platform.iam.entity.user.UserInfo;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息
 *
 * @author xxm
 * @since 2021/7/30
 */
@Mapper
public interface UserInfoMapper extends MPJBaseMapper<UserInfo> {

}
