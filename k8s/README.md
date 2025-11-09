# Kubernetes Deployment

## 🚀 Deploy Rápido

```bash
# Build da imagem
docker build -t spring-boot-grpc:latest .

# Deploy no Kubernetes
kubectl apply -k k8s/

# Verificar status
kubectl get pods -n grpc-app
kubectl get services -n grpc-app
```

## 📋 Recursos Criados

- **Namespace**: `grpc-app`
- **Deployment**: 2 réplicas com health checks
- **Service**: LoadBalancer (HTTP:8080, gRPC:9090)
- **ConfigMap**: Configurações específicas K8s
- **HPA**: Auto-scaling 2-10 pods

## 🧪 Testar

```bash
# Obter IP externo
kubectl get service spring-boot-grpc-service -n grpc-app

# Testar REST API
curl http://<EXTERNAL-IP>:8080/api/health

# Testar gRPC
grpcurl -plaintext <EXTERNAL-IP>:9090 list
```

## 🔧 Comandos Úteis

```bash
# Logs
kubectl logs -f deployment/spring-boot-grpc -n grpc-app

# Escalar manualmente
kubectl scale deployment spring-boot-grpc --replicas=3 -n grpc-app

# Port-forward para teste local
kubectl port-forward service/spring-boot-grpc-service 8080:8080 9090:9090 -n grpc-app

# Deletar tudo
kubectl delete -k k8s/
```