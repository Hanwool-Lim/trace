
ifneq ($(DEBUG),)
	CFLAGS += -g
endif

.PHONY: all clean run
all: server.token

clean:
	rm -f server.manifest server.manifest.sgx server.sig server.token server.argv

server.argv: server.argv.txt
	gramine-argv-serializer $$(cat $<) >$@

server.manifest: server.manifest.template server.argv
	gramine-manifest \
		$< >$@

enclave-key.pem:
	@echo "generate anonymous key for testing. attach key for production"
	openssl genrsa -3 -out $@ 3072

server.manifest.sgx: server server.manifest enclave-key.pem
	gramine-sgx-sign \
		--key $(word 3, $^) \
		--manifest $(word 2, $^) \
		--output $@

server.sig: server.manifest.sgx

server.token: server.sig
	gramine-sgx-get-token --output $@ --sig $<

run: all
	gramine-sgx server
