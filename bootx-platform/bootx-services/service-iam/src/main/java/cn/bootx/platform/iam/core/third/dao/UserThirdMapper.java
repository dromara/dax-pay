package cn.bootx.platform.iam.core.third.dao;

import cn.bootx.platform.iam.core.third.entity.UserThird;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 三方登录
 *
 * @author xxm
 * @since 2021/8/2
 */
@Mapper
public interface UserThirdMapper extends BaseMapper<UserThird> {

}
