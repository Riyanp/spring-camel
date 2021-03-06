FROM alpine:3.8

# Default to UTF-8 file.encoding
ENV LANG C.UTF-8
ENV TZ Asia/Jakarta

# add a simple script that can auto-detect the appropriate JAVA_HOME value
# based on whether the JDK or only the JRE is installed
RUN { \
		echo '#!/bin/sh'; \
		echo 'set -e'; \
		echo; \
		echo 'dirname "$(dirname "$(readlink -f "$(which javac || which java)")")"'; \
	} > /usr/local/bin/docker-java-home \
	&& chmod +x /usr/local/bin/docker-java-home

ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk

ENV PATH $PATH:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin

ENV JAVA_VERSION 8u181

ENV JAVA_ALPINE_VERSION 8.181.13-r0

RUN set -x \
	&& apk add --update --no-cache \
		openjdk8="$JAVA_ALPINE_VERSION" bash tzdata && \
		rm -rf /var/cache/apk/* && \
		[ "$JAVA_HOME" = "$(docker-java-home)" ] && \
	ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
	echo $TZ > /etc/timezone

ARG JAR_FILE

COPY ${JAR_FILE} /usr/local/bin/customerservice.jar

COPY wait-for-it.sh /usr/bin

RUN chmod +x /usr/bin/wait-for-it.sh

EXPOSE 6000

# DIRECTORY /applog must same with

RUN mkdir /applog

VOLUME  ["/tmp", "/applog"]

STOPSIGNAL SIGTERM

#EOF