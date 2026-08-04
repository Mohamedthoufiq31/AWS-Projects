# aws alb-path based routing

## Project Overview

In this project, I learnt how to:

✅ Create multiple web applications locally

✅ Launch multiple Amazon EC2 Linux instances

✅ Connect to EC2 instances using SSH

✅ Install Apache (HTTPD) Web Server

✅ Host multiple websites on different EC2 instances

✅ Create Target Groups

✅ Configure an Application Load Balancer (ALB)

✅ Implement Path-Based Routing using ALB Listener Rules

✅ Verify Health Checks

✅ Access multiple applications using a single ALB DNS name

This project is designed for beginners who want to understand how AWS Application Load Balancer distributes traffic to multiple applications using URL paths.

## Architecture
<img width="1056" height="577" alt="image" src="https://github.com/user-attachments/assets/0aafd2e6-b8e8-4788-87ef-dc5ca97bedff" />

## Step 1: Create Website Files
Create three web applications.

home/
──> index.html
──> health.html

app1/
──> index.html

app2/
──> index.html
## Step 2: Launch EC2 Instances
| Instance Name | Purpose         |
| ------------- | --------------- |
| Home-Server   | Default Website |
| App1-Server   | App1 Website    |
| App2-Server   | App2 Website    |

Choose:
Amazon Linux 2023
t2.micro

## Step 3: Create Security Group

Allow the following inbound rules.

| Type | Port |
|------|------|
| SSH | 22 |
| HTTP | 80 |

### Why SSH?

SSH allows secure remote login to the EC2 instance.

### Why HTTP?

HTTP allows users to access the hosted websites from a browser.

---

## 🚀 Step 4: Connect to EC2

```bash
ssh -i my-key.pem ec2-user@<public-ip>
```

### Example

```bash
ssh -i my-key.pem ec2-user@54.xxx.xxx.xxx
```

---

## 🚀 Step 5: Install Apache HTTP Server

```bash
sudo yum update -y

sudo yum install httpd -y

sudo systemctl start httpd

sudo systemctl enable httpd
```

Apache is the web server responsible for serving the website.

---

## 🚀 Step 6: Upload Website Files

### Home Server

```text
/var/www/html/

index.html
health.html
```

### App1 Server

```text
/var/www/html/app1/

index.html
```

### App2 Server

```text
/var/www/html/app2/

index.html
```

---

## 🚀 Step 7: Verify Apache

Check the Apache service.

```bash
systemctl status httpd
```

### Expected Output

```text
active (running)
```

---

## 🚀 Step 8: Create Target Groups

Navigate to:

```text
EC2
 ↓
Target Groups
 ↓
Create Target Group
```

Create the following Target Groups.

| Target Group | EC2 Instance |
|--------------|--------------|
| home-tg | Home EC2 |
| app1-tg | App1 EC2 |
| app2-tg | App2 EC2 |

### Health Check Paths

| Target Group | Health Check Path |
|--------------|-------------------|
| home-tg | / |
| app1-tg | /app1 |
| app2-tg | /app2 |

Wait until all Target Groups become **Healthy**.

---

## 🚀 Step 9: Create Application Load Balancer

Navigate to:

```text
EC2
 ↓
Load Balancers
 ↓
Create Load Balancer
 ↓
Application Load Balancer
```

### Configuration

- Internet Facing
- IPv4
- HTTP Listener (Port 80)
- Select two Public Subnets
- Attach Security Group
- Default Target Group → **home-tg**

---

## 🚀 Step 10: Configure Listener Rules

Navigate to:

```text
Application Load Balancer
 ↓
Listeners
 ↓
Manage Rules
```

Create the following listener rules.

| Priority | Path | Action |
|----------|------|--------|
| 1 | /app1* | Forward → app1-tg |
| 2 | /app2* | Forward → app2-tg |
| Default | / | Forward → home-tg |

This enables **Path-Based Routing**.

---

## 🚀 Step 11: Verify the Project

Open the ALB DNS name:

```text
http://ALB-DNS
```

**Expected Result**

```text
Home Page
```

Open:

```text
http://ALB-DNS/app1
```

**Expected Result**

```text
App1
```

Open:

```text
http://ALB-DNS/app2
```

**Expected Result**

```text
App2
```

## 📚 Useful Commands Summary

### Update System Packages

```bash
sudo yum update -y
```

Updates all installed packages to the latest available versions.

---

### Install Apache HTTP Server

```bash
sudo yum install httpd -y
```

Installs the Apache (HTTPD) web server.

---

### Start Apache Service

```bash
sudo systemctl start httpd
```

Starts the Apache web server.

---

### Enable Apache on Boot

```bash
sudo systemctl enable httpd
```

Configures Apache to start automatically whenever the EC2 instance reboots.

---

### Check Apache Status

```bash
systemctl status httpd
```

Verifies whether the Apache service is running.

**Expected Output**

```text
active (running)
```

---

### Create Website Directories

```bash
sudo mkdir -p /var/www/html/app1
sudo mkdir -p /var/www/html/app2
```

Creates directories for the App1 and App2 websites.

---

### List Website Files

```bash
ls -R /var/www/html
```

Displays all files and folders inside the Apache web root.

---

### Restart Apache

```bash
sudo systemctl restart httpd
```

Restarts the Apache service after making changes to the website.

---

## 🚀 Future Improvements

- Configure **HTTPS** using **AWS Certificate Manager (ACM)**.
- Register a **custom domain** using **Amazon Route 53**.
- Add **Auto Scaling Groups** for high availability.
- Deploy applications using **Docker containers**.
- Implement **CI/CD pipelines** using **GitHub Actions** or **Jenkins**.
- Monitor application health and performance using **Amazon CloudWatch**.
- Protect the application with **AWS WAF (Web Application Firewall)**.
- Configure **Amazon CloudFront** to improve content delivery and reduce latency.
- Store static website assets in **Amazon S3**.
- Integrate **Amazon SNS** for notifications and alerts.
- Automate infrastructure provisioning using **Terraform** or **AWS CloudFormation**.
- Add **AWS IAM Roles** to enhance security and follow the principle of least privilege.

---

## ⭐ Support

If you found this project helpful for learning **AWS Application Load Balancer, EC2, Apache HTTP Server, and Path-Based Routing**, please consider **starring ⭐ this repository**. Your support is greatly appreciated!




