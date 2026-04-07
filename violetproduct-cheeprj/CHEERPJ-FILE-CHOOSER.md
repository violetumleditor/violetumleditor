# CheerpJ File Chooser Implementation

## Overview
This implementation enables native browser file dialogs for opening and saving Violet UML diagrams when running via CheerpJ in a web browser.

## What Changed

### 1. **index.html** - JavaScript Bridge
Added three new native methods to handle file operations:

- **`nativeShowOpenDialog(acceptedExtensions)`**
  - Displays browser file picker dialog
  - Returns `true` if file selected, `false` if cancelled
  - Parameter: comma-separated file extensions (e.g., ".violet.html,.vml")
  - Stores selected file data in `window.cheerpjFileState`

- **`nativeGetFileData()`**
  - Returns the byte array of the selected file
  - Called after `nativeShowOpenDialog` returns true

- **`nativeGetFileName()`**
  - Returns the filename string of the selected file
  - Useful for preserving original filename on save

- **`nativeShowSaveDialog(defaultFilename, content, mimeType)`**
  - Downloads file immediately with specified filename
  - The browser may show save-as dialog depending on user settings

### 2. **CheerpJInterfaceService.java**
Added wrapper methods that bridge Java to JavaScript:
- `showOpenDialog(String acceptedExtensions)` - returns boolean
- `getFileData()` - returns byte[]
- `getFileName()` - returns String
- Proper error handling with IOException

### 3. **CheerpJFileChooserService.java** - Main Implementation
Updated the file chooser service to:
- **`chooseAndGetFileReader()`** - Now opens browser file dialog and returns reader
- **`buildAcceptedExtensions()`** - Converts ExtensionFilter array to browser-compatible format
- Maintains backward compatibility with existing file operations

### 4. **CheerpJFileReader.java** (New)
New class implementing `IFileReader`:
- Reads in-memory file data from browser upload
- Mimics the IFile interface for consistency
- Provides InputStream for diagram deserialization

## How It Works

### Opening a File
```
User clicks "File > Open"
  ↓
CheerpJFileChooserService.chooseAndGetFileReader()
  ↓
Shows browser file picker dialog (HTML5)
  ↓
User selects file
  ↓
FileReader API reads file as ArrayBuffer
  ↓
Data stored in window.cheerpjFileState
  ↓
Java retrieves file data via nativeGetFileData()
  ↓
CheerpJFileReader created with file content
  ↓
Violet opens/deserializes diagram from byte array
```

### Saving a File
```
User clicks "File > Save"
  ↓
CheerpJFileChooserService.chooseAndGetFileWriter()
  ↓
Returns CheerpJDownloadFileWriter
  ↓
Violet serializes diagram to XML
  ↓
CheerpJDownloadFileWriter.close() is called
  ↓
Calls CheerpJInterfaceService.downloadFile()
  ↓
JavaScript creates blob and triggers download
  ↓
Browser shows "Save As" dialog (depending on browser settings)
  ↓
File saved to user's default downloads folder
```

## File Dialog Behavior

### Browser Compatibility
- Works with all modern browsers (Chrome, Firefox, Safari, Edge)
- Uses standard HTML5 `<input type="file">` element
- Uses File API and FileReader

### Platform Behavior
- **Accept filter**: Browser file picker shows filtered extensions
- **Download folder**: Uses browser's default download location
- **Overwrite**: Browser handles overwrite protection

## Deployment

### Building
```bash
mvn clean package
```

### Deploying
1. Copy the built JAR containing CheerpJ runtime
2. Host `index.html` on your static server
3. Update JAR path in index.html if needed:
   ```javascript
   await cheerpjRunJar("/app/violetumleditor/violetumleditor-latest.jar", ...)
   ```

### Test URL
Visit: `http://your-server/path/to/index.html`

## Notes

- **No Save-As dialog on open**: Browser security prevents writing to arbitrary files on first open. Use Save instead.
- **Single file at a time**: Can only handle one file upload at a time (stored in `window.cheerpjFileState`)
- **File size limits**: Depends on browser RAM available (typically 500MB+)
- **CORS**: If hosting JAR on different server, ensure CORS headers are set
- **HTTPS recommended**: File dialogs work best on HTTPS (required for some features in production)

## Example Usage (For Developers)

The file chooser is automatically integrated. No special code needed:

```java
// Opening files
IFileReader reader = fileChooserService.chooseAndGetFileReader(
    new ExtensionFilter("Violet Diagrams", ".violet.html")
);

// Saving files  
IFileWriter writer = fileChooserService.chooseAndGetFileWriter(
    new ExtensionFilter("Violet Diagrams", ".violet.html")
);
```

## Future Enhancements

- Add drag-and-drop support for file upload
- Implement recent files list
- Add file preview in upload dialog
- Support for multiple file uploads
- Implement proper save-to-cloud integration
