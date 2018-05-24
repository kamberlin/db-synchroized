package util;

import java.io.File;

public class EncrypteTool {

	public static void main(String[] args) {
		try {
			File a=new File("C:\\Users\\k7043\\Downloads\\dbSynchronize\\data\\column_setting.txt");
			//CommonUtil.encrypt(a.getPath(), DBSynConstans.edit_pw);
			
			CommonUtil.decrypt(a.getPath(), Constans.edit_pw);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
