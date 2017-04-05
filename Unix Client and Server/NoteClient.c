#include <sys/types.h>
#include <sys/socket.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <time.h>
#include <sys/signal.h>
#include <dirent.h>

void createNote(int server,int sendAction);
void readNote(int server,int sendAction);
void deleteNote(int server,int sendAction);
void listNote(int server,int sendAction);
void exitNote(int server,int sendAction);

int main(int argc,char *argv[]){
	
	int server,portNumber,pid,n;
	socklen_t len;
	struct sockaddr_in servAdd;
	int sendAction;

	if(argc !=3){
	
		printf("Usage Error: IP and PORT#");
		exit(0);
	}
	
	if((server = socket(AF_INET,SOCK_STREAM,0))<0){
	
		fprintf(stderr,"cannot create socket\n");
		exit(1);
	}

	servAdd.sin_family = AF_INET;
	sscanf(argv[2],"%d",&portNumber);
	servAdd.sin_port = htons((uint16_t)portNumber);

	if(inet_pton(AF_INET,argv[1],&servAdd.sin_addr)<0){
		
		fprintf(stderr,"inet_pton() has failed\n");
		exit(2);
	}
	
	if(connect(server,(struct sockaddr *)&servAdd,sizeof(servAdd))<0){
		
		fprintf(stderr,"connect() has failed,exiting\n");
		exit(3);	
	}

	/* Main Logic Starts */

	while(1){
               
                int action;
		printf("/*****************SIMPLE NOTE*********************/\n");
		printf("Enter the number to do the action\n");
		printf("1.Create Note\n");
		printf("2.Read Note\n");
		printf("3.Display Name of the notes\n");
		printf("4.Delete Note\n");
		printf("5.Exit to close the application\n");
		printf("/************************************************/\n");
		scanf("%d",&action); 	
		
		sendAction = htonl(action);		
		switch(action){

			case 1:
				createNote(server,sendAction);
				break;
			case 2:
				readNote(server,sendAction);
				break;
			case 3: 
				listNote(server,sendAction);	
				break;         		
		        case 4:
				deleteNote(server,sendAction);
				break;			    
                        case 5:
				exitNote(server,sendAction);

		}
     	}		
}

void exitNote(int server,int sendAction){
	
	char message[40];
	int n;
	write(server,&sendAction,sizeof(sendAction));
	memset(message,0,255);
	if((n = read(server,message,sizeof(message),0))>0){
		printf("%s",message);
	}
	exit(0);
}

void listNote(int server,int sendAction){
	
	char message[255]; 
	char inputbuffer[255];
	int n;
        
	write(server,&sendAction,sizeof(sendAction));
        printf("Listing the files\n");
	
	while(1){
		memset(message,0,255);
		if(n = read(server,message,255)){
			if(!strncmp("End of the list",message,0)){
				fprintf(stderr,"%s\n",message);
				break;	
			}
			else fprintf(stderr,"%s\n",message);
		}

	}	


	/*while(1){
		memset(message,0,255);
		if(((n = recv(server,message,sizeof(message),0))>0)){
			if(strcasecmp(message,"End of the list")){
                                 fprintf(stderr,"%s",message);
                                 break;
                        }

			else  fprintf(stderr,"%s",message);
		}
	}*/
				
}

void deleteNote(int server,int sendAction){
	
 	char message[255];
        char fileName[15];
        int n;
        write(server,&sendAction,sizeof(sendAction));
        read(server,message,255);
        fprintf(stderr,"%s",message);

        scanf("%s",fileName);
	send(server,fileName,sizeof(fileName),0);

	memset(message,0,255);
        if((n = recv(server,message,sizeof(message),0))>0){
     		printf("%s\n",message);
	}
}

void readNote(int server,int sendAction){
	
	char message[255];
	char fileName[15];
	int n;
	write(server,&sendAction,sizeof(sendAction));
	read(server,message,255);
	fprintf(stderr,"%s",message);
	
	scanf("%s",fileName);
	
	send(server,fileName,sizeof(fileName),0);
	
	printf("Content of the file:\n");

	while(1){
		memset(message,0,255);

		if((n = recv(server,message,sizeof(message),0))>0)
			if(strcasecmp(message,"End of the file")){
				 printf("%s",message);
				 break;	
			}
			else printf("%s",message);
	}
}

void createNote(int server, int sendAction){

	int n;
	char message[255];
        char fileName[15];
        char inputbuffer[240];
	int bytesread;

	write(server,&sendAction,sizeof(sendAction));			
	read(server,message,255);
	fprintf(stderr,"%s",message);				

        scanf("%s",fileName);
        if(write(server,fileName,strlen(fileName)) < 0){ printf("write error");}
        if(n = read(server,message,255)){
        	message[n] = '\0';
                fprintf(stderr,"%s\n",message);
                printf("\nStart Writing Note..$ to save and exit note\n:");
         }

        while(1){
                memset(inputbuffer,0,240);
                bytesread = read(fileno(stdin),inputbuffer,240);
                if(!strncmp("vola",inputbuffer,bytesread-1)) {
			write(server,inputbuffer,strlen(inputbuffer));
			break;
		}else {	
			write(server,inputbuffer,strlen(inputbuffer));

                }
	}	
}

