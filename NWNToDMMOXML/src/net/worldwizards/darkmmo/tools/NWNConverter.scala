package net.worldwizards.darkmmo.tools {

  import com.worldwizards.nwn.files.{KeyTable,NWNTilesetPalette}
  import NWNTilesetPalette._
  import NWNTilesetPalette.NODETYPE._
  import com.worldwizards.nwn.model.bioware.MDL
  import com.worldwizards.nwn.ResourceManager
  import com.worldwizards.nwn.ResourceDescriptor
  import java.io.{FileWriter, PrintWriter, File}
  import java.io.{OutputStream, BufferedOutputStream, FileOutputStream}
  import java.nio.ByteBuffer
  import javax.imageio.ImageIO
  import java.util.regex.{Matcher, Pattern}
  import scala.collection.mutable.{Set,Map}
  import java.awt.image.{AffineTransformOp, BufferedImage}
  import java.awt.geom.AffineTransform
  import com.worldwizards.nwn.files.resources.{Win32INI, NWNImage, GFF}
  import java.awt.Graphics2D

 

  object NWNConverter {
    //this is the other part of adding foreach to Iterators
    implicit def iteratorToWrapper[T](iter: java.util.Iterator[T]): IteratorWrapper[T] = new IteratorWrapper[T](iter)

    val resMgr = new ResourceManager();
    var verbose=false
 
    class TileRecord(val tileNum:Int,val resRef:String)

    def main(args: Array[String]) = {
      verbose = System.getProperty("verbose","false").equalsIgnoreCase("true")
      val nwnpath=   System.getProperty("nwnpath", ".")
      val nwnsrcs:Array[String] = nwnpath.split(";")
      nwnsrcs.foreach(src=>processNWNSrc(src))

      val outDir = System.getProperty("outputdir", "assets")
      val d: File = new File(outDir)
      if (!d.exists) {
        d.mkdir
      }
      if (!d.isDirectory) {
        System.err.println(outDir + " is not a directory")
      }
      //System.getProperties.keySet.iterator.foreach(k => println(k))
      if (System.getProperties.containsKey("models")) {
        val modelSubDir = getAssetDir(d, "models")
        val pngSubDir = getAssetDir(d, "png")
        var modelMatch = System.getProperty("models") // default is match all
        if (modelMatch.equals("")){
          modelMatch=".*"
        }
        val regexp: Pattern = Pattern.compile(modelMatch)
        val mdls = resMgr.listResources("mdl")
        mdls.iterator.foreach(resRec => {
            if (regexp.matcher(resRec.getResRef()).matches) {
              processMdl(modelSubDir, pngSubDir, resRec)
            }
          })
      }
      if (System.getProperties.containsKey("tilesets")){
        val tileSetSubDir = getAssetDir(d, "tile_sets")
        val modelSubDir = getAssetDir(d, "models")
        val pngSubDir = getAssetDir(d, "png")
        var tlsMatch = System.getProperty("tilesets") // default is match all
        if (tlsMatch.equals("")){
          tlsMatch = ".*"
        }
        val regexp: Pattern = Pattern.compile(tlsMatch)
        val tileSets = resMgr.listResources("set")
        tileSets.iterator.foreach(resRec => {
            if (regexp.matcher(resRec.getResRef()).matches) {
              println("Processing set: "+resRec.getResRef())
              processTileSet(tileSetSubDir,modelSubDir, pngSubDir, resRec)
            } else {
              println("Skipped set: "+resRec.getResRef())
            }
          })
      }
    }

    def getAssetDir(root: File, dirname: String): File = {
      var subDir = new File(root, dirname)
      if (!subDir.exists) {
        subDir.mkdir
      }
      if (!subDir.isDirectory) {
        System.err.println(subDir.getName + " is not a directory")
      }
      return subDir
    }

    def processNWNSrc(arg: String): Unit = {
      if (verbose) {
        println("adding nwn src: "+arg)
      }
      val javaFile = new File(arg);
      if (!javaFile.exists) {
        System.err.println("Error: File or Directory " + arg + " does not exist.");
        return
      }
      if (javaFile.isDirectory()) {
        resMgr.addNWNRoot(javaFile);
      } else if (javaFile.getName().endsWith(".key")) {
        resMgr.addKeyTable(new KeyTable(javaFile));
      } else {
        System.out.println("Don't knwo what to do with file named " + arg);
      }
    }

    def processTileSet(tlsDir:File, modelDir: File, pngDir: File, tlsResDesc: ResourceDescriptor): Unit = {
      val tileSet:Win32INI =
        resMgr.getResource(tlsResDesc.getResRef,tlsResDesc.getResType.asInstanceOf[Short]).asInstanceOf[Win32INI]
      val tileSetFile = new File(tlsDir, tlsResDesc.getResRef + ".tls")
      if (!tileSetFile.exists) {
        tileSetFile.delete
      }
      val fw:FileWriter = new FileWriter(tileSetFile)
      val w:TileSetWriter = new TileSetWriter(fw)
      w.openTileSets
      w.openTileSet(tlsResDesc.getResRef, 
                    resMgr.getDialogString(
          Integer.valueOf(tileSet.getINIEntry("GENERAL","DisplayName")).intValue));
      // collect tiles
      val tileCount:Int = tileSet.getINIEntry("TILES","Count").toInt
      val tileMap = Map[String,TileRecord]()
      for(i <- 0 to tileCount-1){
        val p:String = "TILE"+i
        val model:String = tileSet.getINIEntry(p,"Model")
        tileMap += (model->new TileRecord(i,model))
      }
      // open palette
      val pname:String = tlsResDesc.getResRef+"palstd";
      val paletteResource:GFF =
        resMgr.getResource(pname,"itp").asInstanceOf[GFF];
      val palette:NWNTilesetPalette = new NWNTilesetPalette(resMgr,paletteResource);
      w.openTileGroups
      val tileGroups = palette.listGroups
      tileGroups.iterator.foreach(tileGroup => {
    	  w.openTileGroup(tileGroup.name,tileGroup.nodeType.toString)
          tileGroup.nodeType match {
            case FEATURES =>
              tileGroup.tileNodes.iterator.foreach(tileNode => {
                  w.writeTile(tileMap(tileNode.resRef).tileNum, tileNode.name,tileNode.resRef)
    	  	})
            case  GROUPS =>
              tileGroup.tileNodes.iterator.foreach(tileNode => {
                  w.writeTile(tileMap(tileNode.resRef).tileNum, tileNode.name,tileNode.resRef)
    	  	})
            case TERRAIN =>
              // still to be implemented
          }
          w.closeTileGroup
        })
      w.closeTileGroups
      w.closeTileSet
      w.closeTileSets
      fw.flush
      fw.close
    }

    def processMdl(modelDir: File, pngDir: File, mdlResDesc: ResourceDescriptor): Unit = {
      val buff = resMgr.getRawResource(mdlResDesc.getResRef(), mdlResDesc.getResType.asInstanceOf[Short])
      var modelWriter: FileWriter = null
      try {
        val mdl = new MDL(buff, false)
        val modelFile = new File(modelDir, mdlResDesc.getResRef + ".dae")
        if (!modelFile.exists) {
          modelFile.delete
        }
        modelWriter = new FileWriter(modelFile)
        val imageSet: Set[String] = MDL2Collada.convert(mdlResDesc.getResRef(), mdl, modelWriter);
        if (!imageSet.isEmpty) {
          imageSet.foreach(image => processTGAImage(pngDir, resMgr.getResourceDescriptor(image, 3)))
        }
      } catch {
        case e: Exception =>
          System.err.println("Exception processing model " + mdlResDesc.getResRef)
          e.printStackTrace
        case e: IllegalArgumentException  =>
          System.err.println("Exception processing model " + mdlResDesc.getResRef)
          e.printStackTrace
      } finally {
        if (modelWriter!=null){
          modelWriter.flush
          modelWriter.close
        }
      }
    }

    def processTGAImage(dir: File, mdlResDesc: ResourceDescriptor): Unit = {
      val img: NWNImage =
        resMgr.getResource(mdlResDesc.getResRef, mdlResDesc.getResType.asInstanceOf[Short]).asInstanceOf[NWNImage]
      try {
        val imageFile = new File(dir, mdlResDesc.getResRef + ".png")
        if (img == null){ // not availabel as a tga, for now give up
          System.err.println("Couldnt find image "+mdlResDesc.getResRef + ".png")
          return
        }
        var bimg: BufferedImage = img.getImage();
        // Flip the image vertically
        val tx:AffineTransform = new AffineTransform(1, 0, 0, -1, 0, bimg.getHeight());
        try {
          val transformedImage:BufferedImage =
            new BufferedImage(bimg.getWidth,bimg.getHeight,BufferedImage.TYPE_INT_ARGB)
          val g2:Graphics2D = transformedImage.createGraphics()
          g2.transform(tx)
          g2.drawImage(bimg,0,0,null)
          g2.dispose()
          bimg = transformedImage
        } catch {
          case e:java.awt.image.ImagingOpException =>
            System.err.println("Couldnt flip image "+mdlResDesc.getResRef + ".png, "+e.getMessage)
        }
        // Save as PNG
        ImageIO.write(bimg, "png", imageFile);
      } catch {
        case e: Exception =>
          System.err.println("Exception processing model " + mdlResDesc.getResRef)
          e.printStackTrace

      }
    }

  }

}