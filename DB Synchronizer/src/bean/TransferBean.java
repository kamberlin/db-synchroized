package bean;

public class TransferBean {
	String type;
	String srcColumn;
	String destColumn;
	String srcContent;
	String destContent;
	String destTimeYMDFormat;
	String destTimeHMSFormat;
	
	
	public TransferBean(String type, String srcColumn, String destColumn, String srcContent, String destContent) {
		super();
		this.type = type;
		this.srcColumn = srcColumn;
		this.destColumn = destColumn;
		this.srcContent = srcContent;
		this.destContent = destContent;
	}
	public TransferBean(String type, String srcColumn, String destColumn, String srcContent, String destContent,
			String destTimeYMDFormat, String destTimeHMSFormat) {
		super();
		this.type = type;
		this.srcColumn = srcColumn;
		this.destColumn = destColumn;
		this.srcContent = srcContent;
		this.destContent = destContent;
		this.destTimeYMDFormat = destTimeYMDFormat;
		this.destTimeHMSFormat = destTimeHMSFormat;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSrcColumn() {
		return srcColumn;
	}
	public void setSrcColumn(String srcColumn) {
		this.srcColumn = srcColumn;
	}
	public String getDestColumn() {
		return destColumn;
	}
	public void setDestColumn(String destColumn) {
		this.destColumn = destColumn;
	}
	public String getSrcContent() {
		return srcContent;
	}
	public void setSrcContent(String srcContent) {
		this.srcContent = srcContent;
	}
	public String getDestContent() {
		return destContent;
	}
	public void setDestContent(String destContent) {
		this.destContent = destContent;
	}
	public String getDestTimeYMDFormat() {
		return destTimeYMDFormat;
	}
	public void setDestTimeYMDFormat(String destTimeYMDFormat) {
		this.destTimeYMDFormat = destTimeYMDFormat;
	}
	public String getDestTimeHMSFormat() {
		return destTimeHMSFormat;
	}
	public void setDestTimeHMSFormat(String destTimeHMSFormat) {
		this.destTimeHMSFormat = destTimeHMSFormat;
	}
	
}
