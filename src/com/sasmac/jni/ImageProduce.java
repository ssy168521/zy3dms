package com.sasmac.jni;

public class ImageProduce {
	static {
		System.loadLibrary("ImageProduce");
	}

	// 传入含jpw文件的jpg，经过重采样，
	public native boolean ImageRectify(String strInFileName, String strOutFilePath, int outSizeX, int outSizeY);
	public native boolean ImageRectify2GeoCS(String strInFileName, String strOutFilePath, int outSizeX, int outSizeY,int resamplemethod);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ImageProduce imgprodu = new ImageProduce();
		boolean resu = imgprodu.ImageRectify2GeoCS("D:\\framedata\\J46D001001.tif", "D:\\framedata\\J46D001001.png",
				256, 256,1);
		System.out.println(resu);

	}

}
