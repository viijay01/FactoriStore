// factori app generator — store name + URL + icon  ->  installable Android APK
// Usage:
//   node generate.js "Zolara" https://zolara.in icons/zolara.png
//   node generate.js "GKP Vastra" https://gkpvastra.com icons/gkpvastra.png
const { execSync } = require("child_process");
const fs = require("fs");
const path = require("path");

const [name, url, icon] = process.argv.slice(2);
if (!name || !url || !icon) {
  console.log('Usage: node generate.js "Store Name" https://store.url path/to/icon.png');
  process.exit(1);
}
if (!/^https:\/\//.test(url)) { console.error("URL must start with https://"); process.exit(1); }
if (path.extname(icon).toLowerCase() !== ".png") { console.error("Icon must be a .png file"); process.exit(1); }

// unique, permanent app id part: "GKP Vastra" -> "gkpvastra"
let slug = name.toLowerCase().replace(/[^a-z0-9]/g, "");
if (!slug || /^[0-9]/.test(slug)) slug = "s" + slug;

console.log(`\n▶ Building "${name}"  (com.factori.store.${slug})  ->  ${url}\n`);

fs.copyFileSync(icon, path.join("app", "src", "main", "res", "drawable", "app_icon.png"));

const gradlew = process.platform === "win32" ? "gradlew.bat" : "./gradlew";
execSync(
  `${gradlew} assembleDebug -PappName="${name}" -PstoreUrl=${url} -PappSlug=${slug}`,
  { stdio: "inherit" }
);

fs.mkdirSync("output", { recursive: true });
const out = path.join("output", `${slug}.apk`);
fs.copyFileSync(path.join("app", "build", "outputs", "apk", "debug", "app-debug.apk"), out);
console.log(`\n✅ App ready: ${out}\n`);
