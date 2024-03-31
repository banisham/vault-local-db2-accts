#!/bin/bash

# Define the hostname dynamically
hostname=$(hostname)
account="hcvpwdmanid"

# Define the path to the authorized_keys file using the variable
authorized_keys="/home/$account/.ssh/authorized_keys"
authorized_keys_bak="/home/$account/.ssh/authorized_keys_bak"

: <<COMMENT
# Define the API endpoint URL
api_endpoint="http://localhost:8080/ssh-publickey/read"

# Define the request body data as a JSON string
request_body='{
    "host": "'"$hostname"'",
    "account": "'"$account"'",
    "version": "'"$version"'"
}'

# Make a POST request to the API endpoint with the request body
response=$(curl -s -X POST -H "Content-Type: application/json" -d "$request_body" "$api_endpoint")

# Check if the request was successful (HTTP status code 200)
if [[ $? -ne 0 ]]; then
    echo "Error: Failed to make POST request"
    exit 1
fi
COMMENT

# Define the response data as a JSON string
response='{
    "host": "'"$hostname"'",
    "account": "'"$account"'",
    "old-comment": "version-1@hcvpwdmanid",
    "publicKey": "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAgQCAulF437xm1/OMg5rdwd4D44nuh/F4CJSn71BQoRZNSgxyvfctyTwMVDpxoqT1D55fjLJCp4dOJI2Wll+L00zQ+vRsCvwpI6guqrBLKPQbIKQ7IxWq+MZaI6zajSw7u6EPuzOQTs+1W7zw7sgP069HpMbltiQk18B9DOdpLm4oLw== version-2@hcvpwdmanid@ec2-3-83-127-193.compute-1.amazonaws.com"
}'

# Check if the response is empty
if [ -z "$response" ]; then
    echo "Error: Failed to fetch response from the API endpoint."
    exit 1
fi

# Extract the publicKey field from the response
public_key=$(echo "$response" | grep -o '"publicKey": ".*"' | cut -d'"' -f4)

# Extract the comment (for removing public key) from the JSON response
old_comment=$(echo "$response" | grep -o '"old-comment": ".*"' | cut -d'"' -f4)


# Check if the publicKey field is successfully extracted
if [ -z "$public_key" ]; then
    echo "Error: Failed to extract public key from the API response."
    exit 1
fi

# Check if the comment field is successfully extracted
if [ -z "$old_comment" ]; then
    echo "Error: Failed to extract comment field from the API response."
    exit 1
fi

echo "Public key: $public_key"
echo "Old comment: $old_comment"

# Create a temporary file to store the updated authorized_keys
temp_file=$(mktemp)

# Remove the old public key entry from the authorized_keys file
grep -v "$old_comment" "$authorized_keys" > "$temp_file"

echo "Printing the content of $temp_file : "
cat "$temp_file"

# Append the fetched public key to the authorized_keys file
echo "$public_key" >> "$temp_file"

echo "Printing the content of $temp_file : "
cat "$temp_file"

# Backup the original authorized_keys file
cp "$authorized_keys" "$authorized_keys_bak" || { echo "Error: Failed to create backup of authorized_keys."; exit 1; }

# Replace the original authorized_keys file with the updated one
mv "$temp_file" "$authorized_keys" || { echo "Error: Failed to update authorized_keys."; exit 1; }

echo "Public key successfully fetched and updated in authorized_keys."
