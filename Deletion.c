#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]){

	char server_link[100] = "192.168.0.64:/home/test/test";
	char nfs_link[100] = "/home/tracking/test2";
	char command[100];
	int count;

	sprintf(command, "sudo mount -t nfs4 -o minorversion=1 %s %s", server_link, nfs_link);
	system(command);

	sprintf(command, "sudo srm -z %s/%s", nfs_link, argv[1]);
	system(command);

	sprintf(command, "sudo umount -l %s", nfs_link);
	system(command);

	return 0;
}
