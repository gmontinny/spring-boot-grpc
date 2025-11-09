# Spring Boot gRPC User Service

Aplicação Spring Boot completa demonstrando gRPC com boas práticas, incluindo servidor gRPC, cliente gRPC e API REST para testes.

## 🚀 Tecnologias

- **Spring Boot 3.5.7** - Framework principal
- **Spring gRPC 0.12.0** - Integração gRPC
- **Protocol Buffers 4.32.1** - Serialização
- **Lombok** - Redução de boilerplate
- **Maven** - Gerenciamento de dependências
- **Java 17** - Versão LTS

## 🏗️ Arquitetura do Projeto

```
src/main/java/br/com/grpc/
├── 📁 client/           # Cliente gRPC
│   └── UserGrpcClient.java
├── 📁 config/           # Configurações
│   ├── GrpcConfig.java
│   ├── GrpcServerConfig.java
│   └── DataInitializer.java
├── 📁 controller/       # Controllers REST
│   ├── UserController.java
│   ├── GrpcClientController.java
│   ├── HealthController.java
│   └── GlobalExceptionHandler.java
├── 📁 exception/        # Exceções customizadas
│   ├── UserNotFoundException.java
│   └── InvalidUserDataException.java
├── 📁 grpc/            # Serviços gRPC
│   └── UserGrpcService.java
├── 📁 mapper/          # Conversores Proto ↔ Entity
│   └── UserMapper.java
├── 📁 model/           # Entidades de domínio
│   └── User.java
├── 📁 repository/      # Repositório em memória
│   └── UserRepository.java
├── 📁 service/         # Lógica de negócio
│   └── UserService.java
└── GrpcApplication.java

src/main/proto/
└── user_service.proto   # Definições Protocol Buffer

src/main/resources/
└── application.yml      # Configurações
```

## 📋 Funcionalidades

### APIs Disponíveis
- **gRPC Server** (porta 9090): Comunicação eficiente
- **REST API** (porta 8080): Testes e integração web
- **gRPC Client**: Cliente interno para demonstração

### Operações CRUD
- ✅ Criar usuário
- ✅ Buscar usuário por ID
- ✅ Atualizar usuário
- ✅ Deletar usuário
- ✅ Listar usuários (paginação)
- ✅ Buscar por status (streaming gRPC)

### Recursos Avançados
- 🔒 Validação de dados
- 📝 Logging estruturado
- 🛡️ Tratamento de exceções
- 📊 Health checks
- 🔄 Streaming gRPC
- 📄 Paginação

## 🚀 Executar Aplicação

### Pré-requisitos
- Java 17+
- Maven 3.6+

### Comandos
```bash
# Compilar
mvn clean compile

# Executar
mvn spring-boot:run
```

### Portas
- **gRPC**: localhost:9090
- **REST**: http://localhost:8080

## 🧪 Testes

### REST API (Mais Fácil)

```bash
# Health check
curl http://localhost:8080/api/health

# Criar usuário
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"João","email":"joao@email.com","age":30,"status":"ACTIVE"}'

# Buscar usuário
curl http://localhost:8080/api/users/1

# Listar usuários
curl "http://localhost:8080/api/users?page=0&size=10"

# Buscar por status
curl http://localhost:8080/api/users/status/ACTIVE

# Atualizar usuário
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"João Silva","email":"joao.silva@email.com","age":31,"status":"ACTIVE"}'

# Deletar usuário
curl -X DELETE http://localhost:8080/api/users/1
```

### gRPC Client via REST (Testando Cliente Interno)

```bash
# Criar via cliente gRPC interno
curl -X POST "http://localhost:8080/api/grpc-client/users?name=Maria&email=maria@email.com&age=25&status=ACTIVE"

# Buscar via cliente gRPC interno
curl http://localhost:8080/api/grpc-client/users/1

# Listar via cliente gRPC interno
curl http://localhost:8080/api/grpc-client/users

# Stream por status (logs no console)
curl http://localhost:8080/api/grpc-client/users/stream/ACTIVE
```

