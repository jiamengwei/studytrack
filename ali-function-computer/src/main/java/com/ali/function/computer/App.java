package com.ali.function.computer;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.FunctionInitializer;
import com.aliyun.fc.runtime.StreamRequestHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class App implements StreamRequestHandler, FunctionInitializer {

    public void initialize(Context context) throws IOException {

    }

    public void handleRequest(
        InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        context.getExecutionCredentials();
        context.getRequestId();
        context.getLogger().info("hello world");
        context.getLogger().info(context.getFunctionParam().toString());
        context.getLogger().info(context.getFunctionParam().getFunctionName());
        outputStream.write(new String("hello world\n").getBytes());
    }
}