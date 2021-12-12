import socket
import sys

IOagent = '127.0.0.1'
IOagent_PORT = 9999

SKS = ' '
SKS_PORT = 1111

file_name = sys.argv[1]


# 소켓을 만든다.
client_socket_IO = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket_SKS = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# connect함수로 접속.
client_socket_IO.connect((IOagent, IOagent_PORT))

msg = file_name;
data = msg.encode();
length = len(data);
client_socket_IO.sendall(length.to_bytes(4, byteorder="little"));

# 데이터를 전송한다.
client_socket_IO.sendall(data);

client_socket_SKS.connect((SKS, SKS_PORT))

msg2 = file_name;
data2 = msg2.encode();
length2 = len(data2);
client_socket_SKS.sendall(length.to_bytes(4, byteorder="little"));

# 데이터를 전송한다.
client_socket_SKS.sendall(data2);


client_socket_IO.close();
client_socket_SKS.close();

