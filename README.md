# trace


디렉터리(클라이언트) :  trace/wolfssl-4.7.0/examples/client

디렉터리(서버) :  trace/wolfssl-4.7.0/examples/server

------------------------------------------------------


WolfSSL 사용법
---

Server : ./server -b -r -v 4

client : ./client -v 4 -h "서버IP 날짜 시간 AgentID DeviceID ServiceID KeyID FileID IO"

  AgentID, DeviceID, ServiceID, KeyID : json 형식을 이용
