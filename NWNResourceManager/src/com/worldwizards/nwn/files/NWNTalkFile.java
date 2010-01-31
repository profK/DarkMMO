package com.worldwizards.nwn.files;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.worldwizards.util.LERandomAccessFile;

public class NWNTalkFile {
	enum STRING_FLAGS {TEXT_PRESENT,SND_PRESENT,SNDLENGTH_PRESENT};
	Map<Integer,StringDataElement> stringCache = new HashMap<Integer,StringDataElement>();
	private class StringDataElement{
		public StringDataElement(int flags2, String soundResRef2,
				float soundLength2, String string) {
			this.flags = (byte) flags2;
			this.soundResRef = soundResRef2;
			this.soundLength = soundLength2;
			this.dlgString = string;
		}
		byte flags;
		String soundResRef;
		float soundLength;
		String dlgString;
	}
	int languageID;
	int stringCount;
	int stringEntriesOffset;
	LERandomAccessFile raf;
	private long stringEntryTablePos;
	private static final int DATA_ELEMENT_SZ = 40;
	private static final int HEADER_SZ = 20;
	
	public NWNTalkFile(File dialog) throws IOException, InvalidFileFormattingException {
		raf = new LERandomAccessFile(dialog,"r");
		byte[] ba4 = new byte[4];
		raf.read(ba4);
		if (!(new String(ba4).equals("TLK "))){
			throw new InvalidFileFormattingException("Does not start with 'TALK '");
		}
		raf.read(ba4);
		String v = new String(ba4);
		if (!v.equals("V3.0")){
			throw new InvalidFileFormattingException("Wrong version found: "+v);
		}
		languageID = raf.readInt();
		stringCount = raf.readInt();
		stringEntriesOffset = raf.readInt();
		
	}
	
	private StringDataElement loadStringDataElement(int strRef) throws IOException{
		raf.seek(HEADER_SZ+(strRef*DATA_ELEMENT_SZ));
		byte[] ba16 = new byte[16];
		int flags = raf.readInt();
		raf.read(ba16);
		String soundResRef = new String(ba16).trim();
		raf.readInt(); // dump the VolumeVarience
		raf.readInt(); // dump the PitchVariance
		int offsetToStr = raf.readInt();
		int stringSize = raf.readInt();
		float soundLength = raf.readFloat();
		byte[] strbytes = new byte[stringSize];
		raf.seek(stringEntriesOffset+offsetToStr);
		raf.read(strbytes);
		StringDataElement sde =new StringDataElement(flags,soundResRef,soundLength,new String(strbytes));
		stringCache.put(strRef,sde);
		return sde;
	}
	
	private StringDataElement getStringDataElement(int strRef){
		StringDataElement sde = stringCache.get(strRef);
		if (sde==null){
			try {
				return loadStringDataElement(strRef);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else {
			return sde;
		}
	}
	
	public String getString(int strRef){
		StringDataElement sde = getStringDataElement(strRef);
		if (sde!=null) {
			return sde.dlgString;
		} else {
			return null;
		}
	}

}
