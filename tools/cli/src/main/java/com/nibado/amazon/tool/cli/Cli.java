package com.nibado.amazon.tool.cli;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.nibado.amazon.lib.auth.PropertySecrets;
import com.nibado.amazon.lib.s3wrapper.S3;
import com.nibado.amazon.tool.cli.command.Result;
import com.nibado.amazon.tool.cli.writer.ResultWriter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Cli {
    public void run() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        CommandFactory factory = new CommandFactory(s3());

        String line;
        while((line = reader.readLine()) != null) {
            Result<?> result = factory.parse(line).run(new Result<>());

            ResultWriter.write(result);
        }
    }

    private static S3 s3() {
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
        builder.setRegion("eu-west-1");
        builder.setCredentials(new PropertySecrets());

        return new S3(builder.build());
    }

    public static void main(String... argv) throws Exception {
        new Cli().run();
    }
}
