// src/utils/disableLogs.ts
export function disableLogsInProduction() {
  if (import.meta.env.MODE === "production") {
    console.log = () => {};
    console.warn = () => {};
    console.error = () => {};
  }
}
