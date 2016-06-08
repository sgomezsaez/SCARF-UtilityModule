function getFctObject(xml)
{
	var fct = new Object;
	fct.id = $(xml).find('id').text();
	fct.alias = $(xml).find('alias').text();
	fct.formula = $(xml).find('formula').text();
	fct.description = $(xml).find('description').text();
	fct.fctType = $(xml).find('functionType').text();
	fct.author = $(xml).find('author').text();
	fct.parameter = [];
	return fct;	
}

function getParmObject(xml)
{
	var parm = new Object;
	parm.fctId = $(xml).find('functionId').text();
	parm.name = $(xml).find('name').text();
	parm.description = $(xml).find('description').text();
	parm.dataType = $(xml).find('dataType').text();
	parm.author = $(xml).find('author').text();
	parm.def = $(xml).find('defaultValue').text();
	return parm;	
}

function getFunctionTypes()
{
	var types = [];
	$.ajax({	
		type: "GET",
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "Type/FunctionType/",
		async: false,
		success: function(xml) 
		{
			$(xml).find('type').each(function() {
				types.push($(this).text());
			});
		}
	});
	return types;
}

function getDataTypes()
{
	var types = [];
	$.ajax({	
		type: "GET",
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "Type/DataType/",
		async: false,
		success: function(xml) 
		{
			$(xml).find('type').each(function() {
				types.push($(this).text());
			});
		}
	});
	return types;
}

function createFunctionType(fctType)
{
	var result = false;
	$.ajax({	
		type: "POST",
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "Type/FunctionType/" + fctType,
		async: false,
		success: function(xml) 
		{
			result = true;
		}
	});
	return result;
}

function deleteFunctionType(fctType)
{
	var result = false;
	$.ajax({	
		type: "DELETE",
		crossDomain: true,
		url: getKeretaRoot() + "Type/FunctionType/" + fctType,
		async: false,
		success: function(xml) 
		{
			result = true;
		}
	});
	return result;
}

function getNefologParameter(subFct)
{
	var parms = [];
	
	if (subFct == null) return parms;
	
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "UtilityFunction/" + subFct.ufId + "/SubFunction/" + subFct.number + "/NefologParameter/",
		async: false,
		success: function(xml) 
		{
			$(xml).find('parameter').each(function() {
				
				var parm = new Object;
				parm.fctId = "";
				parm.name = $(this).text();
				parm.description = "";
				parm.dataType = "number";
				parm.author = "";
				parm.def = "";
				
				parms.push(parm);
			});
		}
	});
	return parms;
}

function getParameter(fctId, name)
{
	var parm = null;
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "Function/" + fctId + "/Parameter/" + name,
		async: false,
		success: function(xml) 
		{
			parm = getParmObject(xml.documentElement);
		}
	});
	return parm;
}

function getParameters(fctId)
{
	var parms = [];
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "Function/" + fctId + "/Parameter/",
		async: false,
		success: function(xml) 
		{
			$(xml).find('parameter').each(function() {
					
				var parm = getParmObject(this);
				parms.push(parm);
			});
		}
	});
	return parms;
}

function createParameter(fctId, name)
{
	var parm;
	$.ajax({
		url: getKeretaRoot() + "Function/" + fctId + "/Parameter/" + name,
		type: "POST",
		crossDomain: true,
		dataType: "xml",
		async: false,
		success: function(xml) 
		{
			parm = getParmObject(xml.documentElement);
		}
	});
	return parm;
}

function persistParameter(parm)
{
	var result = false;
	if (parm == null) return result;
		
	var doc = $.parseXML("<parameter/>");
	var dataType = doc.createElement("dataType");
	var desc = doc.createElement("description");
	var author = doc.createElement("author");
	
	dataType.appendChild(doc.createTextNode(parm.dataType));
	desc.appendChild(doc.createTextNode(parm.description));
	author.appendChild(doc.createTextNode(parm.author));
	
	doc.documentElement.appendChild(dataType);
	doc.documentElement.appendChild(desc);
	doc.documentElement.appendChild(author);
	data = (new XMLSerializer()).serializeToString(doc);
	
	$.ajax({	
		url: getKeretaRoot() + "Function/" + parm.fctId + "/Parameter/" + parm.name,
		data: data,
		type: "PUT",
		crossDomain: true,
		contentType: "application/xml",
		dataType: "xml",
		async: false,
		success: function(xml) 
		{
			result = true;
		},
		error: function(error, status)
		{
			result = false;
		}
	});
	return result;
}

function deleteParameter(parm)
{
	var result = false;
	if (parm == null) return result;
	
	$.ajax({	
		type: "DELETE",
		crossDomain: true,
		url: getKeretaRoot() + "Function/" + parm.fctId + "/Parameter/" + parm.name,
		async: false,
		success: function() 
		{
			result = true;
		}
	});
	return result;
}

function getFunction(id)
{
	var fct;
	$.ajax({	
		type: "GET",
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "Function/" + id,
		async: false,
		success: function(xml) 
		{
			fct = getFctObject(xml.documentElement);
			fct.parameter = getParameters(fct.id);
		}
	});
	return fct;
}

function getFunctions()
{
	var fcts = [];
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "Function/",
		async: false,
		success: function(xml) 
		{
			$(xml).find('function').each(function() {
					
				var fct = getFctObject(this);
				fct.parameter = getParameters(fct.id);
				fcts.push(fct);
			});
		}
	});
	return fcts;
}

function selectFunctions(fctType)
{
	var fcts = [];
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		url: getKeretaRoot() + "Search?resource=Function&functionType=" + fctType,
		async: false,
		success: function(xml) 
		{
			$(xml).find('function').each(function() {
					
				var fct = getFctObject(this);
				fcts.push(fct);
			});
		}
	});
	return fcts;
}

function persistFunction(fct)
{
	var result = false;
	if (fct == null) return result;
		
	var doc = $.parseXML("<function/>");
	var formula = doc.createElement("formula");
	var alias = doc.createElement("alias");
	var fctType = doc.createElement("functionType");
	var desc = doc.createElement("description");
	var author = doc.createElement("author");
	
	formula.appendChild(doc.createTextNode(fct.formula));
	alias.appendChild(doc.createTextNode(fct.alias));
	fctType.appendChild(doc.createTextNode(fct.fctType));
	desc.appendChild(doc.createTextNode(fct.description));
	author.appendChild(doc.createTextNode(fct.author));
	
	doc.documentElement.appendChild(formula);
	doc.documentElement.appendChild(alias);
	doc.documentElement.appendChild(fctType);
	doc.documentElement.appendChild(desc);
	doc.documentElement.appendChild(author);

	data = (new XMLSerializer()).serializeToString(doc);

	$.ajax({	
		url: getKeretaRoot() + "Function/" + fct.id,
		data: data,
		type: "PUT",
		crossDomain: true,
		contentType: "application/xml",
		dataType: "xml",
		async: false,
		success: function(xml) 
		{
			result = true;
		},
		error: function(error, status)
		{
			result = false;
		}
	}); 
	return result;
}

function createFunction()
{
	var fct;
	$.ajax({
		url: getKeretaRoot() + "Function/",
		type: "POST",
		crossDomain: true,
		dataType: "xml",
		async: false,
		success: function(xml) 
		{
			fct = getFctObject(xml.documentElement);
		}
	});
	return fct;
}

function deleteFunction(fct)
{
	var result = false;
	if (fct == null) return result;
	
	$.ajax({	
		type: "DELETE",
		url: getKeretaRoot() + "Function/" + fct.id,
		async: false,
		success: function() 
		{
			result = true;
		}
	});
	return result;
}