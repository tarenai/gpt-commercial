#!/bin/bash

mkdir -p ./gpt-commercial/{config,logs} 


# docker-compose
curl -O https://bj-1259324451.cos.ap-beijing.myqcloud.com/test/docker-compose.yaml ./config
# Nginx
curl -O https://bj-1259324451.cos.ap-beijing.myqcloud.com/test/genius_ai_ng.conf ./config
# sql
curl -O https://bj-1259324451.cos.ap-beijing.myqcloud.com/test/init.sql ./config
# application.yaml
curl -O https://bj-1259324451.cos.ap-beijing.myqcloud.com/test/application.yaml ./config


# dist
curl -O https://bj-1259324451.cos.ap-beijing.myqcloud.com/test/dist
# base_web
curl -O https://bj-1259324451.cos.ap-beijing.myqcloud.com/test/base_web





# Prompt the user for the variables
read -p "输入你的C端域名: " c_server_name

read -p "输入你的B端域名: " b_server_name

read -p "输入你的数据库密码: " mysql_password

read -p "输入你的openAiKey: " open_ai_key


# Update the docker-compose.yaml file

perl -i -pe "s/(MYSQL_ROOT_PASSWORD=).*/\1${mysql_password}/" ./config/docker-compose.yaml
perl -i -pe "s/(server_name ).*/\1${c_server_name}/" ./config/genius_ai_ng.conf

# Get the current working directory
current_dir=$(pwd)

# Set the new root directory
new_root="${current_dir}/base_web"

# Replace the root parameter in genius_ai_ng.conf
perl -i -pe "s|(root\s+)[^;]*|\1$new_root|" genius_ai_ng.conf


# Run docker-compose
docker-compose up
