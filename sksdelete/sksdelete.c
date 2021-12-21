#include <wolfssl/options.h>
#include <wolfssl/ssl.h>
#include <wolfssl/test.h>
#include <string.h>
#include <errno.h>

//int ra_tls_create_key_and_crt_der(uint8_t **, size_t *, uint8_t **, size_t *);

//int load_ra_tls_cert(WOLFSSL_CTX *ctx){
//    int ret;
//    uint8_t *crt, *key;
//    size_t crt_size, key_size;
//
//    ret = ra_tls_create_key_and_crt_der(&key, &key_size, &crt, &crt_size);
//    if(ret){
//        err_sys("create certificate error\n");
//    }
//
//    ret = wolfSSL_CTX_use_certificate_buffer(ctx, crt, crt_size, SSL_FILETYPE_ASN1);
//    if(ret != SSL_SUCCESS){
//        err_sys("certificate loading failed");
//    }
//
//    ret = wolfSSL_CTX_use_PrivateKey_buffer(ctx, key, key_size, SSL_FILETYPE_ASN1);
//    if(ret != SSL_SUCCESS){
//        err_sys("key loading failed");
//    }
//
//    return SSL_SUCCESS;
//}

int main(int argc, char *argv[])
{
    int ret;
    int sockfd;
    WOLFSSL_CTX* ctx;
    WOLFSSL* ssl;
    WOLFSSL_METHOD* method;
    struct  sockaddr_in servAddr;

    // ignore argv[1]
    long unsigned ip = (10 << 24) | (73 << 16) | (31 << 8) | (201 << 0);
    int port = 6380;
    //int port = atoi(argv[2]);
    char *delkey = argv[1];
    size_t delkey_size = strlen(delkey);
    char msg[0x400];
    size_t msg_size = 0;

    char buf[0x400] = {0, };

    msg_size = sprintf(msg, "*2\r\n$3\r\nDEL\r\n$%lu\r\n%s\r\n", (long unsigned) delkey_size, delkey);

    printf("payload dump: ");
    for(int k=0;k<msg_size;++k)
	    printf("%02x ", msg[k]);
    putchar('\n');

    /* create and set up socket */
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    memset(&servAddr, 0, sizeof(servAddr));
    servAddr.sin_family = AF_INET;
    servAddr.sin_addr.s_addr = htonl((uint32_t) ip);
    servAddr.sin_port = htons((uint16_t) port);

    /* connect to socket */
    connect(sockfd, (struct sockaddr *) &servAddr, sizeof(servAddr));

    /* initialize wolfssl library */
    wolfSSL_Init();
    method = wolfTLSv1_2_client_method(); /* use TLS v1.2 */

    /* make new ssl context */
    if ( (ctx = wolfSSL_CTX_new(method)) == NULL) {
        err_sys("wolfSSL_CTX_new error");
    }

    wolfSSL_CTX_set_verify(ctx, WOLFSSL_VERIFY_NONE, 0);

    /* add cert */
    if ( (wolfSSL_CTX_use_certificate_file(ctx, "/home/tracking/ra-tls-cert/cert_pem.crt", SSL_FILETYPE_PEM)) != WOLFSSL_SUCCESS) {
        err_sys("wolfSSL_use_certificate_file error");
    }

    if ( (wolfSSL_CTX_use_PrivateKey_file(ctx, "/home/tracking/ra-tls-cert/key_pem.pem", SSL_FILETYPE_PEM)) != WOLFSSL_SUCCESS) {
        err_sys("wolfSSL_use_PrivateKey_file error");
    }

    /* Add cert to ctx */
//    if (load_ra_tls_cert(ctx) != SSL_SUCCESS) {
//        err_sys("Error loading certs/ca-cert.pem");
//    }

    /* make new wolfSSL struct */
    if ( (ssl = wolfSSL_new(ctx)) == NULL) {
        err_sys("wolfSSL_new error");
    }

    /* Connect wolfssl to the socket, server, then send message */
    wolfSSL_set_fd(ssl, sockfd);
    ret = wolfSSL_connect(ssl);
    if(ret != SSL_SUCCESS){
	char buf[0x80], *errrtn;
	ret = wolfSSL_get_error(ssl, ret);
	errrtn = wolfSSL_ERR_error_string(ret, buf);
        fprintf(stderr, "wolfSSL_connect failed with code %d : %s\n", ret, errrtn);
	exit(ret);
    }
//    wolfSSL_write(ssl, message, strlen(message));
    wolfSSL_write(ssl, msg, msg_size);

    wolfSSL_read(ssl, buf, sizeof(buf) - 1);
    printf("response: %s\n", buf);

    /* frees all data before client termination */
    wolfSSL_free(ssl);
    wolfSSL_CTX_free(ctx);
    wolfSSL_Cleanup();
}
