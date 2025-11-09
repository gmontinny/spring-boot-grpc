# Guia de Testes - Spring Boot gRPC User Service

## 🚀 Executando a Aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em:
- **gRPC Server**: `localhost:9090`
- **REST API**: `http://localhost:8080`

## 🔧 Testando via REST API

### 1. Health Check
```bash
curl http://localhost:8080/api/health
```

### 2. Informações da Aplicação
```bash
curl http://localhost:8080/api/info
```

### 3. Criar Usuário
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao.silva@email.com",
    "age": 30,
    "status": "ACTIVE"
  }'
```

### 4. Buscar Usuário por ID
```bash
curl http://localhost:8080/api/users/1
```

### 5. Listar Usuários (com paginação)
```bash
curl "http://localhost:8080/api/users?page=0&size=10"
```

### 6. Buscar Usuários por Status
```bash
curl http://localhost:8080/api/users/status/ACTIVE
```

### 7. Atualizar Usuário
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva Santos",
    "email": "joao.santos@email.com",
    "age": 31,
    "status": "ACTIVE"
  }'
```

### 8. Deletar Usuário
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## 🔍 Testando via gRPC (usando grpcurl)

### Instalar grpcurl
```bash
# Windows (Chocolatey)
choco install grpcurl

# macOS (Homebrew)
brew install grpcurl

# Linux
go install github.com/fullstorydev/grpcurl/cmd/grpcurl@latest
```

### 1. Listar Serviços Disponíveis
```bash
grpcurl -plaintext localhost:9090 list
```

### 2. Descrever Serviço
```bash
grpcurl -plaintext localhost:9090 describe br.com.grpc.user.UserService
```

### 3. Criar Usuário
```bash
grpcurl -plaintext -d '{
  "name": "Maria Santos",
  "email": "maria@email.com",
  "age": 25,
  "status": "ACTIVE"
}' localhost:9090 br.com.grpc.user.UserService/CreateUser
```

### 4. Buscar Usuário por ID
```bash
grpcurl -plaintext -d '{"id": 1}' localhost:9090 br.com.grpc.user.UserService/GetUser
```

### 5. Listar Usuários
```bash
grpcurl -plaintext -d '{"page": 0, "size": 10}' localhost:9090 br.com.grpc.user.UserService/ListUsers
```

### 6. Buscar Usuários por Status (Streaming)
```bash
grpcurl -plaintext -d '{"status": "ACTIVE"}' localhost:9090 br.com.grpc.user.UserService/GetUsersByStatus
```

### 7. Atualizar Usuário
```bash
grpcurl -plaintext -d '{
  "id": 1,
  "name": "Maria Santos Silva",
  "email": "maria.silva@email.com",
  "age": 26,
  "status": "ACTIVE"
}' localhost:9090 br.com.grpc.user.UserService/UpdateUser
```

### 8. Deletar Usuário
```bash
grpcurl -plaintext -d '{"id": 1}' localhost:9090 br.com.grpc.user.UserService/DeleteUser
```

## 📊 Exemplos de Respostas

### REST API Response (Criar Usuário)
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao.silva@email.com",
  "age": 30,
  "status": "ACTIVE",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### gRPC Response (Listar Usuários)
```json
{
  "users": [
    {
      "id": "1",
      "name": "João Silva",
      "email": "joao.silva@email.com",
      "age": 30,
      "status": "ACTIVE",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "totalCount": 1,
  "page": 0,
  "size": 10
}
```

## 🧪 Testando Validações

### Dados Inválidos (REST)
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "email": "email-invalido",
    "age": -5,
    "status": "ACTIVE"
  }'
```

### Usuário Não Encontrado
```bash
curl http://localhost:8080/api/users/999
```

## 🔄 Cenários de Teste Completos

### Fluxo CRUD Completo
1. Criar usuário
2. Buscar usuário criado
3. Atualizar usuário
4. Listar usuários
5. Buscar por status
6. Deletar usuário
7. Tentar buscar usuário deletado (deve retornar erro)

### Teste de Paginação
1. Criar múltiplos usuários
2. Testar diferentes páginas e tamanhos
3. Verificar contagem total

### Teste de Streaming gRPC
1. Criar usuários com diferentes status
2. Usar GetUsersByStatus para receber stream
3. Verificar que apenas usuários do status correto são retornados