/*
 * Copyright © 2019 Agency for Data Supply and Efficiency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.kortforsyningen.proj;

import java.net.URL;
import java.net.URISyntaxException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


/**
 * Base class of all objects having a reference to a native resource.
 * The resource is referenced in a pointer of type {@code long} and named {@code "ptr"}.
 * The field name matter, since native code searches for a field having exactly that name.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @version 1.0
 * @since   1.0
 */
abstract class NativeResource {
    /**
     * Name of the logger to use for all warnings or debug messages emitted by this package.
     */
    static final String LOGGER_NAME = "org.kortforsyningen.proj";

    /**
     * The message to provide in exception when a feature is not supported in PROJ,
     * or when we provide no mapping to it.
     */
    static final String UNSUPPORTED = "Not supported.";

    /**
     * For subclass constructors.
     */
    NativeResource() {
    }

    /**
     * Returns the version number of the PROJ library.
     *
     * @return the PROJ release string.
     *
     * @see Proj#version()
     */
    static native String version();

    /**
     * Returns an absolute path to the Java Native Interface C/C++ code.
     * If the resources can not be accessed by an absolute path,
     * then this method copies the resource in a temporary file.
     *
     * @return absolute path to the library (may be a temporary file).
     * @throws URISyntaxException if an error occurred while creating a URI to the native file.
     * @throws IOException if an error occurred while copying the library to a temporary file.
     * @throws SecurityException if the security manager denies loading resource, creating absolute path, <i>etc</i>.
     * @throws UnsatisfiedLinkError if no native resource has been found for the current OS.
     *
     * @see System#load(String)
     */
    private static Path libraryPath() throws URISyntaxException, IOException {
        final String os = System.getProperty("os.name");
        final String libdir, suffix;
        if (os.contains("Windows")) {
            libdir = "windows";
            suffix = "dll";
        } else if (os.contains("Mac OS")) {
            libdir = "darwin";
            suffix = "so";
        } else if (os.contains("Linux")) {
            libdir = "linux";
            suffix = "so";
        } else {
            throw new UnsatisfiedLinkError("Unsupported operating system: " + os);
        }
        /*
         * If the native file is inside the JAR file, we need to extract it to a temporary file.
         * That file will be deleted on JVM exists, so a new file will be copied every time the
         * application is executed.
         *
         * Example of URL for a JAR entry: jar:file:/home/…/proj.jar!/org/…/NativeResource.class
         */
        final String nativeFile = libdir + "/libproj-binding." + suffix;
        final URL res = NativeResource.class.getResource(nativeFile);
        if (res == null) {
            throw new UnsatisfiedLinkError("Missing native file: " + nativeFile);
        }
        final Path location;
        if ("jar".equals(res.getProtocol())) {
            location = Files.createTempFile("libproj-binding", suffix);
            location.toFile().deleteOnExit();
            try (InputStream in = res.openStream()) {
                Files.copy(in, location, StandardCopyOption.REPLACE_EXISTING);
            }
        } else {
            location = Paths.get(res.toURI());
        }
        return location;
    }

    /**
     * Loads the native library. If this initialization fails, a message is logged at fatal error level
     * (because the library will be unusable) but no exception is thrown.  We do not throw an exception
     * from this static initializer because doing so would result in {@link NoClassDefFoundError} to be
     * thrown on all subsequent attempts to use this class, which may be confusing.
     */
    static {
        try {
            System.load(libraryPath().toAbsolutePath().toString());
        } catch (UnsatisfiedLinkError | Exception e) {
            System.getLogger(LOGGER_NAME).log(System.Logger.Level.ERROR, e);
        }
    }

    /**
     * Invoked by native code for logging a message. This method should not be invoked from Java code
     * in order to allow the logging system to infer more accurately the source Java class and method.
     * If this method is renamed, then the native C++ code needs to be updated accordingly.
     *
     * @param  message  the message to log.
     */
    @SuppressWarnings("unused")
    private static void log(String message) {
        System.getLogger(LOGGER_NAME).log(System.Logger.Level.DEBUG, message);
    }
}
