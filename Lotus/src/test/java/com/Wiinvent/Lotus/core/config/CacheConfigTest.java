package com.Wiinvent.Lotus.core.config;

import com.Wiinvent.Lotus.domain.reward.entity.RewardConfig;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CacheConfigTest {

    @Test
    void shouldSerializeAndDeserializeLocalDateTimeInRewardConfig() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        RewardConfig rewardConfig = RewardConfig.builder()
                .dayNumber(1)
                .rewardPoint(1L)
                .build();
        rewardConfig.setCreatedAt(LocalDateTime.now());
        rewardConfig.setUpdatedAt(LocalDateTime.now());

        byte[] serialized = serializer.serialize(rewardConfig);
        assertThat(serialized).isNotEmpty();

        Object deserialized = serializer.deserialize(serialized);
        assertThat(deserialized).isInstanceOf(RewardConfig.class);
        RewardConfig result = (RewardConfig) deserialized;
        assertThat(result.getDayNumber()).isEqualTo(1);
        assertThat(result.getCreatedAt()).isNotNull();
    }
}
