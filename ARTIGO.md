# gRPC com Spring Boot: Construindo APIs de Alta Performance

## Introdução

Em um mundo onde a performance e eficiência das aplicações são cruciais, o **gRPC** (Google Remote Procedure Call) emerge como uma solução revolucionária para comunicação entre serviços. Este artigo apresenta uma implementação completa de gRPC com Spring Boot, demonstrando como construir APIs modernas, eficientes e escaláveis.

## O que é gRPC?

**gRPC** é um framework de comunicação de alta performance desenvolvido pelo Google que utiliza **HTTP/2** como protocolo de transporte e **Protocol Buffers** como formato de serialização. Diferentemente das APIs REST tradicionais que usam JSON sobre HTTP/1.1, o gRPC oferece:

### Características Principais:
- **Comunicação Binária**: Serialização eficiente com Protocol Buffers
- **HTTP/2**: Multiplexação, compressão de headers e server push
- **Streaming Bidirecional**: Comunicação em tempo real
- **Tipagem Forte**: Contratos bem definidos
- **Multiplataforma**: Suporte a diversas linguagens

## Por que gRPC?

### 1. **Performance Superior**
```
REST/JSON:     ~1.2MB para 1000 objetos
gRPC/Protobuf: ~156KB para 1000 objetos
Redução: ~87% no tamanho dos dados
```

### 2. **Eficiência de Rede**
- **HTTP/2**: Múltiplas requisições simultâneas
- **Compressão**: Headers e payload comprimidos
- **Keep-Alive**: Reutilização de conexões

### 3. **Streaming Nativo**
- **Server Streaming**: Servidor envia múltiplas respostas
- **Client Streaming**: Cliente envia múltiplas requisições
- **Bidirectional Streaming**: Comunicação full-duplex

## Arquitetura do Projeto

Nossa implementação demonstra uma arquitetura robusta e bem estruturada:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   gRPC Client   │    │   REST Client   │    │   grpcurl CLI   │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
         ┌─────────────────────────────────────────────────┐
         │              Spring Boot App                    │
         │  ┌─────────────────┐  ┌─────────────────────┐   │
         │  │ REST Controllers│  │   gRPC Services     │   │
         │  └─────────┬───────┘  └─────────┬───────────┘   │
         │            │                    │               │
         │  ┌─────────────────────────────────────────┐   │
         │  │           Business Layer                │   │
         │  │  ┌─────────────┐  ┌─────────────────┐   │   │
         │  │  │  Services   │  │     Mappers     │   │   │
         │  │  └─────────────┘  └─────────────────┘   │   │
         │  └─────────────────────────────────────────┘   │
         │  ┌─────────────────────────────────────────┐   │
         │  │            Data Layer                  │   │
         │  │         ┌─────────────────┐            │   │
         │  │         │   Repository    │            │   │
         │  │         └─────────────────┘            │   │
         │  └─────────────────────────────────────────┘   │
         └─────────────────────────────────────────────────┘
```

## Tecnologias Utilizadas

### **Spring Boot 3.5.7**
Framework principal que oferece:
- **Auto-configuração**: Setup automático do gRPC
- **Dependency Injection**: Gerenciamento de dependências
- **Actuator**: Monitoramento e health checks
- **DevTools**: Hot reload para desenvolvimento

### **Spring gRPC 0.12.0**
Integração oficial do Spring com gRPC:
- **@GrpcService**: Anotação para serviços gRPC
- **Auto-configuração**: Setup automático do servidor
- **Interceptadores**: Middleware para logging e segurança
- **Health Checks**: Monitoramento nativo

### **Protocol Buffers 4.32.1**
Sistema de serialização do Google:
- **Tipagem Forte**: Contratos bem definidos
- **Versionamento**: Evolução compatível de schemas
- **Eficiência**: Serialização binária compacta
- **Multiplataforma**: Geração de código para várias linguagens

### **Lombok**
Redução de boilerplate:
- **@Data**: Getters, setters, equals, hashCode
- **@Builder**: Padrão Builder automático
- **@Slf4j**: Logger automático
- **@RequiredArgsConstructor**: Injeção de dependência

## Implementação Detalhada

### 1. **Protocol Buffer Schema**
```protobuf
syntax = "proto3";

