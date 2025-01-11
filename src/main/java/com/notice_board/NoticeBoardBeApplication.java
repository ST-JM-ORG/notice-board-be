package com.notice_board;

import com.notice_board.common.YamlPropertySourceFactory;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@Slf4j
@PropertySource(
        value = {"classpath:config/application.yml", "classpath:config/${spring.profiles.active:local}/application.yml"} ,
        factory = YamlPropertySourceFactory.class
)
@ConfigurationPropertiesScan("com.notice_board")
@EntityScan(basePackages = "com.notice_board.model")
@EnableJpaAuditing
public class NoticeBoardBeApplication {
    static {
        System.setProperty("spring.config.location", "classpath:config/application.yml");
    }

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        log.info("서버 시작 시간 : " + new Date());
    }

    public static void main(String[] args) {
        SpringApplication.run(NoticeBoardBeApplication.class, args);
    }

}
