

mvn clean install -pl xxl-job-admin-cloud  -am  -Dmaven.test.skip=true

docker buildx build --platform linux/amd64,linux/arm64/v8,linux/arm/v7 -t sweeter/xxl-job-pro-admin:v1.0.0  --push ./xxl-job-admin-cloud/