service UserService {
  rpc CreateUser(CreateUserRequest) returns (UserResponse);
  rpc GetUser(GetUserRequest) returns (UserResponse);
  rpc ListUsers(ListUsersRequest) returns (ListUsersResponse);
  rpc GetUsersByStatus(GetUsersByStatusRequest) returns (stream UserResponse);
}

message UserResponse {
  int64 id = 1;
  string name = 2;
  string email = 3;
  int32 age = 4;
  UserStatus status = 5;
}

enum UserStatus {
  UNKNOWN = 0;
  ACTIVE = 1;
  INACTIVE = 2;
  SUSPENDED = 3;
}
```

### 2. **Serviço gRPC**
```java
@GrpcService
@RequiredArgsConstructor
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {
    
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public void createUser(CreateUserRequest request, 
                          StreamObserver<UserResponse> responseObserver) {
        try {
            User user = userMapper.toEntity(request);
            User createdUser = userService.createUser(user);
            UserResponse response = userMapper.toProto(createdUser);
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                .withDescription(e.getMessage())
                .asRuntimeException());
        }
    }
}
```

### 3. **Cliente gRPC**
```java
@Component
public class UserGrpcClient {
    
    private final UserServiceGrpc.UserServiceBlockingStub blockingStub;

    public UserGrpcClient() {
        ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 9090)
            .usePlaintext()
            .build();
        this.blockingStub = UserServiceGrpc.newBlockingStub(channel);
    }

    public UserResponse createUser(String name, String email, int age) {
        CreateUserRequest request = CreateUserRequest.newBuilder()
            .setName(name)
            .setEmail(email)
            .setAge(age)
            .setStatus(UserStatus.ACTIVE)
            .build();
        return blockingStub.createUser(request);
    }
}
```

## Benefícios Demonstrados no Projeto

### 1. **Performance**
- **Serialização Binária**: Protocol Buffers são ~10x menores que JSON
- **HTTP/2**: Multiplexação reduz latência
- **Streaming**: Processamento em tempo real

### 2. **Tipagem Forte**
- **Contratos Definidos**: Schema protobuf garante consistência
- **Validação Automática**: Tipos validados em tempo de compilação
- **IDE Support**: Auto-complete e detecção de erros

### 3. **Streaming Nativo**
```java
@Override
public void getUsersByStatus(GetUsersByStatusRequest request,
                           StreamObserver<UserResponse> responseObserver) {
    List<User> users = userService.getUsersByStatus(status);
    
    users.forEach(user -> {
        UserResponse response = userMapper.toProto(user);
        responseObserver.onNext(response); // Stream cada usuário
    });
    
    responseObserver.onCompleted();
}
```

### 4. **Interoperabilidade**
- **Dual API**: gRPC + REST na mesma aplicação
- **Múltiplos Clientes**: Web, mobile, CLI
- **Linguagens Diversas**: Java, Python, Go, C#, etc.

## Comparação: gRPC vs REST

| Aspecto | gRPC | REST |
|---------|------|------|
| **Protocolo** | HTTP/2 | HTTP/1.1 |
| **Formato** | Protocol Buffers | JSON |
| **Tamanho** | ~87% menor | Maior |
| **Performance** | Alta | Moderada |
| **Streaming** | Nativo | Limitado |
| **Tipagem** | Forte | Fraca |
| **Caching** | Limitado | Excelente |
| **Browser** | Limitado | Nativo |

## Casos de Uso Ideais

### **gRPC é Ideal Para:**
- **Microserviços**: Comunicação interna eficiente
- **APIs de Alta Performance**: Sistemas críticos
- **Streaming**: Dados em tempo real
- **Mobile**: Economia de bateria e dados
- **IoT**: Dispositivos com recursos limitados

### **REST Ainda é Melhor Para:**
- **APIs Públicas**: Facilidade de uso
- **Web Browsers**: Suporte nativo
- **Caching**: Estratégias HTTP
- **Debugging**: Ferramentas familiares

## Monitoramento e Observabilidade

### **Health Checks**
```yaml
spring:
  grpc:
    server:
      health:
        enabled: true
