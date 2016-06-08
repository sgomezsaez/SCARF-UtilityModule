function getUfObject(xml)
{
	var uf = new Object;
	uf.id = $(xml).find('id').text();
	uf.alias = $(xml).find('alias').text();
	uf.dstrId = $(xml).find('distributionId').text();
	uf.subFct = [];
	return uf;	
}

function getSubFctObject(xml)
{
	var subFct = new Object;
	subFct.ufId = $(xml).find('utilityFunctionId').text();
	subFct.number = $(xml).find('number').text();
	subFct.fctId = $(xml).find('functionId').text();
	return subFct;	
}

function getSubFunctions(ufId)
{
	var subFcts = [];
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "UtilityFunction/" + ufId + "/SubFunction/",
		async: false,
		success: function(xml) 
		{
			$(xml).find('subFunction').each(function() {
					
				var subFct = getSubFctObject(this);
				subFcts.push(subFct);
			});
		}
	});
	return subFcts;
}

function getSubFunction(ufId, sbFctNbr)
{
	var subFct = null;
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "UtilityFunction/" + ufId + "/SubFunction/" + sbFctNbr,
		async: false,
		success: function(xml) 
		{
			subFct = getSubFctObject(xml.documentElement);
		}
	});
	return subFct;
}

function assignSubFunction(subFct, query)
{
	var result = false;
	if (subFct == null) return result;
	
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "UtilityFunction/" + subFct.ufId + "/SubFunction/" + subFct.number + "/assign?" + query,
		async: false,
		success: function(xml) 
		{
			result = true;
		}
	});
	return result;
}

function calcSubFunction(subFct, key)
{
	var result = NaN;
	if (subFct == null) return result;
	
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "UtilityFunction/" + subFct.ufId + "/SubFunction/" + subFct.number + "/calc?key=" + key,
		async: false,
		success: function(xml) 
		{
			result = $(xml).find('result').text();
		}
	});
	return result;
}

function calcUtilityFunction(uf, key)
{
	var result = NaN;
	if (uf == null) return result;
	
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "UtilityFunction/" + uf.id + "/calc?key=" + key,
		async: false,
		success: function(xml) 
		{
			result = $(xml).find('result').text();
			result = Math.round(parseFloat(result));
		}
	});
	return result;
}

function getUtilityFunctions(dstrId)
{
	var ufs = [];
	
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "Distribution/" + dstrId + "/UtilityFunction/",
		async: false,
		success: function(xml) 
		{
			$(xml).find('utilityFunction').each(function() {
					
				var uf = getUfObject(this);
				uf.subFct = getSubFunctions(uf.id);
				ufs.push(uf);
			});
		}
	});
	return ufs;
}

function getUtilityFunction(ufId)
{
	var uf = null;
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "UtilityFunction/" + ufId,
		async: false,
		success: function(xml) 
		{
			uf = getUfObject(xml.documentElement);
			uf.subFct = getSubFunctions(uf.id);
		}
	});
	return uf;
}

function deleteUtilityFunction(uf)
{
	var result = false;
	if (uf == null) return result;
	
	$.ajax({	
		type: "DELETE",
		crossDomain: true,
		url: getKeretaRoot() + "UtilityFunction/" + uf.id,
		async: false,
		success: function() 
		{
			result = true;
		}
	});
	return result;
}

function deleteSubFunction(subFct)
{
	var result = false;
	if (subFct == null) return result;
	
	$.ajax({	
		type: "DELETE",
		crossDomain: true,
		url: getKeretaRoot() + "UtilityFunction/" + subFct.ufId + "/SubFunction/" + subFct.number,
		async: false,
		success: function() 
		{
			result = true;
		}
	});
	return result;
}

function persistUtilityFunction(uf)
{
	var result = false;
	if (uf == null) return result;
		
	var doc = $.parseXML("<utilityFunction/>");
	var alias = doc.createElement("alias");
	alias.appendChild(doc.createTextNode(uf.alias));
	doc.documentElement.appendChild(alias);
	
	data = (new XMLSerializer()).serializeToString(doc);
	
	$.ajax({	
		url: getKeretaRoot() + "UtilityFunction/" + uf.id,
		crossDomain: true,
		data: data,
		type: "PUT",
		contentType: "application/xml",
		dataType: "xml",
		
		async: false,
		success: function(xml) 
		{
			result = true;
		},
		error: function(error)
		{
			result = false;
		}
	});
	return result; 
}

function persistSubFunction(subFct)
{
	var result = false;
	if (subFct == null) return result;
		
	var doc = $.parseXML("<subFunction/>");
	var fctId = doc.createElement("functionId");
	fctId.appendChild(doc.createTextNode(subFct.fctId));
	doc.documentElement.appendChild(fctId);
	
	data = (new XMLSerializer()).serializeToString(doc);
	
	$.ajax({	
		url: getKeretaRoot() + "UtilityFunction/" + subFct.ufId + "/SubFunction/" + subFct.number,
		crossDomain: true,
		data: data,
		type: "PUT",
		contentType: "application/xml",
		dataType: "xml",
		
		async: false,
		success: function(xml) 
		{
			result = true;
		},
		error: function(error)
		{
			result = false;
		}
	});
	return result; 
}

function createUtilityFunction(dstrId)
{
	var uf = null;
	
	$.ajax({	
		type: "POST",
		crossDomain: true,
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "Distribution/" + dstrId + "/UtilityFunction",
		async: false,
		success: function(xml) 
		{
			uf = getUfObject(xml.documentElement);
		}
	});
	return uf;
}

function createSubFunction(ufId, number)
{
	var subFct = null;
	
	$.ajax({	
		type: "POST",
		crossDomain: true,
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "UtilityFunction/" + ufId + "/SubFunction/" + number,
		async: false,
		success: function(xml) 
		{
			subFct = getSubFctObject(xml.documentElement);
		}
	});
	return subFct;
}