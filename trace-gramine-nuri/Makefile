
ifneq ($(DEBUG),)
	CFLAGS += -g
endif

.PHONY: all clean run
all: sksdelete.token

clean:
	rm -f sksdelete.manifest sksdelete.manifest.sgx sksdelete.sig sksdelete.token sksdelete.argv

sksdelete.argv: sksdelete.argv.txt
	gramine-argv-serializer $$(cat $<) >$@

sksdelete.manifest: sksdelete.manifest.template sksdelete.argv
	gramine-manifest \
		$< >$@

enclave-key.pem:
	@echo "generate anonymous key for testing. attach key for production"
	openssl genrsa -3 -out $@ 3072

sksdelete.manifest.sgx: sksdelete sksdelete.manifest enclave-key.pem
	gramine-sgx-sign \
		--key $(word 3, $^) \
		--manifest $(word 2, $^) \
		--output $@

sksdelete.sig: sksdelete.manifest.sgx

sksdelete.token: sksdelete.sig
	gramine-sgx-get-token --output $@ --sig $<

run: all
	gramine-sgx sksdelete
