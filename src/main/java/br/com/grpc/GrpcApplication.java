package br.com.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrpcApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrpcApplication.class, args);
        System.out.println("=== Spring Boot gRPC Application Started ===");
        System.out.println("gRPC Server running on port: 9090");
        System.out.println("Health Check: http://localhost:8080/actuator/health");
        System.out.println("============================================");
    }
}