```

### **Métricas**
- **Latência**: Tempo de resposta das chamadas
- **Throughput**: Requisições por segundo
- **Erros**: Taxa de falhas
- **Conexões**: Pools de conexão ativas

### **Logging**
```java
@Slf4j
@GrpcService
public class UserGrpcService {
    
    @Override
    public void createUser(CreateUserRequest request, 
                          StreamObserver<UserResponse> responseObserver) {
        log.info("Creating user: {}", request.getName());
        // ... implementação
    }
}
```

## Segurança

### **TLS/SSL**
```java
NettyServerBuilder.forPort(9090)
    .useTransportSecurity(certFile, keyFile)
    .build();
```

### **Autenticação**
```java
@Component
public class AuthInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        
        String token = headers.get(Metadata.Key.of("authorization", 
                                                  Metadata.ASCII_STRING_MARSHALLER));
        
        if (!isValidToken(token)) {
            call.close(Status.UNAUTHENTICATED, new Metadata());
            return new ServerCall.Listener<ReqT>() {};
        }
        
        return next.startCall(call, headers);
    }
}
```

## Testes

### **Testes Unitários**
```java
@ExtendWith(MockitoExtension.class)
class UserGrpcServiceTest {
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private UserGrpcService grpcService;
    
    @Test
    void shouldCreateUser() {
        // Given
        CreateUserRequest request = CreateUserRequest.newBuilder()
            .setName("Test User")
            .setEmail("test@email.com")
            .build();
        
        // When & Then
        StreamObserver<UserResponse> responseObserver = mock(StreamObserver.class);
        grpcService.createUser(request, responseObserver);
        
        verify(responseObserver).onNext(any(UserResponse.class));
        verify(responseObserver).onCompleted();
    }
}
```

### **Testes de Integração**
```bash
# Teste com grpcurl
grpcurl -plaintext -d '{"name": "Test", "email": "test@email.com"}' \
  localhost:9090 br.com.grpc.user.UserService/CreateUser
```

## Deployment e Produção

### **Docker**
```dockerfile
FROM openjdk:17-jre-slim
COPY target/grpc-app.jar app.jar
EXPOSE 9090 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### **Kubernetes**
```yaml
apiVersion: v1
kind: Service
metadata:
  name: grpc-service
spec:
  selector:
    app: grpc-app
  ports:
  - name: grpc
    port: 9090
    protocol: TCP
  - name: http
    port: 8080
    protocol: TCP
```

## Conclusão

O **gRPC** representa uma evolução natural das APIs, oferecendo performance superior, tipagem forte e recursos avançados como streaming. Nossa implementação com **Spring Boot** demonstra como é possível construir aplicações modernas e eficientes, mantendo a simplicidade e produtividade do ecossistema Spring.

### **Principais Vantagens Demonstradas:**

1. **Performance**: ~87% redução no tamanho dos dados
2. **Tipagem Forte**: Contratos bem definidos com Protocol Buffers
3. **Streaming**: Comunicação em tempo real nativa
4. **Interoperabilidade**: Dual API (gRPC + REST)
5. **Produtividade**: Integração seamless com Spring Boot

### **Quando Usar gRPC:**
- Comunicação entre microserviços
- APIs de alta performance
- Aplicações mobile e IoT
- Sistemas que requerem streaming
- Ambientes onde performance é crítica

O futuro das APIs está na eficiência, e o gRPC, combinado com a robustez do Spring Boot, oferece uma solução completa para construir sistemas distribuídos modernos e performáticos.

---

**Código Fonte**: O projeto completo está disponível com exemplos práticos, testes e documentação detalhada, demonstrando uma implementação production-ready de gRPC com Spring Boot.