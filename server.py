import socket

# TCP/IP 소켓을 생성하고
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# 소켓을 포트에 연결
server_address = ('192.168.119.129', 9999)
print('Startinf up on {} port {}'.format(*server_address))
sock.bind(server_address)

# 연결을 기다림
sock.listen()

while True:
    #연결을 기다림
    print('waiting for a connection')
    connection, client_address = sock.accept()
    try:
        print('connection from', client_address)

        #작은 데이터를 받고 다시 전송
        while True:
            data = connection.recv(16)
            print('received {!r}'.format(data))

            break
    finally:
        # 연결 모두 지움
        print("closing current connection")
        connection.close()
