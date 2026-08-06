package cn.daxpay.open.demo.ipregion.result;

import lombok.Data;
import lombok.experimental.Accessors;

/// # IP归属地查询演示结果
///
/// 与审计日志（`operateLocation` / `loginLocation`）写入逻辑同源，
/// 同时返回结构化字段与格式化文本，方便前端直观展示。
@Data
@Accessors(chain = true)
public class IpRegionDemoResult {

    /// 查询的 IP 地址
    private String ip;

    /// 国家
    private String country;

    /// 省份
    private String province;

    /// 城市
    private String city;

    /// ISP 运营商
    private String isp;

    /// 国家码(iso-alpha2, 如 CN/HK/US)
    private String countryCode;

    /// 格式化后的归属地文本（与审计日志 location 字段同源）
    private String regionStr;

    /// 是否内网地址
    private boolean innerIp;

    /// 是否国内地址
    private boolean chinaIp;
}
