
###  REST API 
#### 1) To generate SSH key pair to configure unix functional account
#### 2) To manage unix local accounts passwords

**_Usage_**

 - Key Generation - http://localhost:8080/unix-server/onboard 
    -    {
         "targetHost": "34.207.85.33",
         "functionalAccount": "hcvpwdmanid"
         }


#### ********************************************************************************

**_Usage_**

- Unix Acct Rotate - http://localhost:8080/unix-local-acct/rotate
   -    {
        "targetHost": "34.207.85.33",
        "targetAccount": "appuser1",
        "newPasswd": "appuser-passwd-22"
        }	
