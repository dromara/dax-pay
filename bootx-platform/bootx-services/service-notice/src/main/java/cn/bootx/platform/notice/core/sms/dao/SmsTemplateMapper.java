package cn.bootx.platform.notice.core.sms.dao;

import cn.bootx.platform.notice.core.sms.entity.SmsTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信模板配置
 * @author xxm
 * @since 2023-08-03
 */
@Mapper
public interface SmsTemplateMapper extends BaseMapper<SmsTemplate> {
}
