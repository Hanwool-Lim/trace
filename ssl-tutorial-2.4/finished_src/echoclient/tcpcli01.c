#include	"unp.h"
#include    <wolfssl/options.h>
#include	<wolfssl/ssl.h>

void sig_handler(const int sig)
{
    printf("\nSIGINT handled.\n");
	wolfSSL_Cleanup();			/* Free wolfSSL */

    /* We can't free the WOLFSSL_CTX here because the 'ctx' variable is
       out of scope.  As such, we let the OS free this resource when the
       program terminates. */

    exit(EXIT_SUCCESS);
}

void
str_cli(FILE *fp, WOLFSSL* ssl)
{
	char	sendline[MAXLINE], recvline[MAXLINE];
	int		n = 0;

	while (Fgets(sendline, MAXLINE, fp) != NULL) {

		if(wolfSSL_write(ssl, sendline, strlen(sendline)) !=
                strlen(sendline)){
			err_sys("wolfSSL_write failed");
		}

		if ((n = wolfSSL_read(ssl, recvline, MAXLINE)) <= 0)
			err_quit("wolfSSL_read error");

		recvline[n] = '\0';
		Fputs(recvline, stdout);
	}
}

int
main(int argc, char **argv)
{
	int					sockfd;
	struct sockaddr_in	servaddr;

	WOLFSSL* ssl;
	WOLFSSL_CTX* ctx;

	/* define a signal handler for when the user closes the program
       with Ctrl-C */
	signal(SIGINT, sig_handler);

	if (argc != 2)
		err_quit("usage: tcpcli <IPaddress>");

    /* Initialize wolfSSL */
	wolfSSL_Init();

	/* Create and initialize WOLFSSL_CTX structure */
	if ( (ctx = wolfSSL_CTX_new(wolfTLSv1_2_client_method())) == NULL) {
		fprintf(stderr, "wolfSSL_CTX_new error.\n");
		exit(EXIT_FAILURE);
	}

	/* Load CA certificates into WOLFSSL_CTX.
	   These will be used to verify the server we connect to */
	if (wolfSSL_CTX_load_verify_locations(ctx,"../certs/ca-cert.pem", 0) !=
            SSL_SUCCESS) {
		fprintf(stderr, "Error loading ../certs/ca-cert.pem, "
                "please check the file.\n");
		exit(EXIT_FAILURE);
	}

	/* Create Socket file descriptor */
	sockfd = Socket(AF_INET, SOCK_STREAM, 0);

	bzero(&servaddr, sizeof(servaddr));
	servaddr.sin_family = AF_INET;
	servaddr.sin_port = htons(SERV_PORT);
	Inet_pton(AF_INET, argv[1], &servaddr.sin_addr);

	/* Connect to socket file descriptor */
	Connect(sockfd, (SA *) &servaddr, sizeof(servaddr));

	/* Create WOLFSSL object */
	if( (ssl = wolfSSL_new(ctx)) == NULL) {
		fprintf(stderr, "wolfSSL_new error.\n");
		exit(EXIT_FAILURE);
	}

    /* pass socket descriptor to wolfSSL */
	wolfSSL_set_fd(ssl, sockfd);

	str_cli(stdin, ssl);		/* do it all */

	wolfSSL_free(ssl);          /* Free SSL object */
	wolfSSL_CTX_free(ctx);      /* Free SSL_CTX object */
	wolfSSL_Cleanup();          /* Free wolfSSL */

	exit(EXIT_SUCCESS);
}

