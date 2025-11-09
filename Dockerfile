# Multi-stage build para otimizar tamanho da imagem
FROM maven:3.9-eclipse-temurin-21 AS build

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivos de configuração do Maven
COPY pom.xml .
COPY src ./src

# Compilar aplicação
RUN mvn clean package -DskipTests

# Imagem final otimizada
FROM eclipse-temurin:21-jre-alpine

# Criar usuário não-root para segurança
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Definir diretório de trabalho
WORKDIR /app

# Copiar JAR da aplicação
COPY --from=build /app/target/*.jar app.jar

# Alterar proprietário dos arquivos
RUN chown -R appuser:appgroup /app

# Usar usuário não-root
USER appuser

# Expor portas
EXPOSE 8080 9090

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/health || exit 1

# Comando de execução
ENTRYPOINT ["java", "-Dgrpc.client.host=localhost", "-Dgrpc.client.port=9090", "-jar", "/app/app.jar"]