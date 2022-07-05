# loan_platform
The core code of the platform.
# Development Environment
Basic environment for project development
 Tools&Framework  | Version
 ---- | -----
Node.js |	v14.16.1
MySQL |	5.7.34
JDK |	1.8
Maven |	3.6.3
Nacos |	1.4.0
Redis |	6.2.3
RabbitMQ | 3.8.1
# Development environment instruction
Before code development, we need to configure the environment of the tools used. Here are some guidelines. If you need more instructions on the configuration of the project, please reach out to me: klein703703@gmail.com.
## Redis
In this project, we need to install docker under centos7
First uninstall the old version of docker:
```shell
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
```
Then install docker dependency:
```shell
sudo yum install -y yum-utils
```
Then configure the address of Docker
```shell
sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo
```
After that, start installing docker
```shell
sudo yum install docker-ce docker-ce-cli containerd.io
```
Then start Docker
```shell
sudo systemctl start docker
```
Then enable dokcer self startup
```shell
sudo systemctl enable docker
```
****
Then install redis. First, download the redis image:
```shell
docker pull redis
```
Create directory structure:
```shell
mkdir -p /mydata/redis/conf
touch /mydata/redis/conf/redis.conf
```
Create an instance of redis and start it:
```shell
docker run -p 6379:6379 --name redis\
                  -v /mydata/redis/data:/data\
                  -v /mydata/redis/conf/redis.conf:/etc/redis/redis.conf\
                  -d redis redis-server /etc/redis/redis.conf
```
Use this command to operate redis client:
```shell
docker exec -it redis redis-cli
```
Now redis does not support data persistence, we update it by jumping to /mydata/redis/conf directory and modify redis Conf file:
```shell
appendonly yes
```
Restart the Redis:
```shell
docker restart redis
```
Configure the following to enable redis to start with the start of docker:
```shell
docker update redis --restart=always
```
****
Now we turn off the firewall of centos7 to avoid unnecessary troubles:
```shell
systemctl stop firewalld
```

