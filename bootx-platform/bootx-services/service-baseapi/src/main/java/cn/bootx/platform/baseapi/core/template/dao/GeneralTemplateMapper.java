package cn.bootx.platform.baseapi.core.template.dao;

import cn.bootx.platform.baseapi.core.template.entity.GeneralTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通用模板管理
 * @author xxm
 * @since 2023-08-12
 */
@Mapper
public interface GeneralTemplateMapper extends BaseMapper<GeneralTemplate> {
}
