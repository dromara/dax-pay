package cn.daxpay.open.start;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/// # 商户端端点参数类护栏测试
///
/// 防复发「商户号不可为空」缺陷: 商户端(`/mch/`、`/app-mch/`)约定 mchNo 由控制器从
/// [cn.daxpay.open.payment.common.context.PaymentContext] 强制组装(前端不传, 防越权),
/// 若挂了 Default 组校验的 `@RequestBody` 参数类含 Default 组必填的 mchNo, Bean Validation
/// 会在方法体之前拦截请求, 控制器里的 `setMchNo` 永远执行不到, 商户端保存必报
/// `validation.field.mchNo.notBlank`。
///
/// 正确写法二选一(参照 [cn.daxpay.open.channel.wechat.controller.direct.MchWechatDirectKeyConfigController]):
/// ① 拆商户端专用参数类(`Mch*Param`, 不含 mchNo), 控制器组装完整参数调 service;
/// ② 参数不挂校验注解, 方法体 `setMchNo` 后用 `ValidationUtil.validateParam` 手工校验。
///
/// 本测试扫描全部 RestController, 断言上述冲突组合不存在; 新增商户端端点若误用运营端
/// 参数类(含必填 mchNo)将直接测试失败。
class MerchantEndpointParamGuardTest {

    /// 商户端路径前缀(商户端 Web 与商户端小程序)
    private static final List<String> MCH_PREFIXES = List.of("/mch/", "/app-mch/");

    @Test
    void merchantEndpointBodyParamMustNotRequireMchNo() throws Exception {
        List<String> violations = new ArrayList<>();

        var provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        Set<BeanDefinition> candidates = provider.findCandidateComponents("cn.daxpay.open");

        for (BeanDefinition candidate : candidates) {
            Class<?> controller = Class.forName(candidate.getBeanClassName());
            RequestMapping mapping = AnnotatedElementUtils.findMergedAnnotation(controller, RequestMapping.class);
            if (mapping == null || mapping.value().length == 0) {
                continue;
            }
            String path = mapping.value()[0];
            if (MCH_PREFIXES.stream().noneMatch(path::startsWith)) {
                continue;
            }
            collectViolations(controller, path, violations);
        }

        Assertions.assertTrue(violations.isEmpty(),
                "商户端端点存在 Default 组必填 mchNo 的 @RequestBody 参数类(会先于控制器 setMchNo 触发校验, 商户端保存必报\"商户号不可为空\"):\n"
                        + String.join("\n", violations)
                        + "\n修复: 改用不含 mchNo 的商户端 Mch*Param 并在控制器组装(参照 MchWechatDirectKeyConfigController)");
    }

    /// 检查单个商户端控制器的全部 @RequestBody 方法参数
    private void collectViolations(Class<?> controller, String path, List<String> violations) {
        for (Method method : controller.getDeclaredMethods()) {
            for (Parameter parameter : method.getParameters()) {
                if (!parameter.isAnnotationPresent(RequestBody.class)) {
                    continue;
                }
                if (!validatesDefaultGroup(parameter)) {
                    continue;
                }
                Class<?> paramType = parameter.getType();
                if (paramType.isPrimitive() || paramType.isArray() || List.class.isAssignableFrom(paramType)) {
                    continue;
                }
                if (mchNoRequiredAtDefaultGroup(paramType)) {
                    violations.add("%s#%s(%s)  [path=%s]".formatted(
                            controller.getSimpleName(), method.getName(), paramType.getSimpleName(), path));
                }
            }
        }
    }

    /// 参数上是否挂了触发 Default 组 Bean Validation 的注解(@Valid 恒为 Default; @Validated 看显式组)
    private boolean validatesDefaultGroup(Parameter parameter) {
        if (parameter.isAnnotationPresent(Valid.class)) {
            return true;
        }
        Validated validated = parameter.getAnnotation(Validated.class);
        if (validated == null) {
            return false;
        }
        // value() 即校验组; 为空等价 Default 组; 显式指定非 Default 组(如 ValidationGroup.add)时 Default 组约束不参与校验
        return validated.value().length == 0
                || Arrays.asList(validated.value()).contains(jakarta.validation.groups.Default.class);
    }

    /// 参数类是否含 Default 组必填的 mchNo 字段
    private boolean mchNoRequiredAtDefaultGroup(Class<?> paramType) {
        Field mchNo;
        try {
            mchNo = paramType.getDeclaredField("mchNo");
        } catch (NoSuchFieldException e) {
            return false;
        }
        for (var annotation : mchNo.getAnnotations()) {
            List<Class<?>> groups;
            if (annotation instanceof NotBlank notBlank) {
                groups = Arrays.asList(notBlank.groups());
            } else if (annotation instanceof NotNull notNull) {
                groups = Arrays.asList(notNull.groups());
            } else {
                continue;
            }
            if (groups.isEmpty() || groups.contains(jakarta.validation.groups.Default.class)) {
                return true;
            }
        }
        return false;
    }
}
