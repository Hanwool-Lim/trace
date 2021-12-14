#include <stdio.h>
#include <stdlib.h>
#include "/usr/include/mysql/mysql.h"
#include <string.h>

int main(void){
	char trace[1024];

	MYSQL *connect;
	MYSQL_RES *result;
	MYSQL_ROW sql_row;
	int query_stat;
	
	char *server = "127.0.0.1"; //no localhost
	char *user = "root";
	char *database = "tracking";

	connect = mysql_init(NULL);

	if(!mysql_real_connect(connect, server, user, "", "tracking", 0, NULL, 0)){
		fprintf(stderr, "mysql connection error %s\n", mysql_error(connect));
		return -1;
	}
	
	sprintf(trace, "select EXISTS (select * from tracking where FileID='%s' limit 1) as success", "test3.txt");
	
	query_stat = mysql_query(connect, trace);
	
	if(query_stat !=0){
		fprintf(stderr, "mysql query error %s\n", mysql_error(connect));
		return -1;
	}

	result = mysql_use_result(connect);
	
	while((sql_row = mysql_fetch_row(result))!=NULL){
		if(atoi(sql_row[0]) == 0)
			printf("hello nonono\n");
		else
			printf("%d\n", atoi(sql_row[0]));
	}

	mysql_close(connect);

	return 0;
}
