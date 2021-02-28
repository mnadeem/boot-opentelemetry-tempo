package io.opentelemetry.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import io.lettuce.core.ReadFrom;

@Configuration
public class RedisConfig {
	
	@Value("${spring.redis.host}")
	private String redisHost;
	
	@Value("${spring.redis.port}")
	private int redisPort;

	@Bean
	LettuceConnectionFactory redisConnectionFactory(RedisClusterConfiguration redisConfiguration) {

		LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
				.readFrom(ReadFrom.REPLICA_PREFERRED).build();
		return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort), clientConfig);
		//return new LettuceConnectionFactory(redisConfiguration, clientConfig);
	}

	@Bean
	LettuceClientConfiguration clientConfig() {
		return LettuceClientConfiguration.builder()
										.readFrom(ReadFrom.REPLICA_PREFERRED)
										.build();
	}

	@Bean
	RedisClusterConfiguration redisConfiguration(RedisClusterConfigProperties clusterProperties) {
		RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterProperties.getNodes());
		redisClusterConfiguration.setMaxRedirects(clusterProperties.getMaxRedirects());
		return redisClusterConfiguration;
	}

	@Bean(name = "redisTemplate")
	StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}	
}