## RabbitMQ
Download rabbitmq using docker:
```shell
docker pull rabbitmq:3.8.1-management
```
Create directory structure:
```shell
cd /mydata
mkdir -p /rabbitmq/data
```
And then start RabbitMQ:
```shell
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 -v /mydata/rabbitmq/data:/var/lib/rabbitmq --hostname myRabbit -e RABBITMQ_DEFAULT_VHOST=my_vhost  -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin rabbitmq:3.8.1-management
```
Set rabbitmq to start up with docker start up：
```shell
docker update rabbitmq --restart=always
```
通过主机ip访问rabbitmq：
my ip address here is http://192.168.56.101:15672/#/users
![image](https://user-images.githubusercontent.com/89323566/176047460-73b8fa41-8e7a-478f-8c37-6942c2ec5f22.png)
The default username and password are both:admin
You can set rabbitmq according to your needs then.
## Nacos Registry
The modules in this project are based on microservice. So we need a registry for microservice discovery. Here I select Nacos from Alibaba. You can select other registry either. Here is the instruction about Nacos:
Download address: https://github.com/alibaba/nacos/releases/tag/1.4.0 , page to bottom:
![image](https://user-images.githubusercontent.com/89323566/176047974-e5a844b9-6d19-40ae-afcd-c185b79a9ab9.png)
Download to tar.gz is the suffix of the compressed package. If you want to deploy it on windows, download the zip compressed package. The deployment method is the same.

After downloading, upload it to centos7 and start it (start it in the bin directory of Nacos):
```shell
cd nacos
cd bin
./startup.sh -m standalone
```
The startup of Nacos also needs the support of JDK. You need to set it in CentOS7.

# Start up the project
When everything is ready, we can start the project. The whole system is divided into four parts:
1. Backend system：https://github.com/zhezhang01/loan_platform
2. Third party payment interface: https://github.com/zhezhang01/loan_platform_thirdPartyPaymentInterface
3. User front-end: https://github.com/zhezhang01/loan_platform-frontend
4. Administrator front-end: https://github.com/zhezhang01/loan_platform-Administrator-frontend

The background interface and payment system are both developed using springboot. The background management page and the user front-end page are developed using Vue + elementui. Let's start the front-end project, clone the front-end project locally, and execute the instructions:
```shell
npm install
npm run dev
```
## Administrator interface display
The back-end management page is as follows:
![image](https://user-images.githubusercontent.com/89323566/176051690-7ce7799f-215b-46e1-a35c-09bf841c44d8.png)
The Points Management page, which used to manage the borrowing limit of different credit users
![image](https://user-images.githubusercontent.com/89323566/176052498-435d9fd2-4d3d-437d-9331-ed4cbdadf7a1.png)
The member management page, which used to manage the member existed in this system.
![image](https://user-images.githubusercontent.com/89323566/176052679-d0392c21-1c22-48be-9244-e470d2225cd0.png)
The sign in record of a user
![image](https://user-images.githubusercontent.com/89323566/176052775-f4cf5e1e-e193-47ac-8406-39ab9b7a5f69.png)
The borrower management page, which used to manage the borrower registered in this system.
![image](https://user-images.githubusercontent.com/89323566/176052821-20c63add-23f4-4f49-8783-e8c611bec221.png)
The basic info of the borrower, including Photo id, vehicle registration pictures etc. The administrator can review the user information here to determine whether to grant the borrowing qualification.
![image](https://user-images.githubusercontent.com/89323566/176052908-5b638fd2-4708-44b7-9e04-3c0f4769d36c.png)
The borrow management page, which used to manage the loan request initiated by the borrower.
![image](https://user-images.githubusercontent.com/89323566/176053102-e9d5fca6-a585-4394-8956-c237d1a701b4.png)
![image](https://user-images.githubusercontent.com/89323566/176053214-b293ee2a-33b1-44bf-a91f-7b6687416f91.png)
The bid management page, which used to manage the bid and check how many investors have invested the loan.
![image](https://user-images.githubusercontent.com/89323566/176053293-6cc16ace-08bd-4de5-97a2-12085e7e819f.png)
![image](https://user-images.githubusercontent.com/89323566/176053379-ac914904-44aa-4bca-a83f-da9c5d86172c.png)

## Userinterface display
The main page of the user front-end
![image](https://user-images.githubusercontent.com/89323566/176054037-66246946-300d-4ded-b3fc-e806c8877b73.png)
![image](https://user-images.githubusercontent.com/89323566/176054057-37b1776d-b5cb-4110-a30d-f2753ce31cc7.png)
User sing up page:
There are tow characters here,investor means you will be registered as the character who will invest for a loan.
Borrower means you are about to register as the people who request to borrow money.
![image](https://user-images.githubusercontent.com/89323566/176054110-a5d2e9e4-e489-4a55-a06b-75cc9c19b953.png)
After registration, the user will receive the verification code from Redis
![image](https://user-images.githubusercontent.com/89323566/176054310-b53fb0e2-bf0d-4a12-a87c-571056df9144.png)
Fill the verification code and sign up process is up.
![image](https://user-images.githubusercontent.com/89323566/176054428-1f8d2553-db98-4f16-9b53-dc172a76d26d.png)
The sign in page of the system, registered user can sign in through this page:
![image](https://user-images.githubusercontent.com/89323566/176054508-4142cb81-137d-4cd6-a0ba-6d8c0f217449.png)
The page after signing in:
![image](https://user-images.githubusercontent.com/89323566/176054592-8b957a45-5117-4b2d-92ad-ee79320b7316.png)
As a borrower, we can bind the third party platform for money transaction and then request a loan:
![image](https://user-images.githubusercontent.com/89323566/176055467-94b77e13-2632-402f-95e5-a49b8b10fb54.png)
Third party platform form page,user can bind their bank account here
![image](https://user-images.githubusercontent.com/89323566/176055670-97293b8e-7810-44ce-aa17-813821528d49.png)
After binding the third party platform, the user now are eligible to borrow, the brrow info will be displayed here and all investors can invest for this loan from now on:
![image](https://user-images.githubusercontent.com/89323566/176056493-cc90d493-e8a2-47d9-a349-7ecd0114285d.png)
The investor character: The sign in and bind process was same as the borrower
The investor and borrower can recharge some money into their account through thrird-party platform and withdraw the money the made from srb platform:
![image](https://user-images.githubusercontent.com/89323566/176057402-607f0753-42f8-4dc5-b193-42b925203669.png)
![image](https://user-images.githubusercontent.com/89323566/176057467-7bcb3715-b92f-46a0-b1f3-7e15e8cbfd65.png)
The investor can investor for a loan at the page we saw above.
At the detailed page, the investor can select how much money they want to invest and the details of the loan:
![image](https://user-images.githubusercontent.com/89323566/176057682-7c566f2e-d8c4-47f5-b4e5-431c2b8a0bb1.png)
***
Some other pages display:
Q&A:
![image](https://user-images.githubusercontent.com/89323566/176058272-e6ccd898-8e45-49b3-804d-9cbfa6b8e1f0.png)
About us:
![image](https://user-images.githubusercontent.com/89323566/176058371-261a5924-e519-4aad-8bdb-c7e3a7bce6ec.png)
![image](https://user-images.githubusercontent.com/89323566/176058438-f7c7904b-2466-4546-995a-ea196823ec8a.png)


















