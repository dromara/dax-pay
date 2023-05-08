package cn.bootx.daxpay.core.paymodel.wechat.dao;

import cn.bootx.daxpay.core.paymodel.wechat.entity.WeChatPayConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信支付配置
 *
 * @author xxm
 * @date 2021/3/19
 */
@Mapper
public interface WeChatPayConfigMapper extends BaseMapper<WeChatPayConfig> {

}
