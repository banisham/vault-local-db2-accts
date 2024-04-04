#!/bin/bash

# Function to display usage message
usage() {
    echo "Usage: $0 <target_server> <priv_ssh_key> <target_account> <new_password>"
}

# Check if the required arguments are provided
if [ "$#" -ne 4 ]; then
    usage
    exit 1
fi

# Assign input arguments to variables
target_server="$1"
priv_ssh_key="$2"
target_account="$3"
new_password="$4"
priv_user="hcvpwdmanid"

# SSH command to log in to the remote server and change password
ssh_command="ssh -i $priv_ssh_key $priv_user@${target_server}"
ssh_verfcn_command="ssh -i $priv_ssh_key $target_account@$target_server"



# Function to execute SSH command and handle errors
execute_ssh_command() {
    echo "ssh_command : $ssh_command"
    # Execute SSH command
    ssh_output=$($ssh_command "$@" 2>&1)

    # Check if SSH command was successful
    if [ $? -eq 0 ]; then
        echo "$ssh_output"
    else
        echo "Error executing SSH command: $ssh_output"
        exit 1
    fi
}

# Function to execute SSH command for password change
execute_ssh_passwd_verification() {
    echo "ssh_verfcn_command : $ssh_verfcn_command"

   # Execute SSH command
    ssh_output=$(echo "$new_password" | "$ssh_verfcn_command" "exit" 2>&1)

    # Check if SSH command was successful
    if [ $? -eq 0 ]; then
        echo "Password change verification successful."
        echo "$ssh_output"
    else
        echo "Error executing passwd change verification command: $ssh_output"
        exit 1
    fi
}

# Function to change password using SSH
change_password() {
    # Execute SSH command to change password
    execute_ssh_command "sudo passwd $target_account" << EOF
$new_password
$new_password
EOF
}


# Main function to execute password change
main() {
    # Change password
    change_password
    echo "Password for $target_account changed successfully on $target_server."
#   execute_ssh_passwd_verification
#   echo "Password change verification for $target_account successful on $target_server."

}

# Invoke main function
main
