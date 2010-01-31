package net.worldwizards.darkmmo.tools

import java.io.Writer

/**
 * Created by IntelliJ IDEA.
 * User: jeff
 * Date: Dec 30, 2009
 * Time: 6:39:56 PM
 * To change this template use File | Settings | File Templates.
 */

class TileSetWriter(w:Writer)  {
  var indent:String=""

  def openTileSets {
    w.write(indent+"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
    w.write(indent+"<TileSets xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
            "xsi:noNamespaceSchemaLocation=\"../schemae/tilesets.xsd\">\n")
    indent += "   "
  }

  def closeTileSets {
    indent = indent.substring(3)
    w.write(indent+"</TileSets>\n")
  }

  def openTileSet(name:String,displayName:String) {
    w.write(indent+"<TileSet name=\""+name+"\" displayName=\""+displayName+"\">\n")
    indent += "   "
  }

  def closeTileSet {
    indent = indent.substring(3)
    w.write(indent+"</TileSet>\n")
  }
  
  def openTileGroups {
    w.write(indent+"<TileGroups>\n")
     indent = indent+"   "
  }
  
  def closeTileGroups {
    indent = indent.substring(3)
    w.write(indent+"</TileGroups>\n")
  }
  
  def openTileGroup(name:String,tileType:String) {
    w.write(indent+"<TileGroup name=\""+name+"\" tileType=\""+tileType+"\" >\n")
    indent = indent+"   "
  }
  
  def closeTileGroup() {
    indent = indent.substring(3)
    w.write(indent+"</TileGroup>\n")
  }
  

  
  def writeTile(tilenum:Int, name:String, mdl:String) {
    w.write(indent+"<Tile name=\""+name+"\" tilenum=\""+tilenum+"\" model=\""+mdl+"\"/>\n")
  }
}