package br.com.grpc.config;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class GrpcServerConfig implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        startGrpcServer();
    }

    private void startGrpcServer() throws IOException, InterruptedException {
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(9090);
        
        // Adicionar serviço de reflection
        serverBuilder.addService(ProtoReflectionService.newInstance());
        log.info("Registered gRPC reflection service");
        
        // Registrar todos os serviços gRPC
        Map<String, Object> grpcServices = applicationContext.getBeansWithAnnotation(GrpcService.class);
        for (Object service : grpcServices.values()) {
            if (service instanceof io.grpc.BindableService) {
                serverBuilder.addService((io.grpc.BindableService) service);
                log.info("Registered gRPC service: {}", service.getClass().getSimpleName());
            }
        }

        Server server = serverBuilder.build().start();
        log.info("gRPC Server started on port: 9090");
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down gRPC server");
            server.shutdown();
        }));
    }
}