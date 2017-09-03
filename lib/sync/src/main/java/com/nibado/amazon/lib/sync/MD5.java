package com.nibado.amazon.lib.sync;

import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    private static MessageDigest getMd5() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5(final File file) throws IOException {
        return md5(new FileInputStream(file));
    }

    public static String md5(final InputStream ins) throws IOException {
        MessageDigest md = getMd5();
        byte[] buffer = new byte[1024];
        int read;

        while((read = ins.read(buffer)) > 0) {
            md.update(buffer, 0, read);
        }

        ins.close();

        return new String(Hex.encodeHex(md.digest()));
    }
}
