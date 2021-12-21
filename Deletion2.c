#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]){

	char nfs_link[100] = "/home/tracking/trace/sksdelete/sksdelete";
	char ip[100] = "10.73.31.201";
	char port[100] = "6380";
	char command[100];

	sprintf(command, "%s %s %s %s", nfs_link,ip,port, argv[1]);
	system(command);


	return 0;
}
