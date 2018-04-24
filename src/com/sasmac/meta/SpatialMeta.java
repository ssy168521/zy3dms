package com.sasmac.meta;

import java.sql.Connection;
import org.apache.commons.dbutils.QueryRunner;

/**
 * 用于接收xml节点内或geotiff数据内容、NodePathParser.java
 * 
 * @author Administrator
 * @ClassName:SpatialMeta
 * @Version 版本
 * @ModifiedBy 修改人
 * @date 2018年3月20日 下午4:19:15
 */
public class SpatialMeta implements metadata {

	private String FileName; // 文件名
	private String FilePath; // 路径
	private String satellite;// 卫星
	private long FileSize; // 影像大小
	private double ExtentRight;// 影像右边界
	private double ExtentLeft;// 左边界
	private double ExtentTop;// 上边界
	private double ExtentBottom;// 下边界
	private String DataType;// 栅格类型
	private String wktString;// 范围
	private java.util.Date acquisitionTime;// 获取时间
	private java.util.Date archiveTime; // 归档时间点
	private long dataid; // 数据id，唯一标识
	private int orbitID; // 轨道ID号
	private int scenePath; // 场景轨道
	private int sceneRow; // 场景行
	private String sensor; // 传感器
	private long productid; // 产品id
	private int productLevel; // 产品级别

	public java.util.Date getAcquisitionTime() {
		return acquisitionTime;
	}

	public void setAcquisitionTime(java.util.Date acquisitionTime) {
		this.acquisitionTime = acquisitionTime;
	}

	public java.util.Date getArchiveTime() {
		return archiveTime;
	}

	public void setArchiveTime(java.util.Date archiveTime) {
		this.archiveTime = archiveTime;
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public String getFilePath() {
		return FilePath;
	}

	public void setFilePath(String filePath) {
		FilePath = filePath;
	}

	public long getFileSize() {
		return FileSize;
	}

	public void setFileSize(long fileSize) {
		FileSize = fileSize;
	}

	public double getExtentRight() {
		return ExtentRight;
	}

	public void setExtentRight(double extentRight) {
		ExtentRight = extentRight;
	}

	public double getExtentLeft() {
		return ExtentLeft;
	}

	public void setExtentLeft(double extentLeft) {
		ExtentLeft = extentLeft;
	}

	public double getExtentTop() {
		return ExtentTop;
	}

	public void setExtentTop(double extentTop) {
		ExtentTop = extentTop;
	}

	public double getExtentBottom() {
		return ExtentBottom;
	}

	public void setExtentBottom(double extentBottom) {
		ExtentBottom = extentBottom;
	}

	public String getWktString() {
		return wktString;
	}

	public void setWktString(String wktString) {
		this.wktString = wktString;
	}

	public String getSatellite() {
		return satellite;
	}

	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}

	public String getDataType() {
		return DataType;
	}

	public void setDataType(String dataType) {
		DataType = dataType;
	}

	public long getDataid() {
		return dataid;
	}

	public void setDataid(long dataid2) {
		this.dataid = dataid2;
	}

	public int getOrbitID() {
		return orbitID;
	}

	public void setOrbitID(int orbitID) {
		this.orbitID = orbitID;
	}

	public int getScenePath() {
		return scenePath;
	}

	public void setScenePath(int scenePath) {
		this.scenePath = scenePath;
	}

	public int getSceneRow() {
		return sceneRow;
	}

	public void setSceneRow(int sceneRow) {
		this.sceneRow = sceneRow;
	}

	public String getSensor() {
		return sensor;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
	}

	public long getProductid() {
		return productid;
	}

	public void setProductid(long dataid2) {
		this.productid = dataid2;
	}

	public int getProductLevel() {
		return productLevel;
	}

	public void setProductLevel(int productLevel) {
		this.productLevel = productLevel;
	}

	@Override
	public boolean insertmeta(Connection conn) throws Exception {

		// QueryRunner qr = new QueryRunner();

		return true;
	}
}