#include <stdio.h>
#include <stdlib.h>
#include <mysql.h>
#include <string.h>

int main(int argc, char *argv[]){
	char trace[1024];

	MYSQL *connect;
	MYSQL_RES *result;
	
	char *server = "127.0.0.1"; //no localhost
	char *user = "root";
	char *database = "tracking";

	connect = mysql_init(NULL);

	if(!mysql_real_connect(connect, server, user, "", "tracking", 0, NULL, 0)){
		fprintf(stderr, "%s\n", mysql_error(connect));
		return -1;
	}

	result = mysql_use_result(connect);

	sprintf(trace, "insert into tracking (Date, Time, AgentID, DeviceID, ServiceID, FileID, IO) values ('%s', '%s', '%s', '%s', '%s', '%s', '%s')", argv[1], argv[2], argv[3], argv[4], argv[5], argv[6], argv[7]);

	mysql_query(connect, trace);
	result=mysql_use_result(connect);

	mysql_close(connect);

	return 0;
}
