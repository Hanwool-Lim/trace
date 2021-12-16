import socket
import sys


SKS = ' '
SKS_PORT = 1111

file_name = sys.argv[1]


# 소켓을 만든다.

client_socket_SKS = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# connect함수로 접속.
client_socket_SKS.connect((SKS, SKS_PORT))

msg = file_name;
data = msg.encode();
length = len(data);
#client_socket_SKS.sendall(length.to_bytes(4, byteorder="little"));

# 데이터를 전송한다.
client_socket_SKS.sendall(data);

client_socket_SKS.close();
