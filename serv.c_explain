#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <memory.h>
#include <malloc.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <resolv.h>

// openssl
#include <openssl/rsa.h>
#include <openssl/crypto.h>
#include <openssl/x509.h>
#include <openssl/pem.h>
#include <openssl/ssl.h>
#include <openssl/err.h>

//homedir for key and cert file
#define HOME "./"

//key and cert file
#define CERTF HOME "server.crt"
#define KEYF HOME "server.key"

int OpenListener(int port){
	int sd; //listen sd
	struct sockaddr_in addr; //server address 

	sd = socket(PF_INET, SOCK_STREAM, 0);
	bzero(&addr, sizeof(addr));
	addr.sin_family = AF_INET;
	addr.sin_port = htons(port); 
	addr.sin_addr.s_addr = INADDR_ANY;

	if(bind(sd, (struct sockaddr *) &addr, sizeof(addr)) != 0){
		perror("can't bind port");
		abort(); //terminate program
	}

	if(listen(sd, 10)!=0){
		perror("Can't configure listening port");
		abort();
	}
	return sd;
}

int isRoot(){
	if(getuid()!=0)
		return 0;
	else
		return 1;
}

SSL_CTX* InitServerCTX(void){
	SSL_METHOD *method;
	SSL_CTX *ctx;

	OpenSSL_add_all_algorithms();
	SSL_load_error_strings();

	method = TLSv1_2_server_method();
	ctx = SSL_CTX_new(method);

	if(ctx == NULL){
		ERR_print_errors_fp(stderr);
		abort();
	}
	return ctx;
}

void LoadCertificates(SSL_CTX* ctx, char* CertFile, char* KeyFile){
	if(SSL_CTX_use_certificate_file(ctx, CertFile, SSL_FILETYPE_PEM)<=0){
		ERR_print_errors_fp(stderr);
		abort();
	}

	if(SSL_CTX_use_PrivateKey_file(ctx, KeyFile, SSL_FILETYPE_PEM) <=0){
		ERR_print_errors_fp(stderr);
		abort();
	}
	
	if(!SSL_CTX_check_private_key(ctx)){
		fprintf(stderr, "Private key does not match the public certificate\n");
		abort();
	}
}

int main(int count, char *argc[]){
	SSL_CTX *ctx;
	int server;
	char *portnum;

	if(!isRoot()){
		printf("This program must be run as root/sudo user!!\n");
		exit(0);
	}
	
	if(count!=2){
		printf("Usage : %s <portnum>\n", Argc[0]);
		exit(0);
	}

	SSL_library_init();
}


