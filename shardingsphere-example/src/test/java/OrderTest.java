import com.shardingsphere.example.ShardingsphereExampleApplication;
import com.shardingsphere.example.entity.TOrder;
import com.shardingsphere.example.mapper.TOrderMapper;
import com.shardingsphere.example.service.TOrderService;
import com.shardingsphere.example.service.TOrderServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@MapperScan("com.shardingsphere.example.mapper")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardingsphereExampleApplication.class)
public class OrderTest {

    @Autowired
    private TOrderMapper tOrderMapper;

    @Autowired
    private TOrderServiceImpl tOrderService;


    @Test
    public void test() {
        List<TOrder> tOrders = tOrderMapper.selectList(null);
        tOrders.forEach(System.out::println);
    }

    @Test
    public void save() {
        List<TOrder> orders = new ArrayList<>(100);
        orders = IntStream
            .rangeClosed(1, 100)
            .mapToObj(i -> new TOrder(i, "OK"))
            .collect(Collectors.toList());

//        boolean success = tOrderService.save(new TOrder(12,"ok"));
        boolean success = tOrderService.saveBatch(orders);
        Assert.assertTrue(success);
    }
}
