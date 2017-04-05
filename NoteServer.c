#include <sys/types.h>
#include <sys/socket.h>
#include <string.h>
#include <stdio.h>
#include <arpa/inet.h>
#include <time.h>
#include <sys/signal.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <dirent.h>
	
#define HOME "/home/chandram/noteHome/"

void child( int sd);
void createNote(int sd); 
void readNote(int sd);
void deleteNote(int sd);
void listNote(int sd);
void exitNote(int sd);

int main(int argc,char *argv[]){

	int sd,client,portNumber,status;
	socklen_t len;
	struct sockaddr_in servAdd;

	if(argc != 2){
		printf("Usage Error: <Port Number>\n");
		exit(0);
	}

	if((sd = socket(AF_INET,SOCK_STREAM,0))<0){
		printf("Cannot create the socket\n");
		exit(1);
	}

	servAdd.sin_family = AF_INET;
	servAdd.sin_addr.s_addr = htonl(INADDR_ANY);
	sscanf(argv[1],"%d",&portNumber);
	servAdd.sin_port = htons((uint16_t)portNumber);
	
	bind(sd,(struct sockaddr*)&servAdd,sizeof(servAdd));
	
	listen(sd,5);
	
	while(1){
	
		client = accept(sd,(struct sockaddr *)NULL,NULL);
		printf("Got Cient.\n");
		if(!fork())
			child(client);
		close(client);
		waitpid(0,&status,WNOHANG);
	}
}

void child(int sd){

	int actionrecieved,action;
	
	while(1){
		read(sd,&actionrecieved,sizeof(actionrecieved));
		action = ntohl(actionrecieved);
		switch(action){

			case 1:	
				createNote(sd);
				break;
			case 2:
				readNote(sd);
				break;
			case 3:
				listNote(sd);
				break;
			case 4:
				deleteNote(sd);
				break;
			case 5:
				exitNote(sd);
				exit(0);
		}		
	}
}


void exitNote(int sd){

	char message[40];
	sprintf(message,"Exiting the Note Program\n");
	printf("exit");
	send(sd,message,40,0);
}

void listNote(int sd){

	DIR     *d;
	struct dirent *dir;
	char fileName[40];

	d = opendir(HOME);
	if(d){
		while(1){
			if((dir=readdir(d))!= NULL){
				memset(fileName,0,40);
				printf("%s\n",dir->d_name);
				sprintf(fileName,"%s\n",dir->d_name);
				write(sd,fileName,strlen(fileName));
			}
			if((dir=readdir(d))==NULL){
				memset(fileName,0,40);
				sprintf(fileName,"End of the List\n");
				printf("End of the list\n");
				write(sd,fileName,strlen(fileName));
				break;
			}
		}
	}
	closedir(d);
/*	else {
		memset(fileName,0,40);
		sprintf(fileName,"No files to list\n");
		send(sd,fileName,40,0);
	
	}
*/

}

void deleteNote(int sd){
	
	char fileName[15];
	char sendMessage[50];
	char fileToDelete[240];
	int fd;	
	
	memset(sendMessage,0,50);
        sprintf(sendMessage,"Enter File Name to Delete:");
        write(sd,sendMessage,strlen(sendMessage));
        
	recv(sd,fileName,15,0);
	sprintf(fileToDelete,"%s%s",HOME,fileName);
	
	if(remove(fileToDelete)==0)
		send(sd,"File is deleted\n",15,0);
	else
		send(sd,"File is not deleted because file is not exist\n",40,0);				
	
	return;
}
void readNote(int sd){
	
	char sendMessage[50];
	char fileName[15];
	char pathToSave[240];
	int fd,n;
	int sendBuffer[1024];
	
	memset(sendMessage,0,50);
	sprintf(sendMessage,"Enter File Name to view:");
	write(sd,sendMessage,strlen(sendMessage));
	
	recv(sd,fileName,15,0);
	sprintf(pathToSave,"%s%s",HOME,fileName);
	
	fd = open(pathToSave,O_RDONLY);
	
	if(fd<0){
		send(sd,"No file founded\n",15,0);
		return;
	}
	else {
		while((n = read(fd,sendBuffer,sizeof(sendBuffer)))>0){
			send(sd,sendBuffer,n,0);
		}
	}
	memset(sendMessage,0,50);	
	sprintf(sendMessage,"\nEnd of the file\n");
	write(sd,sendMessage,strlen(sendMessage));
	close(fd);
	return;

}

void createNote(int sd){

	int n;
	char message[255];
        char pathToSave[240];
        int currentFileFd;
	char sendMessage[50];
	memset(sendMessage,0,50);
	sprintf(sendMessage,"Enter the name of the Note to be created:");
	write(sd,sendMessage,41);	
	if((n = read(sd,message,255))>0){
        	sprintf(pathToSave,"%s%s",HOME,message);
                if((currentFileFd = open(pathToSave,O_CREAT|O_WRONLY|O_TRUNC,0777))!=-1){
                	sprintf(sendMessage,"%s is created",pathToSave);
                        write(sd,sendMessage,strlen(sendMessage));
                }
        }

        while(1){
                memset(message,0,255);
                if(n=read(sd,message,255)>0){
			if(!strcasecmp(message,"vola\n")){
				break;
			}else{
	                	write(currentFileFd,message,strlen(message));
			}
                }
                else{
                        break;
                }
        }

        close(currentFileFd);
	return;
}
