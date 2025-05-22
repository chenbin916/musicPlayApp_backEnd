package com.musicplayer.exception;

public class FileUploadException extends BusinessException {
    public FileUploadException(String message) {
        super("FILE_UPLOAD_ERROR", message);
    }

    public static FileUploadException fileTooLarge(long maxSize) {
        return new FileUploadException("File size exceeds the maximum limit of " + maxSize + " bytes");
    }

    public static FileUploadException invalidFileType(String allowedTypes) {
        return new FileUploadException("Invalid file type. Allowed types: " + allowedTypes);
    }

    public static FileUploadException uploadFailed(String reason) {
        return new FileUploadException("File upload failed: " + reason);
    }
} 