package cn.bootx.platform.notice.core.site.dao;

import cn.bootx.platform.notice.core.site.domain.SiteMessageInfo;
import cn.bootx.platform.notice.core.site.entity.SiteMessage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 站内信
 *
 * @author xxm
 * @since 2021/8/7
 */
@Mapper
public interface SiteMessageMapper extends BaseMapper<SiteMessage> {

    Page<SiteMessageInfo> pageMassage(Page<SiteMessageInfo> page,
                                      @Param(Constants.WRAPPER) Wrapper<SiteMessageInfo> wrapper);

    Integer countMassage(@Param(Constants.WRAPPER) Wrapper<SiteMessageInfo> wrapper);

}
