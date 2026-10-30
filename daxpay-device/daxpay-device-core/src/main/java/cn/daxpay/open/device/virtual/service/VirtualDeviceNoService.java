package cn.daxpay.open.device.virtual.service;

import cn.daxpay.open.device.virtual.dao.VirtualDeviceManager;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/// # 虚拟设备号生成器(VD + 日期 + 随机段)
///
/// 虚拟设备的设备号(SN)是平台自造字符串, 无硬件出厂编号约束, 生成时由系统产号,
/// 替代运营手工编列设备号; 格式: VD + yyMMdd + 6 位去混淆随机段(数字 2-9 + 大写字母去掉 I/O),
/// 共 14 位, 示例 VD261022K7M2Q4。
/// 随机段 32^6 ≈ 10.9 亿, 为激活唯一准入凭据(单因子)提供随机空间。
/// 本服务由运营端生成服务在同一事务内调用(产号 + 入库原子完成, 并发同号缝隙由入库侧兜底校验拦截)。
@Service
@RequiredArgsConstructor
public class VirtualDeviceNoService {

    /// 设备号前缀(虚拟设备平台自造号段, 与实体厂商出厂编号区分)
    public static final String PREFIX = "VD";

    /// 单次生成数量上限(与前端批量录入单次导入上限一致)
    public static final int MAX_COUNT = 5000;

    /// 撞号重生成轮次上限(随机段 32^6 ≈ 10.9 亿, 5000 台批内碰撞期望极低, 5 轮足够)
    private static final int MAX_ROUNDS = 5;

    /// 随机段字符集(数字 2-9 + 大写字母去掉 I/O, 去混淆避免人工抄录错位)
    private static final char[] RANDOM_CHARS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();

    /// 日期段格式(东八区墙钟, 与批次号 B+YYMMDDHHmmss 同一取值口径)
    private static final DateTimeFormatter DATE_PART = DateTimeFormatter.ofPattern("yyMMdd");

    private final VirtualDeviceManager virtualDeviceManager;

    /// 批量生成可用设备号(批内去重 + 虚拟设备台账查重, 撞号重生成, 有界重试)
    ///
    /// 开源版仅虚拟设备单台账(无音箱/打印台账跨表查重)。
    public List<String> generate(int count) {
        if (count < 1 || count > MAX_COUNT) {
            // 设备: 生成数量超限
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.generateCountRange", MAX_COUNT);
        }
        String datePart = LocalDate.now().format(DATE_PART);
        Set<String> result = new LinkedHashSet<>(count * 2);
        for (int round = 0; round < MAX_ROUNDS && result.size() < count; round++) {
            List<String> candidates = new ArrayList<>(count - result.size());
            while (candidates.size() < count - result.size()) {
                candidates.add(randomNo(datePart));
            }
            // 台账查重(SN 激活按设备号定位, 同号会让激活命中歧义)
            Set<String> exists = new HashSet<>();
            virtualDeviceManager.findByDeviceNos(candidates).forEach(device -> exists.add(device.getDeviceNo()));
            candidates.stream().filter(no -> !exists.contains(no)).forEach(result::add);
        }
        if (result.size() < count) {
            // 设备: 设备号生成失败(重试耗尽, 理论不可达)
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.noGenFailed");
        }
        return new ArrayList<>(result);
    }

    /// 生成单个候选号(VD + 日期 + 6 位随机段)
    private String randomNo(String datePart) {
        StringBuilder sb = new StringBuilder(PREFIX.length() + datePart.length() + 6);
        sb.append(PREFIX).append(datePart);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 6; i++) {
            sb.append(RANDOM_CHARS[random.nextInt(RANDOM_CHARS.length)]);
        }
        return sb.toString();
    }
}
