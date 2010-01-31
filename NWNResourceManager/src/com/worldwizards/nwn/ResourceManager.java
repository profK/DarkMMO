package com.worldwizards.nwn;

import java.io.*;
import java.nio.*;
import java.util.*;

import com.worldwizards.nwn.files.*;

public class ResourceManager {

    List resourceErfs = new ArrayList();
    List resourceDirectories = new ArrayList();
    List keyTables = new ArrayList();
    NWNTalkFile talkFile;

    public ResourceManager() {
    }

    public ResourceManager(String nwndir) {
        File datadir = new File(nwndir);
        if ((!datadir.exists()) || (!datadir.isDirectory())) {
            System.err.println(nwndir + " is not a valid directory");
            return;
        }
        // set up
        //File overRideDir = new File(datadir, "override");
        //if (overRideDir.exists()) {
        //    addDirectory(overRideDir);
        //}
        File[] keyFiles = datadir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".key");
            }
        });
        for (File f : keyFiles) {
            KeyTable keyTable = new KeyTable(f);
            addKeyTable(keyTable);
        }
        File dialog = new File(datadir,"dialog.tlk");
        if (dialog.exists()){
        	try {
				talkFile = new NWNTalkFile(dialog);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidFileFormattingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    public void addErf(ERF erf) {
        resourceErfs.add(erf);
    }

    public void addDirectory(File dir) {
        if (!dir.isDirectory()) {
            System.err.println("Error: attempted to add file " + dir +
                    " to resource directories (is not a directory).");
        }
        try {
            resourceDirectories.add(new ResourceDirectory(dir));
            
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        }
    }

    public void addKeyTable(KeyTable keyTable) {
        keyTables.add(keyTable);
    }

    public NWNResource getResource(String filename) {
        int dotloc = filename.indexOf(".");
        if (dotloc == -1) {
            System.err.println("Error: no extension found for " + filename);
            return null;
        }
        String resref = filename.substring(0, dotloc);
        String ext = filename.substring(dotloc + 1);
        short resType = NWNResource.getResType(ext);
        if (resType == -1) {
            System.err.println("Error: no resType regisstred for ext " + ext);
            return null;
        }
        return getResource(resref, resType);
    }

    public NWNResource getResource(String tileSetResRef, String string) {
        return getResource(tileSetResRef, ResourceID.idForExt(string));
    }

    public NWNResource getResource(String resRef, short resType) {
        for (int i = resourceErfs.size() - 1; i >= 0; i--) { // search last first
            ERF erf = (ERF) resourceErfs.get(i);
            NWNResource res = erf.getResource(resRef, resType);
            if (res != null) {
                return res;
            }
        }
        for (int i = resourceDirectories.size() - 1; i >= 0; i--) {
            ResourceDirectory rd =
                    (ResourceDirectory) resourceDirectories.get(i);
            NWNResource res = rd.getResource(resRef, resType);
            if (res != null) {
                return res;
            }
        }
        for (int i = keyTables.size() - 1; i >= 0; i--) {
            KeyTable kt = (KeyTable) keyTables.get(i);
            NWNResource res = kt.getResource(resRef, resType);
            if (res != null) {
                return res;
            }
        }

        return null;
    }

    public ResourceDescriptor getResourceDescriptor(String resRef, short resType) {
        return new ResourceDescriptor(resRef,resType);
    }


    public ByteBuffer getRawResource(String resRef, String string) {
        return getRawResource(resRef, ResourceID.idForExt(string));
    }

    /**
     * getRawResource
     *
     * @param resRef String
     * @param resType short
     * @return ByteBuffer
     */
    public ByteBuffer getRawResource(String resRef, short resType) {
        for (int i = resourceErfs.size() - 1; i >= 0; i--) { // search last first
            ERF erf = (ERF) resourceErfs.get(i);
            ByteBuffer res = erf.getRawResource(resRef, resType);
            if (res != null) {
                return res;
            }
        }
        for (int i = resourceDirectories.size() - 1; i >= 0; i--) {
            ResourceDirectory rd =
                    (ResourceDirectory) resourceDirectories.get(i);
            ByteBuffer res = rd.getRawResource(resRef, resType);
            if (res != null) {
                return res;
            }
        }
        for (int i = keyTables.size() - 1; i >= 0; i--) {
            KeyTable kt = (KeyTable) keyTables.get(i);
            ByteBuffer res = kt.getRawResource(resRef, resType);
            if (res != null) {
                return res;
            }
        }

        return null;

    }

    public long getRawResourceSize(String resRef, short resType) {
        for (int i = resourceErfs.size() - 1; i >= 0; i--) { // search last first
            ERF erf = (ERF) resourceErfs.get(i);
            long sz = erf.getRawResourceSize(resRef, resType);
            if (sz > 0) {
                return sz;
            }
        }
        for (int i = resourceDirectories.size() - 1; i >= 0; i--) {
            ResourceDirectory rd =
                    (ResourceDirectory) resourceDirectories.get(i);
            long sz = rd.getRawResourceSize(resRef, resType);
            if (sz > 0) {
                return sz;
            }
        }
        for (int i = keyTables.size() - 1; i >= 0; i--) {
            KeyTable kt = (KeyTable) keyTables.get(i);
            long sz = kt.getRawResourceSize(resRef, resType);
            if (sz > 0) {
                return sz;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        ResourceManager mgr = new ResourceManager();
        File currentDir = new File(System.getProperty("user.dir"));
        File[] keyFiles = currentDir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return (name.endsWith(".key"));
            }
        });
        for (int i = 0; i < keyFiles.length; i++) {
            try {
                FileInputStream fis = new FileInputStream(keyFiles[i]);
                mgr.addKeyTable(new KeyTable(fis.getChannel()));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        String command;
        BufferedReader rdr =
                new BufferedReader(new InputStreamReader(System.in));
        do {
            try {
                String inline = rdr.readLine();
                StringTokenizer tok = new StringTokenizer(inline);
                command = tok.nextToken();
            } catch (IOException ex1) {
                ex1.printStackTrace();
                command = "quit";
            }
        } while (!command.equalsIgnoreCase("quit"));
    }

    /**
     * listResources
     *
     * @return Set
     */
    public Set<ResourceDescriptor> listResources() {
        Set resourceSet = new HashSet();
        for (int i = resourceErfs.size() - 1; i >= 0; i--) { // search last first
            ERF erf = (ERF) resourceErfs.get(i);
            erf.listResources(resourceSet);
        }
        for (int i = resourceDirectories.size() - 1; i >= 0; i--) {
            ResourceDirectory rd =
                    (ResourceDirectory) resourceDirectories.get(i);
            rd.listResources(resourceSet);
        }
        for (int i = keyTables.size() - 1; i >= 0; i--) {
            KeyTable kt = (KeyTable) keyTables.get(i);
            kt.listResources(resourceSet);
        }
        return resourceSet;
    }

    /**
     * listResources
     *
     * @param extension String
     * @return Set
     */
    public Set<ResourceDescriptor> listResources(String extension) {
        Set<ResourceDescriptor> resourceSet = new HashSet<ResourceDescriptor>();
        int resType = NWNResource.getResType(extension);
        for (int i = resourceErfs.size() - 1; i >= 0; i--) { // search last first
            ERF erf = (ERF) resourceErfs.get(i);
            erf.listResources(resourceSet, resType);
        }
        for (int i = resourceDirectories.size() - 1; i >= 0; i--) {
            ResourceDirectory rd =
                    (ResourceDirectory) resourceDirectories.get(i);
            rd.listResources(resourceSet, resType);
        }
        for (int i = keyTables.size() - 1; i >= 0; i--) {
            KeyTable kt = (KeyTable) keyTables.get(i);
            kt.listResources(resourceSet, resType);
        }
        return resourceSet;

    }

	public void addNWNRoot(File nwnDataDir) {
		// add all key/bif pairs in directory
		for(File f : nwnDataDir.listFiles(new FilenameFilter(){

			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".key");
			}})){
			addKeyTable(new KeyTable(f));
		}
		File dlog = new File(nwnDataDir,"dialog.tlk");
		if (dlog.exists()){
			try {
				talkFile = new NWNTalkFile(dlog);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidFileFormattingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void addModule(File file) {
		try {
			addErf(new ERF(new FileInputStream(file).getChannel()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addDialogFile(File dlog) throws IOException, InvalidFileFormattingException{
		talkFile= new NWNTalkFile(dlog);
	}
	
	public String getDialogString(int strRef) throws NoDialogTalkFileException{
		if (talkFile!=null){
			return talkFile.getString(strRef);
		}
		throw new NoDialogTalkFileException("Did not find a Dialog.tlk in any of the nwn data dirs");
	}

	
}
