/*
 * Copyright 2007 scala-tools.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package scala.maven;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.IOUtil;

public class StreamLogger extends Thread {

    private InputStream in_;
    private Log log_;
    private boolean isErr_;
    private PrintWriter out_;

    public StreamLogger(InputStream in, Log log, boolean isErr) {
        in_ = in;
        log_ = log;
        isErr_ = isErr;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in_));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (isErr_) {
                    log_.warn(line);
                } else {
                    log_.info(line);
                }
            }
        } catch(IOException exc) {
            throw new RuntimeException("wrap: " + exc.getMessage(), exc);
        } finally {
            IOUtil.close(reader);
            IOUtil.close(in_);
            IOUtil.close(out_);
        }
    }
}