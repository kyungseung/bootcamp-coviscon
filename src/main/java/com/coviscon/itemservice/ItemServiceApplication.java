package com.coviscon.itemservice;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication(
		exclude = {
        org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
        org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
        org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class}
)
public class ItemServiceApplication {

//    public static final String APPLICATION_LOCATIONS = "spring.config.location="
//            + "classpath:application.yml,"
//            + "classpath:application-aws.yml";
//
//    public static void main(String[] args) {
//        new SpringApplicationBuilder(ItemServiceApplication.class)
//                .properties(APPLICATION_LOCATIONS)
//                .run(args);
//    }

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

    /* Feign Package 내부에 있는 Logger */
    @Bean
    public Logger.Level feignLoggerLevel() {
        /* 전체 로그 레벨 확인 */
        return Logger.Level.FULL;
    }

    @Bean
    public AmazonS3 amazonS3() {
        return new AmazonS3Client();
    }
}

