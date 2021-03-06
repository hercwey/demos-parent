package com.io.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by lenovo on 2017/12/2.
 */
public class FileChannelDemo {


    public void memoryMaped() {
        File file = new File( "."+File.separator+"README.md");
        try {
            FileInputStream fileInputStream =new FileInputStream(file);
            FileChannel fileChannel = fileInputStream.getChannel();
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE,0,file.length());
            byte[] data  = new byte[(int)file.length()];
            int foot = 0;
            while (mappedByteBuffer.hasRemaining()) {
                data[foot++]=mappedByteBuffer.get();
            }

            System.out.println(new String(data));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileChannelDemo fileChannelDemo = new FileChannelDemo();
        fileChannelDemo.memoryMaped();
    }
}
