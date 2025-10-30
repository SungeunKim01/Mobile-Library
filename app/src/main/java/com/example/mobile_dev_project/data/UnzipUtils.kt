package com.example.mobile_dev_project.data

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipFile

/**
 * this extract all entries of .zip into the destination folde
 * I refer GitHub Gist unzipping utility by Nitin Prakash for reliable ZIP file extraction and code in the comment:
 *https://gist.github.com/NitinPraksash9911/dea21ec4b8ae7df068f8f891187b6d1e#file-unziputils-kt
 * zipFile lets enumerate entries directly & open their streams on demand, so in this milestone 2,
 * reliable for expand whole achive
 */
object UnzipUtils {

     //Unzip the given zip file into destDirectory
     // this also creates destDirectory if missing and
     // keep og subfolder structure from the zip
    @Throws(IOException::class)
    fun unzip(zipFilePath: File, destDirectory: File) {
        if (!destDirectory.exists()) destDirectory.mkdirs()

        // open the zip as random access archive
        ZipFile(zipFilePath).use { zip ->
            // here iterate every entry - files & directories
            val entries = zip.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val outPath = File(destDirectory, entry.name)

                if (entry.isDirectory) {
                    // make sure directory exist
                    outPath.mkdirs()
                } else {
                    //make sure parent folder exist
                    outPath.parentFile?.mkdirs()

                    //Stream file bytes to disk
                    zip.getInputStream(entry).use { input ->
                        // will write writeStreamToFile function under
                        writeStreamToFile(input, outPath)
                    }
                }
            }
        }
    }
}
