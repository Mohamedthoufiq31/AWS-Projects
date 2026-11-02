# Troubleshooting Guide

This document contains the common issues encountered while building the Enterprise Cloud DevOps Platform and the steps taken to resolve them.

---

# 1. EFS Mount Failed

## Problem

Amazon EFS could not be mounted on the EC2 instance.

Example:

```bash
mount.nfs4: Connection timed out
```

## Cause

- Security Group did not allow NFS (2049)
- Mount Target not created in the Availability Zone
- Incorrect EFS DNS Name

## Solution

- Allowed **TCP 2049** in the EFS Security Group.
- Verified Mount Targets in both Availability Zones.
- Mounted EFS using the correct DNS name.

```bash
sudo mount -t nfs4 -o nfsvers=4.1,rsize=1048576,wsize=1048576,hard,timeo=600,retrans=2,noresvport fs-xxxxxxxx.efs.us-east-1.amazonaws.com:/ /var/www/html
```

---

# 2. Application Load Balancer Health Check Failed

## Problem

Targets were marked as **Unhealthy**.

## Cause

- Apache service was not running.
- Wrong Health Check path.
- HTTP Port 80 blocked.

## Solution

Verified Apache service.

```bash
sudo systemctl status httpd
```

Started Apache.

```bash
sudo systemctl start httpd
```

Enabled Apache.

```bash
sudo systemctl enable httpd
```

Health Check Path

```
/
```

Verified Security Group allowed HTTP (80).

---

# 3. Route 53 DNS Propagation Delay

## Problem

Custom domain was not resolving immediately.

## Cause

DNS propagation takes time after updating records.

## Solution

- Verified Hosted Zone configuration.
- Verified Alias Record pointed to the Application Load Balancer.
- Waited for DNS propagation.

Verified using:

```bash
nslookup mohthoufi.me
```

---

# 4. S3 Backup Upload Failed

## Problem

Backup script failed with:

```text
Unable to locate credentials
```

## Cause

The EC2 instance did not have an IAM Role with S3 permissions.

## Solution

Created an IAM Role.

Attached:

```
AmazonS3FullAccess
```

Assigned the IAM Role to **my-server**.

Verified using:

```bash
aws s3 ls
```

---

# 5. Cron Service Not Installed

## Problem

```bash
crontab: command not found
```

## Cause

Cron service was not installed.

## Solution

Installed Cron.

```bash
sudo dnf install cronie -y
```

Enabled service.

```bash
sudo systemctl enable crond
```

Started service.

```bash
sudo systemctl start crond
```

Verified.

```bash
systemctl status crond
```

---

# 6. Backup Script Not Running

## Problem

Cron job was not executing.

## Cause

- Incorrect cron syntax.
- Script was not executable.

## Solution

Made script executable.

```bash
chmod +x ~/backup/backup.sh
```

Verified cron.

```bash
crontab -l
```

Example.

```cron
15 20 * * * /home/ec2-user/backup/backup.sh
```

---

# 7. Website Not Shared Between EC2 Instances

## Problem

Files created on **my-server** were not visible on **backupserver**.

## Cause

Files were copied before mounting Amazon EFS.

## Solution

Mounted Amazon EFS first.

Copied website files after mounting.

Verified.

```bash
df -h
```

---

# 8. CloudWatch Metrics Not Appearing

## Problem

Memory and Disk metrics were missing.

## Cause

CloudWatch Agent was not installed.

## Solution

Installed CloudWatch Agent.

```bash
sudo dnf install amazon-cloudwatch-agent -y
```

Configured the agent.

Started the service.

Verified metrics under:

```
CloudWatch

↓

CWAgent
```

---

# 9. SNS Email Notifications Not Received

## Problem

No email alerts received.

## Cause

Subscription was not confirmed.

## Solution

Opened the confirmation email.

Clicked:

```
Confirm Subscription
```

Triggered the CloudWatch Alarm again.

---

# 10. Auto Scaling Instances Failed Health Check

## Problem

New EC2 instances were immediately marked unhealthy.

## Cause

Launch Template did not contain the required configuration.

## Solution

Updated the Launch Template.

Verified:

- Apache Installation
- Security Group
- IAM Role
- User Data

---

# 11. CloudFront Not Serving Latest Website

## Problem

Old version of the website continued to load.

## Cause

CloudFront cache.

## Solution

Created an Invalidation.

```
/*
```

Waited for deployment.

---

# 12. Route 53 Domain Not Opening Website

## Problem

Domain did not open the website.

## Cause

Alias record pointed to the wrong destination.

## Solution

Updated the Alias Record.

```
Route 53

↓

Hosted Zone

↓

Alias A Record

↓

Application Load Balancer
```

---

# Lessons Learned

This project provided hands-on experience with:

- Amazon VPC
- EC2
- Amazon EFS
- Application Load Balancer
- Auto Scaling
- CloudWatch
- SNS
- Route 53
- CloudFront
- Amazon S3
- IAM
- Linux Administration
- Bash Scripting
- Cron Job Automation
- High Availability
- Disaster Recovery

---

# Best Practices Followed

- Used Security Groups for least-privilege access.
- Shared website files using Amazon EFS.
- Deployed multiple EC2 instances behind an Application Load Balancer.
- Automated backups using Cron.
- Stored backups in Amazon S3.
- Monitored infrastructure using CloudWatch.
- Configured SNS for alert notifications.
- Used Route 53 for DNS management.
- Accelerated content delivery using CloudFront.

---

# Author

**Mohamed Thoufiq K**

Cloud & DevOps Engineer
