package util;

import java.io.File;

public class EncrypteTool {

	public static void main(String[] args) {
		try {
			File a=new File("C:\\Users\\k7043\\Downloads\\dbSynchronize\\data\\column_setting.bin");
			
			//解密
			CommonUtil.decrypt(a.getPath(), Constans.edit_pw);
			
			//加密
			//CommonUtil.encrypt(a.getPath(), Constans.edit_pw);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
