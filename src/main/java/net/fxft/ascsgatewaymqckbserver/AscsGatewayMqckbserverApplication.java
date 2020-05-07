package net.fxft.ascsgatewaymqckbserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AscsGatewayMqckbserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(AscsGatewayMqckbserverApplication.class, args);
    }

}
