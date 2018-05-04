package main.images.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import main.images.RGBGrid;

public class RGBGridTest {
	RGBGrid grid;
	
	@Before
	public void before(){
		BufferedImage image = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.GREEN);
		g2d.fillRect(0,0,50,100);
		g2d.setColor(Color.BLUE);
		g2d.fillRect(50,0,50,100);
		/*
		File file = new File("test.png");
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		grid = new RGBGrid(4,1,image);
		
	}
	
	@Test
	public void test1() {
		assertEquals(255,grid.getGridCell(0, 0).getG());
	}
	
	@Test
	public void test2() {
		assertEquals(0,grid.getGridCell(3, 0).getG());
	}
	@Test
	public void test3() {
		assertEquals(0,grid.getGridCell(0, 0).getB());
	}
	@Test
	public void test4() {
		assertEquals(255,grid.getGridCell(3, 0).getB());
	}
}
