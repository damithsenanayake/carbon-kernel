/*
 *  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.kernel.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * File Utilities.
 *
 * @since 5.0.0
 */
public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Deletes all files and subdirectories under dir.
     * Returns true if all deletions were successful.
     * If a deletion fails, the method stops attempting to delete and returns false.
     *
     * @param dir The directory to be deleted
     * @return true if the directory and its descendents were deleted
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }


    /**
     * Copies src file to dst file.
     * If the dst file does not exist, it is created
     *
     * @param src The source file
     * @param dst The destiination file
     * @throws java.io.IOException If an Exception occurs while copying
     */
    public static void copyFile(File src, File dst) throws IOException {
        if (!src.exists()) {
            throw new IOException("Source file does not exist: " + src);
        }
        
        String dstAbsPath = dst.getAbsolutePath();
        String dstDir = dstAbsPath.substring(0, dstAbsPath.lastIndexOf(File.separator));
        File dir = new File(dstDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Fail to create the directory: " + dir.getAbsolutePath());
        }

        try (InputStream in = new FileInputStream(src); OutputStream out = new FileOutputStream(dst)) {

            // Transfer bytes from in to out
            byte[] buf = new byte[10240];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            logger.warn("Unable to copy file " + e.getMessage(), e);
            throw new IOException("Unable to copy file ", e);
        }
    }

    /**
     * Copies src file to dst directory.
     * If the dst directory does not exist, it is created
     *
     * @param src The file to be copied
     * @param dst The destination directory to which the file has to be copied
     * @throws java.io.IOException If an error occurs while copying
     */
    public static void copyFileToDir(File src, File dst) throws IOException {
        String dstAbsPath = dst.getAbsolutePath();
        String dstDir = dstAbsPath.substring(0, dstAbsPath.lastIndexOf(File.separator));
        File dir = new File(dstDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Fail to create the directory: " + dir.getAbsolutePath());
        }

        File file = new File(dstAbsPath + File.separator + src.getName());
        copyFile(src, file);
    }

    /**
     * Archive a directory.
     *
     * @param destArchive destination of the archive
     * @param sourceDir   source directory
     * @throws java.io.IOException throws io exception if archive failed
     */
    public static void archiveDir(String destArchive, String sourceDir) throws IOException {
        File zipDir = new File(sourceDir);
        if (!zipDir.isDirectory()) {
            throw new RuntimeException(sourceDir + " is not a directory");
        }

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destArchive))) {
            zipDir(zipDir, zos, sourceDir);
        }
    }

    protected static void zipDir(File zipDir, ZipOutputStream zos, String archiveSourceDir)
            throws IOException {
        //get a listing of the directory content
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[40960];
        int bytesIn;
        //loop through dirList, and zip the files
        if (dirList != null) {
            for (String aDirList : dirList) {
                File f = new File(zipDir, aDirList);
                //place the zip entry in the ZipOutputStream object
                zos.putNextEntry(new ZipEntry(getZipEntryPath(f, archiveSourceDir)));
                if (f.isDirectory()) {
                    //if the File object is a directory, call this
                    //function again to add its content recursively
                    zipDir(f, zos, archiveSourceDir);
                    //loop again
                    continue;
                }
                //if we reached here, the File object f was not a directory
                //create a FileInputStream on top of f
                try (FileInputStream fis = new FileInputStream(f)) {
                    //now write the content of the file to the ZipOutputStream
                    while ((bytesIn = fis.read(readBuffer)) != -1) {
                        zos.write(readBuffer, 0, bytesIn);
                    }
                }
            }
        }
    }

    protected static String getZipEntryPath(File f, String archiveSourceDir) {
        String entryPath = f.getPath();
        entryPath = entryPath.substring(archiveSourceDir.length() + 1);
        if (File.separatorChar == '\\') {
            entryPath = entryPath.replace(File.separatorChar, '/');
        }
        if (f.isDirectory()) {
            entryPath += "/";
        }
        return entryPath;
    }


}
