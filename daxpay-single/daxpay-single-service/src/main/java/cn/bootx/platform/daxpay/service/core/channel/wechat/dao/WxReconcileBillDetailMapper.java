package cn.bootx.platform.daxpay.service.core.channel.wechat.dao;

import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WxReconcileBillDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信对账单明细表
 * @author xxm
 * @since 2024/1/18
 */
@Mapper
public interface WxReconcileBillDetailMapper extends BaseMapper<WxReconcileBillDetail> {
}
