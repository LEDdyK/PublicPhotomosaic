package main.images;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageTest {
	public static void main(String args[]) {
		BufferedImage image = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.RED);
		g2d.fillRect(0, 0, 100, 100);

		System.out.println(image.getRGB(1, 1));
		System.out.println((image.getRGB(1, 1)) & 0xff);
		System.out.println((image.getRGB(1, 1)>>8) & 0xff);
		System.out.println((image.getRGB(1, 1)>>16) & 0xff);
		System.out.println((image.getRGB(1, 1)>>24) & 0xff);
		System.out.println(3/2);
		
	}
}
