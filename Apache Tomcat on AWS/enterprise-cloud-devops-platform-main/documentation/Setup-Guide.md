# Enterprise Cloud DevOps Platform - Setup Guide

This guide explains how to deploy the complete project from scratch on AWS.

---

# Prerequisites

- AWS Account
- Domain Name (Optional)
- AWS CLI Installed
- SSH Key Pair
- Amazon Linux 2023 AMI

---

# AWS Services Used

- IAM
- Amazon VPC
- Internet Gateway
- NAT Gateway
- Security Groups
- EC2
- Amazon EFS
- Application Load Balancer
- Auto Scaling Group
- Amazon CloudWatch
- Amazon SNS
- Amazon Route 53
- Amazon CloudFront
- Amazon S3
- Cron

---

# Step 1 - Create VPC

Create a custom VPC.

```
Name

Enterprise-VPC

CIDR

10.0.0.0/16
```

---

# Step 2 - Create Subnets

Create four subnets.

```
Public Subnet 1
10.0.1.0/24

Public Subnet 2
10.0.2.0/24

Private Subnet 1
10.0.3.0/24

Private Subnet 2
10.0.4.0/24
```

---

# Step 3 - Internet Gateway

Create an Internet Gateway.

Attach it to the VPC.

---

# Step 4 - NAT Gateway

Create a NAT Gateway.

- Allocate an Elastic IP
- Place it in Public Subnet 1

---

# Step 5 - Route Tables

## Public Route Table

Destination

```
0.0.0.0/0
```

Target

```
Internet Gateway
```

Associate:

- Public Subnet 1
- Public Subnet 2

---

## Private Route Table

Destination

```
0.0.0.0/0
```

Target

```
NAT Gateway
```

Associate:

- Private Subnet 1
- Private Subnet 2

---

# Step 6 - Security Groups

## ALB Security Group

Inbound

- HTTP (80)
- HTTPS (443)

Source

```
0.0.0.0/0
```

---

## EC2 Security Group

Inbound

- SSH (22) → My IP
- HTTP (80) → ALB Security Group

---

## EFS Security Group

Inbound

- NFS (2049)

Source

```
EC2 Security Group
```

---

# Step 7 - Launch EC2 Instances

Launch two EC2 instances.

AMI

```
Amazon Linux 2023
```

Instance Type

```
t2.micro / t3.micro
```

Attach

- EC2 Security Group
- IAM Role

---

# Step 8 - Install Apache

SSH into both instances.

```bash
sudo dnf update -y

sudo dnf install httpd -y

sudo systemctl enable httpd

sudo systemctl start httpd
```

Verify

```bash
systemctl status httpd
```

---

# Step 9 - Create Amazon EFS

Create

```
Enterprise-EFS
```

Create Mount Targets

- Public Subnet 1
- Public Subnet 2

Attach the EFS Security Group.

---

# Step 10 - Mount EFS

Install EFS utilities.

```bash
sudo dnf install amazon-efs-utils -y
```

Create mount point.

```bash
sudo mkdir -p /var/www/html
```

Mount EFS.

```bash
sudo mount -t nfs4 -o nfsvers=4.1,rsize=1048576,wsize=1048576,hard,timeo=600,retrans=2,noresvport fs-xxxxxxxx.efs.us-east-1.amazonaws.com:/ /var/www/html
```

Verify.

```bash
df -h
```

---

# Step 11 - Deploy Website

Copy the website files.

```bash
sudo cp index.html /var/www/html/

sudo cp style.css /var/www/html/
```

Verify.

```bash
ls /var/www/html
```

---

# Step 12 - Create Application Load Balancer

Create an Internet-facing Application Load Balancer.

Create a Target Group.

Register

- EC2 Instance 1
- EC2 Instance 2

Health Check Path

```
/
```

Verify using the ALB DNS name.

---

# Step 13 - Configure Auto Scaling

Create a Launch Template.

Create an Auto Scaling Group.

```
Minimum Capacity : 2

Desired Capacity : 2

Maximum Capacity : 4
```

Attach the Application Load Balancer.

---

# Step 14 - Configure CloudWatch

Create a CloudWatch Dashboard.

Add widgets for:

- CPU Utilization
- Network In
- Network Out
- Request Count
- Healthy Host Count

Create CloudWatch Alarms.

CPU Alarm

```
CPU > 70%
```

---

# Step 15 - Configure SNS

Create an SNS Topic.

```
CloudAlerts
```

Create an Email Subscription.

Connect the CloudWatch Alarm to the SNS Topic.

Verify that email notifications are received.

---

# Step 16 - Configure Route 53

Create a Hosted Zone.

Create an Alias A Record.

Point the domain to the Application Load Balancer.

Verify that the website is accessible through the domain.

---

# Step 17 - Configure CloudFront

Create a CloudFront Distribution.

Origin

```
Application Load Balancer
```

Enable HTTPS.

Wait for deployment.

Verify that the website is accessible through the CloudFront URL.

---

# Step 18 - Configure Amazon S3

Create an S3 bucket.

```
my-webby-backup
```

Create a folder.

```
backups/
```

Attach an IAM Role with S3 permissions to the primary EC2 instance.

---

# Step 19 - Create Backup Script

Create a directory.

```bash
mkdir ~/backup
```

Create the script.

```bash
nano ~/backup/backup.sh
```

Paste the following.

```bash
#!/bin/bash

DATE=$(date +%F)

zip -r website-$DATE.zip /var/www/html

aws s3 cp website-$DATE.zip s3://my-webby-backup/backups/

rm -f website-$DATE.zip

echo "Backup Completed Successfully"
```

Make it executable.

```bash
chmod +x ~/backup/backup.sh
```

Test it.

```bash
~/backup/backup.sh
```

Verify that the backup ZIP file is uploaded to Amazon S3.

---

# Step 20 - Automate Backup Using Cron

Install Cron if necessary.

```bash
sudo dnf install cronie -y
```

Enable the Cron service.

```bash
sudo systemctl enable crond

sudo systemctl start crond
```

Edit the cron schedule.

```bash
crontab -e
```

Example: Run every day at 8:15 PM.

```cron
15 20 * * * /home/ec2-user/backup/backup.sh
```

Verify.

```bash
crontab -l
```

---

# Project Architecture

```
                    Internet
                        │
                  Amazon Route 53
                        │
                  Amazon CloudFront
                        │
            Application Load Balancer
                        │
             ┌──────────┴──────────┐
             │                     │
      EC2 Instance 1        EC2 Instance 2
             │                     │
             └──────────┬──────────┘
                        │
                    Amazon EFS
                        │
                 Website Files
                        │
          Backup Script (Cron Job)
                        │
                    Amazon S3
```

---

# Verification Checklist

- VPC Created
- Public & Private Subnets Configured
- Internet Gateway Attached
- NAT Gateway Configured
- EC2 Instances Running
- Apache Installed
- EFS Mounted
- Website Accessible
- Application Load Balancer Working
- Auto Scaling Configured
- CloudWatch Dashboard Created
- CloudWatch Alarm Created
- SNS Email Notifications Working
- Route 53 Configured
- CloudFront Distribution Active
- S3 Backup Working
- Cron Job Scheduled

---

# Author

**Mohamed Thoufiq K**

Cloud & DevOps Engineer
