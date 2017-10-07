#get VPC ID
export VPC_ID=$(aws ec2 describe-vpcs --query "Vpcs[0].VpcId" --output text)

echo $VPC_ID

#create security group
aws ec2 create-security-group --group-name csye6225-fall2017-webapp --description "CSYE6225-WEBSERVER-SG" --vpc-id $VPC_ID

echo "Group Created"

export SG_ID=$(aws ec2 describe-security-groups --filters "Name=group-name, Values=csye6225-fall2017-webapp" --query "SecurityGroups[0].GroupId" --output text)

echo $SG_ID

#view rules of sg
aws ec2 describe-security-groups --group-name csye6225-fall2017-webapp

#change inbound rules
aws ec2 authorize-security-group-ingress --group-name csye6225-fall2017-webapp --protocol tcp --port 22 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-name csye6225-fall2017-webapp --protocol tcp --port 80 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-name csye6225-fall2017-webapp --protocol tcp --port 443 --cidr 0.0.0.0/0

echo "inbound rules modified"

#get subnet id
export SUBNET_ID=$(aws ec2 describe-subnets --filters "Name=vpc-id, Values=$VPC_ID" --query "Subnets[0].SubnetId" --output text)

#get INSTANCE_ID
export INSTANCE_ID=$(aws ec2 run-instances --image-id ami-cd0f5cb6 --count 1 --instance-type t2.micro --key-name csye6225-aws --security-group-ids $SG_ID --subnet-id $SUBNET_ID --block-device-mappings "[{\"DeviceName\":\"/dev/sdf\",\"Ebs\":{\"VolumeSize\":16,\"VolumeType\":\"gp2\"}}]" --disable-api-termination --query "Instances[0].InstanceId" --output text)

echo in

#wait until insance is in running state
aws ec2 wait --region us-east-1 instance-running --instance-ids $INSTANCE_ID

#fetch IP address
export IP_ADDRESS=$(aws ec2 describe-instances --filters "Name=instance-id,Values=$INSTANCE_ID" --query "Reservations[0].Instances[0].PublicIpAddress" --output text)
echo $IP_ADDRESS

#create record set
aws route53 change-resource-record-sets --hosted-zone-id $1 --change-batch "{\"Changes\":[{\"Action\":\"UPSERT\",\"ResourceRecordSet\":{\"Name\":\"$2\",\"Type\":\"A\",\"TTL\":60,\"ResourceRecords\":[{\"Value\":\"$IP_ADDRESS\"}]}}]}"

