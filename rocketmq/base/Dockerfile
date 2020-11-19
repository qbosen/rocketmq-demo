# Start from a Java image.
FROM openjdk:8-jre-alpine
# RocketMQ version
ENV ROCKETMQ_VERSION 4.7.1

RUN mkdir -p /opt/logs /opt/store

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories \
    && apk --no-cache add curl

RUN cd /opt && curl https://mirrors.tuna.tsinghua.edu.cn/apache/rocketmq/${ROCKETMQ_VERSION}/rocketmq-all-${ROCKETMQ_VERSION}-bin-release.zip -o rocketmq.zip \
    && unzip -q rocketmq.zip -d . && rm rocketmq.zip && mv /opt/rocketmq-all-${ROCKETMQ_VERSION}-bin-release /opt/rocketmq-${ROCKETMQ_VERSION}

ENV ROCKETMQ_HOME /opt/rocketmq-${ROCKETMQ_VERSION}
WORKDIR ${ROCKETMQ_HOME}

#修改RocketMQ 内存参数
RUN cd bin && sed -i 's#-server -Xms4g -Xmx4g -Xmn2g#-server -Xms256m -Xmx256m -Xmn128m#g' runserver.sh
RUN cd bin && sed -i 's# -server -Xms8g -Xmx8g -Xmn4g# -server -Xms256m -Xmx256m -Xmn128m#g' runbroker.sh

VOLUME /opt/logs /opt/store