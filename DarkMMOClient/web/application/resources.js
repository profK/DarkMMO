/**
 * This file implements resource loading from the various DarkMMO XML files
 * It depends on JQuery
 * @author jeff kesselman
 * @Copright WorldWizards 2010
 */


function loadResourceGroup(urlString,callback){
	$.ajax({
    type: "GET",
    url: urlString,
    dataType: "xml",
	error: function (XMLHttpRequest, textStatus, errorThrown){
		//console.log("ajax error: "+XMLHttpRequest+" "+textStatus+" "+errorThrown)
	},
    success: function(xml){
		// make our resourceGroup object
		var resourceGroup = {
			tilesetGroups:[]
		}
		// parse the XML into the resource group object
		console.log("checkign for TileSets")
		$(xml).find("TileSets").each(function(){
			var base = $(this).attr("baseDir")
			if (base==undefined){
				base=""
			}
			var tileSetGroup = {
				baseDir: base,
				tilesets: []
			}
			//console.log("tileset base="+base)
			$(this).find("TileSet").each(function(){
				//console.log("tileset="+$(this).text())
				tileSetGroup.tilesets.push($(this).text())
			})
			resourceGroup.tilesetGroups.push(tileSetGroup)
		})
		callback(urlString,resourceGroup)
	}
  });
}


