package cn.daxpay.open.payment.trade.order.dao;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.OffsetDateTime;

/// # 资金交易凭证 Mapper
///
@Mapper
public interface PayTradeMapper extends MPJBaseMapper<PayTrade> {

    /// 查询普通支付已超时但仍处理中的资金交易(兜底定时任务用)
    ///
    /// trade_type=normal, status=processing, 容器 pay_normal_order.expired_time &lt; now。
    /// 条数由首参 [IPage] 经分页插件追加方言 limit，勿在 SQL 写死 LIMIT。
    @Select("""
            SELECT t.*
            FROM pay_trade t
            WHERE t.trade_type = 'normal'
              AND t.status = 'processing'
              AND EXISTS (
                  SELECT 1 FROM pay_normal_order o
                  WHERE o.id = t.container_id
                    AND o.expired_time < #{now}
              )
            ORDER BY t.create_time ASC
            """)
    IPage<PayTrade> findNormalTimeoutTrades(IPage<PayTrade> page, @Param("now") OffsetDateTime now);

    /// 查询网关支付已超时但仍处理中的资金交易(兜底定时任务用)
    ///
    /// trade_type=gateway, status=processing, 容器 pay_gateway_order.expired_time &lt; now。
    /// 条数由首参 [IPage] 经分页插件追加方言 limit，勿在 SQL 写死 LIMIT。
    @Select("""
            SELECT t.*
            FROM pay_trade t
            WHERE t.trade_type = 'gateway'
              AND t.status = 'processing'
              AND EXISTS (
                  SELECT 1 FROM pay_gateway_order o
                  WHERE o.id = t.container_id
                    AND o.expired_time < #{now}
              )
            ORDER BY t.create_time ASC
            """)
    IPage<PayTrade> findGatewayTimeoutTrades(IPage<PayTrade> page, @Param("now") OffsetDateTime now);
}
