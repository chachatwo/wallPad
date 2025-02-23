//package com.wallpad.project;
//
//public enum MajorCategory {
//	REPAIR("보수", MiddleCategory.FLOORING.getName(), MiddleCategory.WALLPAPER.getName(), MiddleCategory.TILES.getName(), MiddleCategory.FURNITURE.getName(),
//					MiddleCategory.FLOORING.getCode(), MiddleCategory.WALLPAPER.getCode(), MiddleCategory.TILES.getCode(), MiddleCategory.FURNITURE.getCode(),
//					MiddleCategory.APPLIANCES.getName(), MiddleCategory.SYSTEM.getName(), MiddleCategory.ELECTRICAL.getName(), MiddleCategory.OTHERS.getName(),
//					MiddleCategory.APPLIANCES.getCode(), MiddleCategory.SYSTEM.getCode(), MiddleCategory.ELECTRICAL.getCode(), MiddleCategory.OTHERS.getCode(),
//					LastCategory.DAMAGE.getName(), LastCategory.CONTAMINATION.getName(), LastCategory.DEFECT.getName(), LastCategory.MALFUNCTION.getName(),
//					LastCategory.DAMAGE.getCode(), LastCategory.CONTAMINATION.getCode(), LastCategory.DEFECT.getCode(), LastCategory.MALFUNCTION.getCode()),
//	
//	REPLACEMENT("교체", MiddleCategory.FLOORING.getName(), MiddleCategory.WALLPAPER.getName(), MiddleCategory.TILES.getName(), MiddleCategory.FURNITURE.getName(),
//					MiddleCategory.FLOORING.getCode(), MiddleCategory.WALLPAPER.getCode(), MiddleCategory.TILES.getCode(), MiddleCategory.FURNITURE.getCode(),
//					MiddleCategory.APPLIANCES.getName(), MiddleCategory.SYSTEM.getName(), MiddleCategory.ELECTRICAL.getName(), MiddleCategory.OTHERS.getName(),
//					MiddleCategory.APPLIANCES.getCode(), MiddleCategory.SYSTEM.getCode(), MiddleCategory.ELECTRICAL.getCode(), MiddleCategory.OTHERS.getCode(),
//					LastCategory.DAMAGE.getName(), LastCategory.CONTAMINATION.getName(), LastCategory.DEFECT.getName(), LastCategory.MALFUNCTION.getName(),
//					LastCategory.DAMAGE.getCode(), LastCategory.CONTAMINATION.getCode(), LastCategory.DEFECT.getCode(), LastCategory.MALFUNCTION.getCode()),
//	
//	INSTALLATION("설치", MiddleCategory.FLOORING.getName(), MiddleCategory.WALLPAPER.getName(), MiddleCategory.TILES.getName(), MiddleCategory.FURNITURE.getName(),
//					MiddleCategory.FLOORING.getCode(), MiddleCategory.WALLPAPER.getCode(), MiddleCategory.TILES.getCode(), MiddleCategory.FURNITURE.getCode(),
//					MiddleCategory.APPLIANCES.getName(), MiddleCategory.SYSTEM.getName(), MiddleCategory.ELECTRICAL.getName(), MiddleCategory.OTHERS.getName(),
//					MiddleCategory.APPLIANCES.getCode(), MiddleCategory.SYSTEM.getCode(), MiddleCategory.ELECTRICAL.getCode(), MiddleCategory.OTHERS.getCode(),
//					LastCategory.DAMAGE.getName(), LastCategory.CONTAMINATION.getName(), LastCategory.DEFECT.getName(), LastCategory.MALFUNCTION.getName(),
//					LastCategory.DAMAGE.getCode(), LastCategory.CONTAMINATION.getCode(), LastCategory.DEFECT.getCode(), LastCategory.MALFUNCTION.getCode()),
//							
//	INSPECTION("점검", MiddleCategory.FLOORING.getName(), MiddleCategory.WALLPAPER.getName(), MiddleCategory.TILES.getName(), MiddleCategory.FURNITURE.getName(),
//					MiddleCategory.FLOORING.getCode(), MiddleCategory.WALLPAPER.getCode(), MiddleCategory.TILES.getCode(), MiddleCategory.FURNITURE.getCode(),
//					MiddleCategory.APPLIANCES.getName(), MiddleCategory.SYSTEM.getName(), MiddleCategory.ELECTRICAL.getName(), MiddleCategory.OTHERS.getName(),
//					MiddleCategory.APPLIANCES.getCode(), MiddleCategory.SYSTEM.getCode(), MiddleCategory.ELECTRICAL.getCode(), MiddleCategory.OTHERS.getCode(),
//					LastCategory.DAMAGE.getName(), LastCategory.CONTAMINATION.getName(), LastCategory.DEFECT.getName(), LastCategory.MALFUNCTION.getName(),
//					LastCategory.DAMAGE.getCode(), LastCategory.CONTAMINATION.getCode(), LastCategory.DEFECT.getCode(), LastCategory.MALFUNCTION.getCode());
//	
//	private String name;
//	private String middleCategory1;
//	private String middleCategory2;
//	private String middleCategory3;
//	private String middleCategory4;
//	private String middleCategory5;
//	private String middleCategory6;
//	private String middleCategory7;
//	private String middleCategory8;
//	private String middleCategory9;
//	private String middleCategory10;
//	private String middleCategory11;
//	private String middleCategory12;
//	private String middleCategory13;
//	private String middleCategory14;
//	private String middleCategory15;
//	private String middleCategory16;
//	private String lastCategory1;
//	private String lastCategory2;
//	private String lastCategory3;
//	private String lastCategory4;
//	private String lastCategory5;
//	private String lastCategory6;
//	private String lastCategory7;
//	private String lastCategory8;
//	
//	MajorCategory(String name, String middleCategory1, String middleCategory2, String middleCategory3, String middleCategory4, String middleCategory5, 
//			String middleCategory6, String middleCategory7, String middleCategory8, String middleCategory9, String middleCategory10, 
//			String middleCategory11, String middleCategory12, String middleCategory13, String middleCategory14, String middleCategory15, String middleCategory16, 
//			String lastCategory1, String lastCategory2, String lastCategory3, String lastCategory4, String lastCategory5, String lastCategory6, 
//			String lastCategory7, String lastCategory8) {
//		this.name = name;
//		this.middleCategory1 = middleCategory1;
//		this.middleCategory2 = middleCategory2;
//		this.middleCategory3 = middleCategory3;
//		this.middleCategory4 = middleCategory4;
//		this.middleCategory5 = middleCategory5;
//		this.middleCategory6 = middleCategory6;
//		this.middleCategory7 = middleCategory7;
//		this.middleCategory8 = middleCategory8;
//		this.middleCategory9 = middleCategory9;
//		this.middleCategory10 = middleCategory10;
//		this.middleCategory11 = middleCategory11;
//		this.middleCategory12 = middleCategory12;
//		this.middleCategory13 = middleCategory13;
//		this.middleCategory14 = middleCategory14;
//		this.middleCategory15 = middleCategory15;
//		this.middleCategory16 = middleCategory16;
//		this.lastCategory1 = lastCategory1;
//		this.lastCategory2 = lastCategory2;
//		this.lastCategory3 = lastCategory3;
//		this.lastCategory4 = lastCategory4;	
//		this.lastCategory5 = lastCategory5;
//		this.lastCategory6 = lastCategory6;
//		this.lastCategory7 = lastCategory7;
//		this.lastCategory8 = lastCategory8;
//		}	
//	
//	public String getName() {
//		return name;
//	}
//	public String getMiddleCategory1() {
//		return middleCategory1;
//	}
//	public String getMiddleCategory2() {
//		return middleCategory2;
//	}
//	public String getMiddleCategory3() {
//		return middleCategory3;
//	}
//	public String getMiddleCategory4() {
//		return middleCategory4;
//	}
//	public String getMiddleCategory5() {
//		return middleCategory5;
//	}
//	public String getMiddleCategory6() {
//		return middleCategory6;
//	}
//	public String getMiddleCategory7() {
//		return middleCategory7;
//	}
//	public String getMiddleCategory8() {
//		return middleCategory8;
//	}
//	public String getMiddleCategory9() {
//		return middleCategory9;
//	}
//	public String getMiddleCategory10() {
//		return middleCategory10;
//	}
//	public String getMiddleCategory11() {
//		return middleCategory11;
//	}
//	public String getMiddleCategory12() {
//		return middleCategory12;
//	}
//	public String getMiddleCategory13() {
//		return middleCategory13;
//	}
//	public String getMiddleCategory14() {
//		return middleCategory14;
//	}
//	public String getMiddleCategory15() {
//		return middleCategory15;
//	}
//	public String getMiddleCategory16() {
//		return middleCategory16;
//	}
//	public String getLastCategory1() {
//		return lastCategory1;
//	}
//	public String getLastCategory2() {
//		return lastCategory2;
//	}
//	public String getLastCategory3() {
//		return lastCategory3;
//	}
//	public String getLastCategory4() {
//		return lastCategory4;
//	}	
//	public String getLastCategory5() {
//		return lastCategory5;
//	}	
//	public String getLastCategory6() {
//		return lastCategory6;
//	}	
//	public String getLastCategory7() {
//		return lastCategory7;
//	}	
//	public String getLastCategory8() {
//		return lastCategory8;
//	}	
//}
