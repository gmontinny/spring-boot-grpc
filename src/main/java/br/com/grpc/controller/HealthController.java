package br.com.grpc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now(),
                "service", "Spring Boot gRPC User Service",
                "grpc_port", 9090,
                "rest_port", 8080
        );
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        return Map.of(
                "application", "Spring Boot gRPC User Service",
                "version", "1.0.0",
                "description", "Demonstração completa de gRPC com Spring Boot",
                "features", Map.of(
                        "grpc_server", "Servidor gRPC na porta 9090",
                        "rest_api", "API REST para testes na porta 8080",
                        "crud_operations", "Operações CRUD completas",
                        "streaming", "Suporte a streaming gRPC",
                        "validation", "Validação de dados",
                        "exception_handling", "Tratamento de exceções"
                )
        );
    }
}