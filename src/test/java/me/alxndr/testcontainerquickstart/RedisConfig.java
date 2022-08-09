package me.alxndr.testcontainerquickstart;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class RedisConfig {
	static final GenericContainer REDIS_CONTAINER;
	private static final String REDIS_IMAGE = "redis:5.0.3-alpine";
	private static final int REDIS_PORT = 6379;

	static {
		REDIS_CONTAINER =
				new GenericContainer(DockerImageName.parse(REDIS_IMAGE)).withExposedPorts(REDIS_PORT);
		REDIS_CONTAINER.start();
	}
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(REDIS_CONTAINER.getHost(), REDIS_CONTAINER.getFirstMappedPort());
	}

	@Bean
	public RedisTemplate<?, ?> redisTemplate() {
		RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		return redisTemplate;
	}
}
