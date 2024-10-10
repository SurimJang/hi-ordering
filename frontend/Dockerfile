FROM webratio/nodejs-http-server

# /tmp 디렉토리 생성
VOLUME /tmp
# 빌드된 Vue.js 파일을 Nginx 또는 Node.js 서버로 추가
ADD dist /opt/www
# 실행 스크립트 추가
ADD run.sh /opt/run.sh
# 빌드 시점에 API 호스트 환경 변수 설정
ARG VUE_APP_API_HOST
ENV VUE_APP_API_HOST=$VUE_APP_API_HOST
# 타임존 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 8080 포트 노출
EXPOSE 8080
# 실행 스크립트 시작
ENTRYPOINT ["sh", "/opt/run.sh"]
