package me.xihuxiaolongren.photoga.mode;

import java.util.List;

public class ImageFolder
{
	/**
	 * 图片的文件夹路径
	 */
	private String dir;

	/**
	 * 第一张图片的路径
	 */
	private String firstImagePath;

	/**
	 * 文件夹的名称
	 */
	private String name;

	private List<String> imageList;

	public List<String> getImageList() {
		return imageList;
	}

	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}

	public String getDir()
	{
		return dir;
	}

	public void setDir(String dir)
	{
		this.dir = dir;
		int lastIndexOf = dir.lastIndexOf("/");
		this.name = dir.substring(lastIndexOf);
	}

	public String getFirstImagePath()
	{
		return firstImagePath;
	}

	public void setFirstImagePath(String firstImagePath)
	{
		this.firstImagePath = firstImagePath;
	}

	public String getName()
	{
		return name;
	}
	public int getCount()
	{
		return imageList.size();
	}

}
