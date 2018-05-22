package util;

import java.io.File;

public class EncrypteTool {

	public static void main(String[] args) {
		try {
			File a=new File("D:\\\\DB_Syn\\\\etc\\\\db.properties.bk");
			CommonUtil.encrypt(a.getPath(), Constans.edit_pw);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
