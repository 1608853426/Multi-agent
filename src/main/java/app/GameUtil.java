/*
 * 
 * create by_xiaoqing on 2018-04-05
 * 
 */
package app;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * 
 *用于获取图片的工具类
 *
 */
public final class GameUtil {

    private GameUtil() {
     
    } 
 
    public static Image getImage(String path) {
        BufferedImage bi = null;
        try {
            URL u = GameUtil.class.getClassLoader().getResource(path);
            bi = ImageIO.read(u);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi;
    }
}
