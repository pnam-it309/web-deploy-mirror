# 🔧 Automation Scripts

This folder contains automation scripts for project maintenance and synchronization.

## 📜 Available Scripts

### Windows Batch Scripts (.bat)

#### [push-to-mirror.bat](push-to-mirror.bat)

**Purpose:** Push changes from local repository to mirror repository

**Usage:**

```bash
.\scripts\push-to-mirror.bat
```

**What it does:**

- Commits current changes
- Pushes to the mirror repository
- Shows sync status

---

#### [sync-to-mirror.bat](sync-to-mirror.bat)

**Purpose:** Synchronize local repository with mirror repository (Windows Command Prompt)

**Usage:**

```bash
.\scripts\sync-to-mirror.bat
```

**What it does:**

- Pulls latest changes from origin
- Pushes to mirror repository
- Handles conflict resolution
- Logs sync activity

---

### PowerShell Scripts (.ps1)

#### [sync-to-mirror.ps1](sync-to-mirror.ps1)

**Purpose:** Synchronize local repository with mirror repository (PowerShell - recommended)

**Usage:**

```powershell
.\scripts\sync-to-mirror.ps1
```

**Features:**

- More robust error handling than .bat version
- Colored output for better visibility
- Detailed logging
- Automatic conflict detection
- Progress indicators

**Advantages over .bat:**

- Better error messages
- More reliable on Windows 10/11
- Support for advanced Git operations
- Easier to maintain and extend

---

## 🚀 How to Run Scripts

### From Project Root

**Command Prompt:**

```bash
cd D:\FPL-UDPM-Catalog
.\scripts\sync-to-mirror.bat
```

**PowerShell:**

```powershell
cd D:\FPL-UDPM-Catalog
.\scripts\sync-to-mirror.ps1
```

### Directly from Scripts Folder

**Command Prompt:**

```bash
cd scripts
sync-to-mirror.bat
```

**PowerShell:**

```powershell
cd scripts
.\sync-to-mirror.ps1
```

---

## ⚠️ Prerequisites

Before running these scripts, ensure:

1. **Git is installed** and available in PATH
2. **Repository is cloned** and initialized
3. **Mirror repository is configured** as a remote
4. **Git credentials** are set up (SSH key or credential manager)

Check Git configuration:

```bash
git remote -v
```

You should see both `origin` and `mirror` remotes.

---

## 📝 Adding New Scripts

When creating new automation scripts:

1. **Place them in this folder** (`/scripts`)
2. **Use descriptive names** (e.g., `backup-database.bat`, `deploy-production.ps1`)
3. **Add documentation** to this README
4. **Include error handling** in your scripts
5. **Test thoroughly** before committing

### Script Template (PowerShell)

```powershell
# Script Name: example-script.ps1
# Purpose: Brief description
# Author: Your Name
# Date: YYYY-MM-DD

# Error handling
$ErrorActionPreference = "Stop"

try {
    Write-Host "Starting script..." -ForegroundColor Green

    # Your script logic here

    Write-Host "Script completed successfully!" -ForegroundColor Green
}
catch {
    Write-Host "Error: $_" -ForegroundColor Red
    exit 1
}
```

---

## 🔍 Troubleshooting

### Script doesn't run

- Check execution policy (PowerShell): `Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser`
- Verify file permissions
- Run as Administrator if needed

### Git errors

- Ensure remote is configured: `git remote -v`
- Check credentials: `git config --list`
- Verify network connectivity

### Path issues

- Use absolute paths when in doubt
- Check working directory: `pwd` (PowerShell) or `cd` (CMD)

---

## 📚 Related Documentation

- [Mirror Sync Guide](../docs/MIRROR-SYNC-GUIDE.md) - Detailed synchronization guide
- [ARCHITECTURE.md](../ARCHITECTURE.md) - Project structure
- [README.md](../README.md) - Main documentation

---

**Last Updated:** 2026-01-13  
**Maintained By:** Development Team
