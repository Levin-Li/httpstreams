package com.thunisoft.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;


public class Yzm {
    public static void main(String[] args) throws IOException {
        DefaultKaptcha k = new DefaultKaptcha();
        
        Config config = new Config(new Properties());
        k.setConfig(config);
        
        BufferedImage image = k.createImage("abcdef");
        
        File out = new File("tset.jpeg");
        ImageIO.write(image, "jpeg", out);
        
        System.out.println(out.getAbsolutePath());
        
    }
}
