
function getKeretaRoot()
{ 
	//return "http://localhost:8090/Kereta/";
	//return "http://" + window.location.hostname + ":" + window.location.port + "/Kereta/";
	return "http://129.69.214.138:8090/Kereta/";

}

function kereta_createWrapper(contentNode)
{
	$("#keretaWrapper").remove();
	var wrapper = document.createElement("div");
	wrapper.setAttribute("id", "keretaWrapper");
	var cnt = document.createElement("div");
	cnt.setAttribute("class", "container");
	var col = document.createElement("div");
	col.setAttribute("class", "col-xs-12");
	col.setAttribute("id", "keretaContent");
	var close = document.createElement("span");
	close.setAttribute("class", "glyphicon glyphicon-off");
	
	col.appendChild(close);
	$(col).append(kereta_menuBar());
	col.appendChild(contentNode);
	cnt.appendChild(col);
	wrapper.appendChild(cnt);
	document.body.appendChild(wrapper);
	
	$(".glyphicon-off").on("click", function() 
	{
			$("#keretaWrapper").remove();
	});
}

function kereta_createSmallWrapper(contentNode)
{
	$("#keretaWrapper").remove();
	var wrapper = document.createElement("div");
	wrapper.setAttribute("id", "keretaWrapper");
	var cnt = document.createElement("div");
	cnt.setAttribute("class", "container");
	var col = kereta_createElement("div", "keretaContent", "col-xs-12 col-md-8 col-lg-6");
	var spc = kereta_createElement("div", "", "hidden-xs col-md-2 col-lg-3");
	spc.textContent = "\ ";
	var close = document.createElement("span");
	close.setAttribute("class", "glyphicon glyphicon-off");
	col.appendChild(close);
	col.appendChild(contentNode);
	cnt.appendChild(spc);
	cnt.appendChild(col);
	
	wrapper.appendChild(cnt);
	document.body.appendChild(wrapper);
	
	$(".glyphicon-off").on("click", function() 
	{
			$("#keretaWrapper").remove();
	});
}

function kereta_replaceURI(url)
{
	return url.replace(/:/g, "%253A").replace(/\//g, "%252F");
}

function kereta_replaceXML(xml)
{
	return xml.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/, "&quot;").replace(/'/, "&apos;").replace(/&/, "&amp;");
}


function kereta_menuBar(active)
{
	var str = "<div class='keretaMenubar'>" +
			  "<button class='btn btn-primary' role='button' onClick='kereta_appGUI();'>Applications</button> " +
              "<button class='btn btn-primary' role='button' onClick='kereta_fctGUI();'>Functions</button> " +
              "<button class='btn btn-primary' role='button' onClick='kereta_ufGUI(\"\", \"\");'>Utility Functions</button> " +
              "<button class='btn btn-primary' role='button' onClick='kereta_distributionGUI();'>Search Distributions</button> " +
			  "</div>";
	var cnt = $.parseHTML(str);
	return cnt;
}

function kereta_formItem(label, itemNode)
{
	var cnt = document.createElement("div");
	cnt.setAttribute("class", "col-sm-6 col-md-4 formCol");
	var lbl = document.createElement("span");
	lbl.textContent = label;
	cnt.appendChild(lbl);
	cnt.appendChild(document.createElement("br"));
	cnt.appendChild(itemNode);
	return cnt;
}

function kereta_createElement(tag, id, className)
{
	var elt = document.createElement(tag);
	if (!className == "") elt.setAttribute("class", className);
	if (!id == "") elt.setAttribute("id", id);
	return elt;
}

function kereta_GUI()
{
	var cntNode = kereta_createElement("div", "keretaAnchor", "col-lg-12");
	kereta_createWrapper(cntNode);
}

function kereta_anchor(headline, cnt)
{
	$("#keretaAnchor").empty();
	//var hl = $.parseHTML("<h3>" + headline + "</h3>");
	//$("#keretaAnchor").append(hl);
	for (var i=0; i< cnt.length; i++)
	{
		if (cnt != "") $("#keretaAnchor").append($(cnt[i]));
	}
}
