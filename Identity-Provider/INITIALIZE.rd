docker build -t identity-provider .
docker images
docker run -d -p 5140:5140 -p 7292:7292 --name Container1 identity-provider
docker-compose up --build
docker ps
docker logs Container1
docker stop Container1
docker rm Container1
docker login
docker tag identity-provider mahandry/identity-provider:latest
docker push mahandry/identity-provider:latest
pg_dump -U postgres -d identity > database.sql
