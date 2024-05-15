import cn.daxpay.single.service.core.order.pay.dao.PayOrderMapper;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.start.DaxpaySingleStart;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DaxpaySingleStart.class)
@WebAppConfiguration
public class SprintBootAppTest {

    @Resource
    private PayOrderMapper payOrderMapper;

    @Test
    public void test2() {
        long time = System.currentTimeMillis();
        for (int a = 0; a < 10; a++) {
            List<PayOrder> list = new ArrayList<>();
            for (int i = 0; i <= 5000; i++) {
                PayOrder payOrder = new PayOrder();
                payOrder.setVersion(1);
                payOrder.setDeleted(false);
                payOrder.setStatus("fail");
                payOrder.setId(IdUtil.getSnowflakeNextId());
                list.add(payOrder);
            }
            payOrderMapper.insertList(list);
        }
        log.info("耗时：{}秒", (double) (System.currentTimeMillis() - time) / 1000);

    }
}
