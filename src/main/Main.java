package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import apt.annotations.Future;
import apt.annotations.InitParaTask;
import apt.annotations.TaskScheduingPolicy;
import main.images.ImageGrid;
import main.images.RGBLibrary;
import main.images.downloader.ImageDownloader;
import main.images.reader.ImageLibrary;
import pt.runtime.ParaTask;

public class Main {
	
	public static void main(String[] args) {
		ParaTask.init(4);
		@Future
		int task1 = taskOne();
		
		@Future
		int task2 = taskTwo();
	
		@Future(depends = "task1, task2")
		int task3 = taskThree(task1, task2);

		
	}
	
	public static int taskOne() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getId());
		return 1;
	}
	
	public static int taskTwo() {
		System.out.println(Thread.currentThread().getId());
		return 2;
	}
	
	public static int taskThree(int task1, int task2) {
		System.out.println(Thread.currentThread().getId());
		return task1 + task2;
	}
}
