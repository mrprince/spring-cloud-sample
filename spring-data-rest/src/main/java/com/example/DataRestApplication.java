package com.example;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Collection;

@SpringBootApplication
@EnableCaching
public class DataRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataRestApplication.class, args);
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory rcf) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(rcf);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JsonRedisSerializer());
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        redisCacheManager.setUsePrefix(true);
        redisCacheManager.setDefaultExpiration(24 * 60 * 60); //one day expired
        return redisCacheManager;
    }

    static class JsonRedisSerializer implements RedisSerializer<Object> {
        private final ObjectMapper om;

        public JsonRedisSerializer() {
            this.om = new ObjectMapper().enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }

        @Override
        public byte[] serialize(Object t) throws SerializationException {
            try {
                return om.writeValueAsBytes(t);
            } catch (JsonProcessingException e) {
                throw new SerializationException(e.getMessage(), e);
            }
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null) {
                return null;
            }
            try {
                return om.readValue(bytes, Object.class);
            } catch (Exception e) {
                throw new SerializationException(e.getMessage(), e);
            }
        }
    }
}

@RepositoryRestResource
interface UserRepository extends JpaRepository<User, Integer> {
    @RestResource(path = "by-name")
    @Cacheable(value = "user", key = "'findByName_'+ #p0 ")
    Collection<User> findByName(@Param("name") String name);

    @Cacheable(value = "user", key = "'findOne_'+ #p0 ")
    User findOne(Integer id);

    @Caching(
            evict = {
                    @CacheEvict(value = "user", key = "'findByName_'+ #p0.name"),
                    @CacheEvict(value = "user", key = "'findOne_'+ #p0.id")
            }
    )
    void delete(User user);

    @Caching(
            evict = {
                    @CacheEvict(value = "user", key = "'findByName_'+ #p0.name"),
                    @CacheEvict(value = "user", key = "'findOne_'+ #p0.id")
            }
    )
    User save(User user);

}

@Entity
@Data
class User implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
}