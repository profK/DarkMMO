/**
 * This file sets up various utilities that the DarkMMO code depends on
 * @author jeff
 * @copyright WorldWizards 2010
 */



var Utils = {
    getHTMLDir : function(url,successFunc,failure){
        if (url.charAt(url.length-1)!='/'){
            failure("URL must be a directory (end with /)")
            return
        }
        $.ajax({
            url: url,
            success: function(data){ // success
                var tempDiv = document.createElement("div");
                data = data.replace(/<![^>]*>/,"")
                console.log(data)
                tempDiv.innerHTML = data.replace(/<script(.|\s)*?\/script>/g, '');

                // tempDiv now has a DOM structure:
               / //tempDiv.childNodes;
                var links = tempDiv.getElementsByTagName('a'); // etc.
                var dirNames = new Array()
                //console.log(links)
                for(var i=0;i<links.length;i++){
                    dirNames.push(links[i].getAttribute("href").toString())
                }
                //console.log(dirNames)
                successFunc(dirNames)
            },
            error: function(XMLHttpRequest, textStatus, errorThrown){ //failures
               failure(textStatus+":"+errorThrown)
            }
        })
    }
}