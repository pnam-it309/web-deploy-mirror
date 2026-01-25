# Production Configuration Guide

## Required Environment Variables

Create a `.env.production` file with the following:

```bash
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/catalog_web?useSSL=true&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=your_db_user
SPRING_DATASOURCE_PASSWORD=your_secure_password

# Email (Gmail example)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-specific-password

# Admin Alerts
ADMIN_ALERTS_EMAIL_ENABLED=true
ADMIN_ALERTS_EMAIL_RECIPIENTS=admin1@company.com,admin2@company.com
ADMIN_ALERTS_SLACK_ENABLED=true
ADMIN_ALERTS_SLACK_WEBHOOK=https://hooks.slack.com/services/YOUR/WEBHOOK/URL

# AWS S3 (for backups)
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key
AWS_REGION=ap-southeast-1
BACKUP_S3_BUCKET=your-catalog-backups

# Security
JWT_SECRET=your-very-long-and-secure-secret-key-at-least-256-bits

# Frontend URL
FRONTEND_URL=https://your-domain.com
BACKEND_URL=https://api.your-domain.com

# VAPID Keys (for Push Notifications)
# Generate new keys: https://vapidkeys.com/
VAPID_PUBLIC_KEY=your-generated-public-key
VAPID_PRIVATE_KEY=your-generated-private-key

# Media Validation
WATERMARK_ENABLED=true
WATERMARK_TEXT=Your Company Name
```

## Database Migration Steps

### 1. Run Migration for TwoFactorAuth

```sql
-- Connect to your database
mysql -u root -p catalog_web

-- Run the migration
source src/main/resources/db/migration/V1__create_two_factor_auth_table.sql

-- Verify
SHOW TABLES LIKE 'two_factor_auth';
DESC two_factor_auth;
```

### 2. Verify All Tables

```sql
-- Check all tables exist
SHOW TABLES;

-- Key tables to verify:
-- admin, two_factor_auth, apps, domains, technologies,
-- push_subscription, email_subscription, etc.
```

## Slack Webhook Setup

1. Go to https://api.slack.com/apps
2. Create new app or select existing
3. Enable "Incoming Webhooks"
4. Add webhook to workspace
5. Copy webhook URL
6. Paste into `ADMIN_ALERTS_SLACK_WEBHOOK`

## AWS S3 Setup for Backups

### 1. Create S3 Bucket

```bash
aws s3 mb s3://fpl-catalog-backups --region ap-southeast-1
```

### 2. Set Bucket Policy

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AllowBackupAccess",
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::ACCOUNT-ID:user/backup-user"
      },
      "Action": [
        "s3:PutObject",
        "s3:GetObject",
        "s3:ListBucket",
        "s3:DeleteObject"
      ],
      "Resource": [
        "arn:aws:s3:::fpl-catalog-backups/*",
        "arn:aws:s3:::fpl-catalog-backups"
      ]
    }
  ]
}
```

### 3. Enable Lifecycle Policy (auto-delete old backups)

```json
{
  "Rules": [
    {
      "Id": "DeleteOldBackups",
      "Status": "Enabled",
      "Expiration": {
        "Days": 30
      },
      "Filter": {
        "Prefix": "backup_"
      }
    }
  ]
}
```

## MySQL CLI Tools Installation

### Ubuntu/Debian

```bash
sudo apt-get update
sudo apt-get install mysql-client
```

### CentOS/RHEL

```bash
sudo yum install mysql
```

### macOS

```bash
brew install mysql-client
```

### Windows

Download from: https://dev.mysql.com/downloads/mysql/

## Testing Checklist

- [ ] Database connection works
- [ ] Email sending works (test with admin alert)
- [ ] Slack notification works
- [ ] Push notifications work (test with announcement)
- [ ] 2FA QR code generation works
- [ ] Media upload with watermark works
- [ ] Database backup creates file
- [ ] S3 upload works (if configured)
- [ ] GraphQL queries work at `/graphql`

## GraphQL Testing

Access GraphQL Playground at: `http://localhost:9999/graphiql`

Example queries:

```graphql
# Get all apps
query {
  apps(page: 1, size: 10) {
    content {
      id
      name
      shortDescription
    }
    totalElements
  }
}

# Get domains
query {
  domains {
    id
    name
    slug
  }
}

# Get specific app
query {
  app(id: "app-id-here") {
    id
    name
    domain {
      name
    }
  }
}
```

## Production Deployment

### 1. Build JAR

```bash
./gradlew clean build
```

### 2. Run with Production Profile

```bash
java -jar build/libs/BE-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --spring.config.location=file:./application-prod.properties
```

### 3. Or use Docker

```dockerfile
FROM openjdk:17-jdk-slim
COPY build/libs/BE-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

```bash
docker build -t fpl-catalog-backend .
docker run -p 9999:9999 --env-file .env.production fpl-catalog-backend
```

## Monitoring

### Health Check Endpoint

```
GET /actuator/health
```

### Check Scheduled Tasks

- Backups run daily at 2 AM
- Batch alerts run every hour

### Logs

```bash
tail -f logs/spring.log
```

## Security Hardening

- [ ] Change default passwords
- [ ] Enable HTTPS/TLS
- [ ] Configure firewall rules
- [ ] Enable rate limiting
- [ ] Set up monitoring/alerts
- [ ] Regular security updates
- [ ] Backup verification

## Troubleshooting

### Backup Fails

- Check MySQL CLI is installed: `mysqldump --version`
- Check database credentials
- Check disk space

### Push Notifications Not Working

- Verify VAPID keys are correct
- Check browser permissions
- Test in different browser

### GraphQL Errors

- Check Spring Boot logs
- Verify GraphQL schema is valid
- Test with GraphiQL interface

## Support

For issues, check logs in:

- `logs/spring.log`
- `logs/error.log`
- Database backup logs in `backups/`
