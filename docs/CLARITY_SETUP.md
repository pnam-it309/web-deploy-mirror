# Microsoft Clarity Integration Guide

Microsoft Clarity is a free user behavior analytics tool that provides heatmaps, session recordings, and insights.

## Setup Instructions

### 1. Create Clarity Account

- Visit https://clarity.microsoft.com/
- Sign in with Microsoft account
- Create new project for "FPL UDPM Catalog"

### 2. Get Project ID

- After creating project, copy the Project ID (looks like: `abc123def`)

### 3. Add Clarity Script to Frontend

Add the following script to `FE/index.html` before the closing `</head>` tag:

```html
<script type="text/javascript">
  (function (c, l, a, r, i, t, y) {
    c[a] =
      c[a] ||
      function () {
        (c[a].q = c[a].q || []).push(arguments);
      };
    t = l.createElement(r);
    t.async = 1;
    t.src = "https://www.clarity.ms/tag/" + i;
    y = l.getElementsByTagName(r)[0];
    y.parentNode.insertBefore(t, y);
  })(window, document, "clarity", "script", "YOUR_PROJECT_ID");
</script>
```

Replace `YOUR_PROJECT_ID` with your actual Clarity Project ID.

## Features Available

✅ **Heatmaps** - See where users click, scroll, and move their mouse
✅ **Session Recordings** - Watch real user sessions
✅ **Insights** - Discover user behavior patterns
✅ **Dead Clicks** - Find frustrating UI elements
✅ **Rage Clicks** - Identify problematic areas

## Privacy Note

Clarity automatically masks sensitive information (passwords, credit cards, etc.) and is GDPR compliant.

## Alternative: Hotjar

For more advanced features (surveys, feedback widgets):

- Visit https://www.hotjar.com/
- Similar integration process
- Free tier available for up to 35 daily sessions
