# Arquitetura da Aplicação Spring Boot gRPC

## 📁 Estrutura do Projeto

```
src/main/java/br/com/grpc/
├── 📁 config/           # Configurações
│   ├── GrpcConfig.java         # Configurações gRPC
│   └── DataInitializer.java    # Dados iniciais
├── 📁 controller/       # Controllers REST
│   ├── UserController.java     # CRUD REST
│   ├── HealthController.java   # Health checks
│   └── GlobalExceptionHandler.java # Tratamento de erros
├── 📁 exception/        # Exceções customizadas
│   ├── UserNotFoundException.java
│   └── InvalidUserDataException.java
├── 📁 grpc/            # Serviços gRPC
│   └── UserGrpcService.java    # Implementação gRPC
├── 📁 mapper/          # Conversores
│   └── UserMapper.java         # Proto ↔ Entity
├── 📁 model/           # Modelos de domínio
│   └── User.java              # Entidade User
├── 📁 repository/      # Repositórios
│   └── UserRepository.java    # Repositório em memória
├── 📁 service/         # Lógica de negócio
│   └── UserService.java       # Serviços de usuário
└── GrpcApplication.java       # Classe principal

src/main/proto/
└── user_service.proto         # Definições Protocol Buffer

src/main/resources/
└── application.yml           # Configurações da aplicação
```

## 🏗️ Camadas da Aplicação

### 1. **Camada de Apresentação**
- **gRPC Services**: Endpoints gRPC para comunicação eficiente
- **REST Controllers**: API REST para testes e integração web
- **Exception Handlers**: Tratamento centralizado de erros

### 2. **Camada de Negócio**
- **Services**: Lógica de negócio e validações
- **Mappers**: Conversão entre diferentes representações de dados

### 3. **Camada de Dados**
- **Repositories**: Acesso aos dados (implementação em memória)
- **Models**: Entidades de domínio

### 4. **Camada de Configuração**
- **Config Classes**: Configurações do Spring e gRPC
- **Data Initializers**: População inicial de dados

## 🔧 Tecnologias e Padrões

### **Tecnologias Principais**
- **Spring Boot 3.5.7**: Framework base
- **Spring gRPC**: Integração gRPC
- **Protocol Buffers**: Serialização
- **Lombok**: Redução de boilerplate
- **Maven**: Gerenciamento de dependências

### **Padrões Implementados**
- **Repository Pattern**: Abstração de acesso a dados
- **Service Layer**: Separação da lógica de negócio
- **DTO Pattern**: Objetos de transferência de dados
- **Exception Handling**: Tratamento centralizado de erros
- **Builder Pattern**: Construção de objetos (via Lombok)

## 🚀 Funcionalidades

### **Operações CRUD**
- ✅ Create User (Criar usuário)
- ✅ Read User (Buscar por ID)
- ✅ Update User (Atualizar usuário)
- ✅ Delete User (Remover usuário)
- ✅ List Users (Listar com paginação)
- ✅ Filter by Status (Buscar por status)

### **Recursos Avançados**
- 🔄 **Streaming gRPC**: GetUsersByStatus retorna stream
- 📄 **Paginação**: Suporte a paginação nas listagens
- ✅ **Validação**: Validação de dados de entrada
- 🔍 **Health Checks**: Endpoints de monitoramento
- 📝 **Logging**: Logs estruturados
- 🛡️ **Exception Handling**: Tratamento robusto de erros

## 🌐 APIs Disponíveis

### **gRPC API (Porta 9090)**
- `CreateUser`: Criar usuário
- `GetUser`: Buscar usuário por ID
- `UpdateUser`: Atualizar usuário
- `DeleteUser`: Deletar usuário
- `ListUsers`: Listar usuários (paginado)
- `GetUsersByStatus`: Stream de usuários por status

### **REST API (Porta 8080)**
- `POST /api/users`: Criar usuário
- `GET /api/users/{id}`: Buscar usuário
- `PUT /api/users/{id}`: Atualizar usuário
- `DELETE /api/users/{id}`: Deletar usuário
- `GET /api/users`: Listar usuários
- `GET /api/users/status/{status}`: Buscar por status
- `GET /api/health`: Health check
- `GET /api/info`: Informações da aplicação

## 🔒 Validações Implementadas

### **Validação de Usuário**
- Nome obrigatório e não vazio
- Email obrigatório e formato válido
- Idade entre 0 e 150 anos
- Status válido (ACTIVE, INACTIVE, SUSPENDED)

### **Validação de Parâmetros**
- IDs válidos para operações
- Parâmetros de paginação válidos
- Status válidos para filtros

## 📊 Monitoramento

### **Health Checks**
- Status da aplicação
- Informações de portas
- Timestamp de verificação

### **Logging**
- Logs estruturados com SLF4J
- Rastreamento de operações
- Logs de erro detalhados

## 🧪 Estratégia de Testes

### **Testes Unitários**
- Testes de serviços com Mockito
- Validação de lógica de negócio
- Cobertura de cenários de erro

### **Testes de Integração**
- Testes end-to-end via REST
- Testes de validação
- Testes de cenários completos

### **Ferramentas de Teste**
- **REST**: curl, Postman, HTTPie
- **gRPC**: grpcurl, BloomRPC, Postman

## 🚀 Como Executar

1. **Compilar**: `mvn clean compile`
2. **Executar**: `mvn spring-boot:run`
3. **Testar**: Seguir guia em `TESTING.md`

## 📈 Próximos Passos

- [ ] Integração com banco de dados
- [ ] Autenticação e autorização
- [ ] Métricas e observabilidade
- [ ] Containerização com Docker
- [ ] Testes automatizados completos
- [ ] CI/CD pipeline