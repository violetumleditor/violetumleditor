package com.horstmann.violet.framework.file.persistence;

/**
 * Represents a delete operation for a specific file.
 * Implementations are environment-specific (standard filesystem, CheerpJ virtual FS, etc.).
 * Analogous to {@link IFileWriter} for write operations.
 */
public interface IFileDeleter
{
    /**
     * Deletes the file this deleter is bound to.
     * Implementations should perform best-effort deletion without throwing.
     */
    void delete();
}
