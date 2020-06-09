import com.example.redis.ExampleApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(classes = ExampleApplication.class)
@RunWith(SpringRunner.class)
public class RedisTemplateTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void stringTest() {
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String key = "string:demo:letter";
        valueOperations.set(key, "A");
        String value = valueOperations.get(key);
        Assert.assertEquals(value, "A");

        valueOperations.append(key, "BC");
        value = valueOperations.get(key);
        Assert.assertEquals(value, "ABC");

        String key2 = "string:demo:number";
        valueOperations.set(key2, "1");
        valueOperations.increment(key2);
        value = valueOperations.get(key2);
        Assert.assertEquals("2", value);

        valueOperations.increment(key2, 2);
        value = valueOperations.get(key2);
        Assert.assertEquals("4", value);
    }

    @Test
    public void hashTest() {
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        String key = "hash:demo:user:1";

        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, "name", "Paul");
        hashOperations.put(key, "sex", "man");
        hashOperations.put(key, "age", "79");

        String name = hashOperations.get(key, "name");
        String sex = hashOperations.get(key, "sex");
        String age = hashOperations.get(key, "age");

        Assert.assertEquals(name, "Paul");
        Assert.assertEquals(sex, "man");
        Assert.assertEquals(age, "79");

        Long nameLength = hashOperations.lengthOfValue(key, "name");
        Assert.assertTrue(nameLength.compareTo(4L) == 0);

        hashOperations.increment(key, "age", 1);
        age = hashOperations.get(key, "age");
        Assert.assertEquals("80", age);
    }

    @Test
    public void setTest() {
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        String key = "set:demo:user:1";

        while (setOperations.size(key) > 0) {
            setOperations.pop(key);
        }
        setOperations.add(key, "Jack", "Paul");
        setOperations.add(key, "Jack", "Paul");
        setOperations.add(key, "Lucy");

        Set<String> users = setOperations.members(key);
        Assert.assertEquals(3, users.size());

        log.info("set :{}", users.toString());


        String key2 = "set:demo:user:2";
        while (setOperations.size(key2) > 0) {
            setOperations.pop(key2);
        }
        setOperations.add(key2, "John", "Paul", "Lucy");
        Set<String> diff = setOperations.difference(key, key2);
        log.info("difference :{}", diff);
        Assert.assertTrue(diff.size() == 1 && diff.contains("Jack"));


        String user = setOperations.pop(key2);
        log.info("remove {}", user);
        Assert.assertFalse(setOperations.isMember(key2, user));

        Set<String> unionUsers = setOperations.union(key, key2);
        log.info("after union :{}", unionUsers.toString());
        Assert.assertTrue(unionUsers.size() <= 4);

    }

    @Test
    public void listTest() {
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ListOperations<String, String> listOperations = redisTemplate.opsForList();

        String key = "list:demo:letter";
        /**
         * 移除所有Paul和Rock
         */
        listOperations.remove(key, 0, "A");
        listOperations.remove(key, 0, "B");
        listOperations.remove(key, 0, "C");

        listOperations.leftPush(key, "A");
        listOperations.leftPush(key, "A");
        listOperations.leftPush(key, "C");
        listOperations.leftPush(key, "C", "B");
        listOperations.leftPush(key, "A");
        listOperations.leftPush(key, "A");
        listOperations.leftPush(key, "A");
        //从后至前移除3个Paul
        listOperations.remove(key, -3, "A");
        //从前至后移除1个Paul
        listOperations.remove(key, 1, "A");

        List<String> letters = listOperations.range(key, 0, -1);
        String[] exceptLetters = new String[]{"A", "B", "C"};
        Assert.assertArrayEquals(exceptLetters, letters.toArray());

        listOperations.trim(key, 2, -1);
        letters = listOperations.range(key, 0, -1);
        Assert.assertTrue(letters.size() == 1 && letters.get(0).equals("C"));

    }

    @Test
    public void test() {
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        String key = "zset:demo:user";
        zSetOperations.removeRangeByScore(key, 0, 100);
        zSetOperations.add(key, "Paul5", 0);
        zSetOperations.add(key, "Paul4", 4);
        zSetOperations.add(key, "Paul3", 3);
        zSetOperations.add(key, "Paul1", 1);
        zSetOperations.add(key, "Paul2", 2);
        zSetOperations.add(key, "Paul2", 2);
        zSetOperations.incrementScore(key, "Paul5", 5);
        Set<String> users = zSetOperations.range(key, 0, -1);
        String[] exceptUsers = new String[]{"Paul1", "Paul2", "Paul3", "Paul4", "Paul5"};
        log.info("users: {}", users.toString());
        Assert.assertArrayEquals(exceptUsers, users.toArray());

        users = zSetOperations.rangeByScore(key, 3, 4);
        exceptUsers = new String[]{"Paul3", "Paul4"};
        log.info("users: {}", users.toString());
        Assert.assertArrayEquals(exceptUsers, users.toArray());

        Long rank = zSetOperations.rank(key, "Paul3");
        log.info("Paul rank: {}", rank);
        Assert.assertTrue(rank.equals(2L));


    }

    @Test
    public void bitTest() {
        redisTemplate.opsForValue().setBit("success", 1, true);
        boolean success = redisTemplate.opsForValue().getBit("success", 1);
        Assert.assertTrue(success);

        success = redisTemplate.opsForValue().getBit("success", 2);
        Assert.assertFalse(success);
    }

    @Test
    public void expireTest() throws InterruptedException {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = "expire:demo:letter";
        valueOperations.set(key, "A");
        redisTemplate.expire(key, 10, TimeUnit.SECONDS);
        String value = valueOperations.get(key);
        Assert.assertTrue(value.equals("A"));

        TimeUnit.SECONDS.sleep(10);
        value = valueOperations.get(key);
        Assert.assertTrue(value == null);
    }
}
