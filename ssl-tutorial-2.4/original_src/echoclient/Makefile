CC=gcc
CFLAGS=-Wall -I ../include
DEPS = ../include/unp.h
OBJ = echoclient.o

%.o: %.c $(DEPS)
	$(CC) -c -o $@ $< $(CFLAGS)

echoclient: ../lib/*.c tcpcli01.c
	$(CC) -o echoclient ../lib/*.c tcpcli01.c -I ../include
	
.PHONY: clean

clean:
	rm -f *.o echoclient