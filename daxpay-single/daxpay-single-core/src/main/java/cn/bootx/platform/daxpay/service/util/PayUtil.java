package cn.bootx.platform.daxpay.service.util;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayAmountAbnormalException;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author xxm
 * @since 2023/12/24
 */
@UtilityClass
public class PayUtil {
    /**
     * 将参数转换为map对象. 使用ChatGPT生成
     * 1. 参数名ASCII码从小到大排序（字典序）
     * 2. 如果参数的值为空不参与签名；
     * 3. 参数名不区分大小写；
     */
    public Map<String, String> toMap(Object object) {
        Map<String, String> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        toMap(object, map);
        return map;
    }

    /**
     * 将参数转换为map对象. 使用ChatGPT生成, 仅局限于对请求支付相关参数进行签名
     */
    @SneakyThrows
    private void toMap(Object object, Map<String, String> map) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(object);
                if (fieldValue != null) {
                    // 基础类型及包装类 和 字符串类型
                    if (ClassUtil.isBasicType(field.getType())|| field.getType().equals(String.class)) {
                        String fieldValueString = String.valueOf(fieldValue);
                        map.put(fieldName, fieldValueString);

                    }
                    // java8时间类型 转为时间戳
                    else if (field.getType().equals(LocalDateTime.class)) {
                        LocalDateTime localDateTime = (LocalDateTime) fieldValue;
                        long timestamp = LocalDateTimeUtil.timestamp(localDateTime);
                        map.put(fieldName, String.valueOf(timestamp));
                    }
                    // 集合类型
                    else if (Collection.class.isAssignableFrom(field.getType())) {
                        Collection<?> collection = (Collection<?>) fieldValue;
                        if (!collection.isEmpty()) {
                            List<Map<String, String>> maps = collection.stream()
                                    .filter(Objects::nonNull)
                                    .map(item -> {
                                        Map<String, String> nestedMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                                        toMap(item, nestedMap);
                                        return nestedMap;
                                    })
                                    .collect(Collectors.toList());
                            map.put(fieldName,  JSONUtil.toJsonStr(maps));
                        }
                        // 其他类型
                    } else {
                        Map<String, String> nestedMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                        toMap(fieldValue, nestedMap);
                        String nestedJson = JSONUtil.toJsonStr(map);
                        map.put(fieldName, nestedJson);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 检查异步支付方式
     */
    public void validationAsyncPay(PayParam payParam) {
        // 组合支付时只允许有一个异步支付方式
        List<PayWayParam> payModeList = payParam.getPayWays();

        long asyncPayCount = payModeList.stream()
                .map(PayWayParam::getChannel)
                .map(PayChannelEnum::findByCode)
                .filter(PayChannelEnum.ASYNC_TYPE::contains)
                .count();
        if (asyncPayCount > 1) {
            throw new PayFailureException("组合支付时只允许有一个异步支付方式");
        }
    }


    /**
     * 检查支付金额
     */
    public void validationAmount(List<PayWayParam> payModeList) {
        for (PayWayParam payWayParam : payModeList) {
            // 支付金额小于等于零
            if (payWayParam.getAmount() < 0) {
                throw new PayAmountAbnormalException();
            }
        }
    }

    /**
     * 获取支付宝的过期时间
     */
    public String getAliExpiredTime(int minute) {
        return minute + "m";
    }

    /**
     * 获取支付宝的过期时间
     */
    public String getAliTimeExpire(LocalDateTime dateTime) {
        return LocalDateTimeUtil.format(dateTime, DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * 获取微信的过期时间
     */
    public String getWxExpiredTime(LocalDateTime dateTime) {
        return LocalDateTimeUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN);
    }

    /**
     * 获取支付单的超时时间
     */
    public LocalDateTime getPaymentExpiredTime(Integer minute) {
        return LocalDateTimeUtil.offset(LocalDateTime.now(), minute, ChronoUnit.MINUTES);
    }

    /**
     * 判断是否有异步支付
     */
    public boolean isNotSync(List<PayWayParam> payWayParams) {
        return payWayParams.stream()
                .map(PayWayParam::getChannel)
                .noneMatch(PayChannelEnum.ASYNC_TYPE_CODE::contains);
    }
}
