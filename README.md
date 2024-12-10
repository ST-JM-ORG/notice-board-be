# NOTICE BOARD BE

docker login ghcr.io -u pjm529 -p 토큰
## 개발 docker 배포
```bash
docker build -t ghcr.io/pjm529/notice-board-be:dev .
```
```bash
docker push ghcr.io/pjm529/notice-board-be:dev
```

### 개발서버
```bash
docker pull ghcr.io/pjm529/notice-board-be:dev
```
```bash
docker compose down
```
```bash
docker compose up -d
```
``` bash
docker rmi $(docker images -f "dangling=true" -q)
```
