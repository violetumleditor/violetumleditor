# CheerpJ interop for browser download

This module now exposes native methods that allow browser-side download when saving a graph:

- Java native: `CheerpJInterfaceService.nativeSetApplication(CheerpJInterfaceService appInstance)`
- Java native: `CheerpJInterfaceService.nativeMethodCallback()`
- Java native: `CheerpJInterfaceService.nativeDownloadFile(String filename, String mimeType, byte[] content)`
- JavaScript native function name (CheerpJ naming convention):
  `Java_com_horstmann_violet_application_jni_CheerpJInterfaceService_nativeSetApplication`
  `Java_com_horstmann_violet_application_jni_CheerpJInterfaceService_nativeMethodCallback`
  `Java_com_horstmann_violet_application_jni_CheerpJInterfaceService_nativeDownloadFile`

## JavaScript snippet

Add these functions to the page where you initialize CheerpJ (for example `index.html`).

```html
<script src="https://cjrtnc.leaningtech.com/4.2/loader.js"></script>
<script>
  async function Java_com_horstmann_violet_application_jni_CheerpJInterfaceService_nativeSetApplication(lib, app) {
    window.violetApp = app;
    // Keep callback thread alive as described in CheerpJ interoperability docs.
    return new Promise(() => {});
  }

  async function Java_com_horstmann_violet_application_jni_CheerpJInterfaceService_nativeMethodCallback(lib) {
    return 1;
  }

  async function Java_com_horstmann_violet_application_jni_CheerpJInterfaceService_nativeDownloadFile(lib, filename, mimeType, content) {
    const bytes = content instanceof Uint8Array ? content : new Uint8Array(content);
    const blob = new Blob([bytes], { type: mimeType || "application/octet-stream" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = filename || "diagram.violet.html";
    document.body.appendChild(link);
    link.click();
    link.remove();
    URL.revokeObjectURL(url);
  }

  (async () => {
    await cheerpjInit({
      version: 8,
      natives: {
        Java_com_horstmann_violet_application_jni_CheerpJInterfaceService_nativeSetApplication,
        Java_com_horstmann_violet_application_jni_CheerpJInterfaceService_nativeMethodCallback,
        Java_com_horstmann_violet_application_jni_CheerpJInterfaceService_nativeDownloadFile,
      },
    });

    // Adjust the jar path to your hosted artifact.
    await cheerpjRunJar("/app/violetumleditor.jar");
  })();
</script>
```

## Behavior

- In browser mode (JavaScript bridge available), Violet now uses a browser-backed file writer.
- `Save` and `Save As` for graph files produce a browser download instead of desktop file dialog I/O.
- Image/PDF export also uses the same browser download path.
