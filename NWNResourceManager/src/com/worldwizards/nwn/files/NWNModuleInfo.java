/*
 * Created on Apr 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.worldwizards.nwn.files;

import java.io.PrintStream;

import com.worldwizards.nwn.files.resources.GFF;
import com.worldwizards.nwn.files.resources.fields.CExoString;
import com.worldwizards.nwn.files.resources.fields.GFFList;
import com.worldwizards.nwn.files.resources.fields.Struct;
import java.io.Serializable;

public class NWNModuleInfo implements Serializable{
    public static final long serialversionUID= 1L;
    
	GFF gff;
	String[] moduleAreas;
	short expansionPacks;
	String customTLKfile;
	byte dawnHour;
	String description;
	byte duskHour;
	String startingArea;
	float startingFacing;
	float startX,startY,startZ;
	String[] hakPacks;
	//byte[] moduleID = new byte[16];
	byte isSavedGame;
	String minGameVersion;
	byte minutesPerHour;
	String moduleName;
	byte startDay,startHour,startMonth;
	int startYear;
	String introMovie;
	String moduleTag;
	int moduleVersion;
	byte xpScale;
	
	public NWNModuleInfo(GFF resource,int locale){
		gff = resource;
		expansionPacks = gff.getWord("Expansion_Pack").shortValue();
		CExoString customTlkExo = gff.getCExoString("Mod_CustomTlk");
		if (customTlkExo!=null){
			customTLKfile = customTlkExo.stringValue();
		}
		dawnHour = gff.getByte("Mod_DawnHour").byteValue();
		duskHour = gff.getByte("Mod_DuskHour").byteValue();
		description = gff.getCExoLocString("Mod_Description").getString(locale);
		startingArea = gff.getResRef("Mod_Entry_Area").stringValue();
		float facingVecX = gff.getFloat("Mod_Entry_Dir_X").floatValue();
		float facingVecY = gff.getFloat("Mod_Entry_Dir_Y").floatValue();
		startingFacing = (float)Math.atan2((double)facingVecY,(double)facingVecX);
		startX = gff.getFloat("Mod_Entry_X").floatValue();
		startY = gff.getFloat("Mod_Entry_Y").floatValue();
		startZ = gff.getFloat("Mod_Entry_Z").floatValue();
		isSavedGame = gff.getByte("Mod_IsSaveGame").byteValue();
		CExoString minGameVerExo = gff.getCExoString("Mod_MinGameVer");
		if (minGameVerExo != null ){
			minGameVersion = minGameVerExo.stringValue();
		}
		minutesPerHour = gff.getByte("Mod_MinPerHour").byteValue();
		moduleName = gff.getCExoLocString("Mod_Name").getString(locale);		
		startDay = gff.getByte("Mod_StartDay").byteValue();
		startHour = gff.getByte("Mod_StartHour").byteValue();
		startMonth = gff.getByte("Mod_StartMonth").byteValue();
		startYear = gff.getDword("Mod_StartYear").intValue();
		introMovie = gff.getResRef("Mod_StartMovie").stringValue();
		moduleTag = gff.getCExoString("Mod_Tag").stringValue();
		moduleVersion = gff.getDword("Mod_Version").intValue();
		xpScale = gff.getByte("Mod_XPScale").byteValue();
                GFFList areaListGFF = gff.getList("Mod_Area_list");
                moduleAreas = new String[areaListGFF.length()];
                for(int i=0;i<areaListGFF.length();i++){
                    Struct listStruct = areaListGFF.getStruct(i);
                    moduleAreas[i] = 
                            listStruct.getResRef("Area_Name").stringValue();
                }
                
	}

	/**
	 * 
	 */
	public void dump(PrintStream strm) {
		strm.println("Module Name:"+moduleName);
		strm.println("Description: "+description);
		//String[] moduleAreas;
		strm.println("Expansion Pack Mask: "+expansionPacks);
		strm.println("Custom TLK: "+customTLKfile);
		strm.println("Dawn/Dusk: "+dawnHour+"/"+duskHour);
		strm.println("Starting Area: "+startingArea);
		strm.println("Starting Location: "+startX+","+startY+","+startZ);
		strm.println("Starting facing: "+startingFacing);		
		//String[] hakPacks;
		//byte[] moduleID = new byte[16];
		strm.println("Is Saved Game: "+isSavedGame);
		strm.println("Minimum game version: "+minGameVersion);
		strm.println("Real World min per game hour: "+minutesPerHour);
		strm.println("Start Year/Month/Day/Hour: "+startYear+"/"+startMonth+"/"+startDay+
				"/"+startHour);
		strm.println("Intro Movie: "+introMovie);
		strm.println("Module Tag: "+moduleTag);
		strm.println("Module Version: "+moduleVersion);
		strm.println("XP Scale: "+xpScale);
		
	}
	
	/**
	 * @return Returns the customTLKfile.
	 */
	public String getCustomTLKfile() {
		return customTLKfile;
	}
	/**
	 * @return Returns the dawnHour.
	 */
	public byte getDawnHour() {
		return dawnHour;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return Returns the duskHour.
	 */
	public byte getDuskHour() {
		return duskHour;
	}
	/**
	 * @return Returns the expansionPacks.
	 */
	public short getExpansionPacks() {
		return expansionPacks;
	}
	/**
	 * @return Returns the gff.
	 */
	public GFF getGff() {
		return gff;
	}
	/**
	 * @return Returns the hakPacks.
	 */
	public String[] getHakPacks() {
		return hakPacks;
	}
	/**
	 * @return Returns the introMovie.
	 */
	public String getIntroMovie() {
		return introMovie;
	}
	/**
	 * @return Returns the isSavedGame.
	 */
	public byte getIsSavedGame() {
		return isSavedGame;
	}
	/**
	 * @return Returns the minGameVersion.
	 */
	public String getMinGameVersion() {
		return minGameVersion;
	}
	/**
	 * @return Returns the minutesPerHour.
	 */
	public byte getMinutesPerHour() {
		return minutesPerHour;
	}
	/**
	 * @return Returns the moduleAreas.
	 */
	public String[] getModuleAreas() {
		return moduleAreas;
	}
	/**
	 * @return Returns the moduleName.
	 */
	public String getModuleName() {
		return moduleName;
	}
	/**
	 * @return Returns the moduleTag.
	 */
	public String getModuleTag() {
		return moduleTag;
	}
	/**
	 * @return Returns the moduleVersion.
	 */
	public int getModuleVersion() {
		return moduleVersion;
	}
	/**
	 * @return Returns the startDay.
	 */
	public byte getStartDay() {
		return startDay;
	}
	/**
	 * @return Returns the startHour.
	 */
	public byte getStartHour() {
		return startHour;
	}
	/**
	 * @return Returns the startingArea.
	 */
	public String getStartingArea() {
		return startingArea;
	}
	/**
	 * @return Returns the startingFacing.
	 */
	public float getStartingFacing() {
		return startingFacing;
	}
	/**
	 * @return Returns the startMonth.
	 */
	public byte getStartMonth() {
		return startMonth;
	}
	/**
	 * @return Returns the startX.
	 */
	public float getStartX() {
		return startX;
	}
	/**
	 * @return Returns the startY.
	 */
	public float getStartY() {
		return startY;
	}
	/**
	 * @return Returns the startYear.
	 */
	public int getStartYear() {
		return startYear;
	}
	/**
	 * @return Returns the startZ.
	 */
	public float getStartZ() {
		return startZ;
	}
	/**
	 * @return Returns the xpScale.
	 */
	public byte getXpScale() {
		return xpScale;
	}
}
