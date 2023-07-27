#!/bin/bash

# 环境
ACTIVE=${1:-"dev"}
# 端口
APP_PORT=${2:-8080}
# jar存放路径
APP_PATH=${3:-"./target"}
# 指定配置文件
CONFIG_PATH=${4:-"classpath:application-${ACTIVE}.yaml"}
# jar名称
APP_NAME=ai-mechanician.jar
# 日志名称
LOG_NAME=$APP_NAME-all.log



echo "=====开始执行======"

echo "=====sart参数======"
echo "环境:$ACTIVE"
echo "端口:$APP_PORT"
echo "配置文件目录:$CONFIG_PATH"
echo "jar包名称:$APP_NAME"
echo "日志名称:$LOG_NAME"
echo "=====stop参数======"

echo "=====开始打包======"
mvn clean package -Dmaven.test.skip=true
echo "=====打包结束======"

cd "$APP_PATH" || exit
ps -ef | grep $APP_NAME | grep -v grep
if [ $? -ne 0 ]
then
nohup java  -jar $APP_NAME --server.port="$APP_PORT" --spring.profiles.active="$ACTIVE" --spring.config.location="$CONFIG_PATH" >> ./$LOG_NAME 2>&1 &
echo $APP_NAME "start success"
else
ps -ef | grep $APP_NAME | grep -v grep |awk '{print $2}' |xargs kill -9
echo $APP_NAME "stop success"
nohup java  -jar $APP_NAME --server.port="$APP_PORT" --spring.profiles.active="$ACTIVE" --spring.config.location="$CONFIG_PATH" >> ./$LOG_NAME 2>&1 &
echo $APP_NAME "start success"
fi
echo "=====执行结束======"


