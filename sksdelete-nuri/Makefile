
CFLAGS += -g -L ../wolfssl-4.7.0/src/.libs/ -lwolfssl -ldl -lra_tls_attest

all: sksdelete

clean:
	rm -f sksdelete

%: %.c
	$(CC) -o $@ $< $(CFLAGS)
