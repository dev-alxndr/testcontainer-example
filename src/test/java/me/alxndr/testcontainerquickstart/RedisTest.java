package me.alxndr.testcontainerquickstart;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisTest {

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	void redisTest() {
		final var key = "user";
		final var value = "alxndr";

		final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

		valueOperations.set(key, value);

		final var val = valueOperations.get(key);

		Assertions.assertThat(val).isEqualTo(value);
		System.out.println(val);
	}

}