**Exemplo de Resposta JSON:**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao.silva@email.com",
  "age": 30,
  "status": "ACTIVE",
  "createdAt": "2025-11-09T09:47:19.0319591",
  "updatedAt": "2025-11-09T09:47:19.0319591"
}
```

### gRPC Direto (grpcurl)

```bash
# Instalar grpcurl
choco install grpcurl  # Windows
brew install grpcurl   # macOS

# Listar serviços
grpcurl -plaintext localhost:9090 list

# PowerShell - usar aspas simples com escape
grpcurl -plaintext -d '{\"name\": \"Pedro\", \"email\": \"pedro@email.com\", \"age\": 35, \"status\": \"ACTIVE\"}' localhost:9090 br.com.grpc.user.UserService/CreateUser

# Buscar usuário
grpcurl -plaintext -d '{\"id\": 1}' localhost:9090 br.com.grpc.user.UserService/GetUser

# Listar usuários
grpcurl -plaintext -d '{\"page\": 0, \"size\": 10}' localhost:9090 br.com.grpc.user.UserService/ListUsers

# Stream por status
grpcurl -plaintext -d '{\"status\": \"ACTIVE\"}' localhost:9090 br.com.grpc.user.UserService/GetUsersByStatus

# Atualizar usuário
grpcurl -plaintext -d '{\"id\": 1, \"name\": \"Pedro Silva\", \"email\": \"pedro.silva@email.com\", \"age\": 36, \"status\": \"ACTIVE\"}' localhost:9090 br.com.grpc.user.UserService/UpdateUser

# Deletar usuário
grpcurl -plaintext -d '{\"id\": 1}' localhost:9090 br.com.grpc.user.UserService/DeleteUser
```

## 📊 Protocol Buffer

### Serviços Definidos
```protobuf
service UserService {
  rpc CreateUser(CreateUserRequest) returns (UserResponse);
  rpc GetUser(GetUserRequest) returns (UserResponse);
  rpc UpdateUser(UpdateUserRequest) returns (UserResponse);
  rpc DeleteUser(DeleteUserRequest) returns (DeleteUserResponse);
  rpc ListUsers(ListUsersRequest) returns (ListUsersResponse);
  rpc GetUsersByStatus(GetUsersByStatusRequest) returns (stream UserResponse);
}
```

### Enum Status
```protobuf
enum UserStatus {
  UNKNOWN = 0;
  ACTIVE = 1;
  INACTIVE = 2;
  SUSPENDED = 3;
}
```

## 🐳 Docker

### Build e Execução
```bash
# Build da imagem
docker build -t spring-boot-grpc .

# Executar container
docker run -p 8080:8080 -p 9090:9090 spring-boot-grpc

# Com Docker Compose
docker-compose up -d

# Logs
docker logs spring-boot-grpc -f
```

### Características do Docker
- **Multi-stage build**: Otimização de tamanho
- **Usuário não-root**: Segurança
- **Health check**: Monitoramento automático
- **Alpine Linux**: Imagem mínima

## ☸️ Kubernetes

### Deploy Rápido
```bash
# Build da imagem
docker build -t spring-boot-grpc:latest .

# Deploy no Kubernetes
kubectl apply -k k8s/

# Verificar status
kubectl get pods -n grpc-app
kubectl get services -n grpc-app
```

### Recursos Kubernetes
- **Namespace**: `grpc-app` para isolamento
- **Deployment**: 2 réplicas com health checks
- **Service**: LoadBalancer (HTTP:8080, gRPC:9090)
- **ConfigMap**: Configurações específicas K8s
- **HPA**: Auto-scaling 2-10 pods baseado em CPU/memória

### Testar no Kubernetes
```bash
# Obter IP externo
kubectl get service spring-boot-grpc-service -n grpc-app

# Testar REST API
curl http://<EXTERNAL-IP>:8080/api/health

# Testar gRPC
grpcurl -plaintext <EXTERNAL-IP>:9090 list

