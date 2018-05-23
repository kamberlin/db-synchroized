package util;

import java.awt.Font;
import java.util.ArrayList;

import bean.DestDBInfo;
import bean.SrcDBInfo;
import bean.TransferBean;
import bean.TransferBean;

public class DBSynConstans {
	public static String edit_pw="13084002";
	public static SrcDBInfo srcDBInfo=null;
	public static DestDBInfo destDBInfo=null;
	public static String mainproperty="../etc/dbsyn.properties";
	public static String dbproperty=null;
	public static String logproperty=null;
	public static String sequence_s="30";
	public static String timeUp="KamberUp=";
	public static String timeDown="KamberDown=";
	public static String condition=null;
	public static String columnproperty=null;
	public static String columnSettingPath=null;
	public static String timeUpPath=null;
	public static String timeDownPath=null;
	public static String condition_column=null;
	public static String defaultJComboBoxText="----請選擇----";
	public static String getOracleColumns="SELECT COLUMN_NAME FROM SYS.ALL_TAB_COLUMNS WHERE OWNER =? AND TABLE_NAME=? order by COLUMN_ID";
	public static String getSQLServerColumns="SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME=?";
	public static ArrayList<String> srcColumns=null;
	public static ArrayList<String> destColumns=null;
	public static ArrayList<TransferBean> columnList=null;
	public static ArrayList<TransferBean> timeUpList=null;
	public static ArrayList<TransferBean> timeDownList=null;
	public static Font titleFont = new Font("微軟正黑體", Font.PLAIN, 24);
	public static Font textFont = new Font("微軟正黑體", Font.PLAIN, 18);
}
