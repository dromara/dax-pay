package cn.bootx.platform.iam.core.user.dao;

import cn.bootx.platform.iam.core.user.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息
 *
 * @author xxm
 * @since 2021/7/30
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}
