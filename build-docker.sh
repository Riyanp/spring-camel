#!/usr/bin/env bash

# Copyright 2018 - 2018 Odenktools.
#
# Permission is hereby granted, free of charge,
# to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
# The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
# IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

cmdname=$(basename $0)

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd $DIR/common
docker build --tag massklik/account-service:${ACCOUNT_VERSION} --build-arg JAR_FILE=target/account-service-${ACCOUNT_VERSION}.jar .
docker tag massklik/account-service:${ACCOUNT_VERSION} massklik/account-service:latest
docker push massklik/account-service:${ACCOUNT_VERSION}
docker push massklik/account-service:latest

cd $DIR/customerservice
docker build --tag massklik/file-service:${FILE_MK_VERSION} --build-arg JAR_FILE=target/file-service-${FILE_MK_VERSION}.jar .
docker tag massklik/file-service:${FILE_MK_VERSION} massklik/file-service:latest
docker push massklik/file-service:${FILE_MK_VERSION}
docker push massklik/file-service:latest

cd $DIR/master-data-service
docker build --tag massklik/master-data-service:${MASTER_DATA_VERSION} --build-arg JAR_FILE=target/master-data-service-${MASTER_DATA_VERSION}.jar .
docker tag massklik/master-data-service:${MASTER_DATA_VERSION} massklik/master-data-service:latest
docker push massklik/master-data-service:${MASTER_DATA_VERSION}
docker push massklik/master-data-service:latest

cd $DIR/product-service/product-command-service
docker build --tag massklik/product-command-service:${PRODUCT_CMD_VERSION} --build-arg JAR_FILE=target/product-command-service-${PRODUCT_CMD_VERSION}.jar .
docker tag massklik/product-command-service:${PRODUCT_CMD_VERSION} massklik/product-command-service:latest
docker push massklik/product-command-service:${PRODUCT_CMD_VERSION}
docker push massklik/product-command-service:latest

cd $DIR/product-service/product-query-service
docker build --tag massklik/product-query-service:${PRODUCT_QRY_VERSION} --build-arg JAR_FILE=target/product-query-service-${PRODUCT_QRY_VERSION}.jar .
docker tag massklik/product-query-service:${PRODUCT_QRY_VERSION} massklik/product-query-service:latest
docker push massklik/product-query-service:${PRODUCT_QRY_VERSION}
docker push massklik/product-query-service:latest

cd $DIR/order-service/order-command-service
docker build --tag massklik/order-command-service:${ORDER_CMD_VERSION} --build-arg JAR_FILE=target/order-command-service-${ORDER_CMD_VERSION}.jar .
docker tag massklik/order-command-service:${ORDER_CMD_VERSION} massklik/order-command-service:latest
docker push massklik/order-command-service:${ORDER_CMD_VERSION}
docker push massklik/order-command-service:latest

cd $DIR/order-service/order-query-service
docker build --tag massklik/order-query-service:${ORDER_QRY_VERSION} --build-arg JAR_FILE=target/order-query-service-${ORDER_QRY_VERSION}.jar .
docker tag massklik/order-query-service:${ORDER_QRY_VERSION} massklik/order-query-service:latest
docker push massklik/order-query-service:${ORDER_QRY_VERSION}
docker push massklik/order-query-service:latest

cd $DIR/merchant-service
docker docker build --no-cache --tag massklik/merchant-service:${MERCHANT_BE_VERSION} --build-arg JAR_FILE=target/merchant-service-${MERCHANT_BE_VERSION}.jar .
docker tag massklik/merchant-service:${MERCHANT_BE_VERSION} massklik/merchant-service:latest
docker push massklik/merchant-service:${MERCHANT_BE_VERSION}
docker push massklik/merchant-service:latest

#EOF