# Port-forward para teste local
kubectl port-forward service/spring-boot-grpc-service 8080:8080 9090:9090 -n grpc-app
```

## 🔧 Configurações

### application.yml
```yaml
spring:
  application:
    name: grpc-user-service
  grpc:
    server:
      port: 9090
      reflection:
        enabled: true
      health:
        enabled: true

logging:
  level:
    br.com.grpc: DEBUG
```

## 🏆 Padrões Implementados

### Arquitetura em Camadas
- **Apresentação**: Controllers REST + Serviços gRPC
- **Negócio**: Services com validações
- **Dados**: Repository em memória
- **Configuração**: Configs e inicializadores

### Boas Práticas
- ✅ Separação de responsabilidades
- ✅ Tratamento centralizado de erros
- ✅ Validação de dados
- ✅ Logging estruturado
- ✅ Mappers para conversão
- ✅ Cliente e servidor gRPC
- ✅ Dual API (gRPC + REST)
- ✅ Documentação completa

## 🧪 Dados de Teste

A aplicação inicializa com 3 usuários:
1. João Silva (ACTIVE)
2. Maria Santos (ACTIVE) 
3. Pedro Oliveira (INACTIVE)

## 🔄 Fluxo Completo de Teste

1. **Iniciar aplicação**: `mvn spring-boot:run`
2. **Verificar logs**: Deve mostrar "gRPC Server started on port: 9090"
3. **Health check**: `curl http://localhost:8080/api/health`
4. **Listar usuários iniciais**: `curl http://localhost:8080/api/users`
5. **Testar gRPC**: `grpcurl -plaintext localhost:9090 list`
6. **Criar novo usuário**: Via POST REST ou gRPC
7. **Testar operações CRUD**: Create, Read, Update, Delete
8. **Testar streaming**: GetUsersByStatus
9. **Testar validações**: Dados inválidos
10. **Testar cliente gRPC**: Via endpoints `/api/grpc-client/*`

## ⚠️ Troubleshooting

### gRPC não conecta
- Verificar se aplicação está rodando: logs devem mostrar "gRPC Server started on port: 9090"
- Testar porta: `netstat -an | findstr :9090`
- No PowerShell, usar aspas simples: `grpcurl -plaintext -d '{\"id\": 1}' localhost:9090 ...`
- Alternativa: testar via REST em `http://localhost:8080/api/grpc-client/*`

### Reflection API
- Se erro "server does not support reflection": servidor gRPC configurado com `ProtoReflectionService`
- Usar `grpcurl -plaintext localhost:9090 list` para verificar

### Cliente gRPC Interno
- Erro 500 em `/api/grpc-client/*`: Problema de serialização JSON resolvido
- Respostas convertidas de Protocol Buffer para JSON
- Tratamento de erros com mensagens descritivas
- Timeout configurado para evitar travamentos

## 📈 Próximos Passos

- [ ] Banco de dados (PostgreSQL/MySQL)
- [ ] Autenticação JWT
- [ ] Métricas Prometheus
- [x] Docker containerização
- [x] Kubernetes deployment
- [ ] Testes automatizados
- [ ] CI/CD pipeline
- [ ] Load balancing
- [ ] Service mesh (Istio)

## 🎆 Funcionalidades Implementadas

✅ **Servidor gRPC** - Porta 9090 com reflection  
✅ **API REST** - Porta 8080 para testes  
✅ **Cliente gRPC Interno** - Demonstração de uso  
✅ **Streaming gRPC** - GetUsersByStatus  
✅ **Paginação** - ListUsers com page/size  
✅ **Validações** - Dados de entrada  
✅ **Tratamento de Erros** - Global exception handler  
✅ **Health Checks** - Monitoramento  
✅ **Docker** - Containerização completa  
✅ **Kubernetes** - Deploy com HPA e LoadBalancer  
✅ **Logging** - Estruturado com SLF4J  
✅ **Dados Iniciais** - 3 usuários de exemplo