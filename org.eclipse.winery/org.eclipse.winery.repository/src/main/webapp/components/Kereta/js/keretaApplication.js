function getAppObject(xml)
{
	var app = new Object;
	app.id = $(xml).find('id').text();
	app.alias = $(xml).find('alias').text();
	app.name = $(xml).find('name').text();
	app.description = $(xml).find('description').text();
	app.appType = $(xml).find('applicationType').text();
	app.author = $(xml).find('author').text();
	return app;	
}

function getDstrObject(xml)
{
	var dstr = new Object;
	dstr.id = $(xml).find('id').text();
	dstr.alias = $(xml).find('alias').text();
	dstr.appId = $(xml).find('applicationId').text();
	dstr.representation = $(xml).find('representation').text();
	dstr.lang = $(xml).find('language').text();
	dstr.langVersion = $(xml).find('langVersion').text();
	dstr.author = $(xml).find('author').text();
	dstr.namespace = $(xml).find('namespace').text();
	return dstr;	
}

function getApplicationTypes()
{
	var types = [];
	$.ajax({	
		type: "GET",
		dataType: "xml",
		crossDomain: true,
		url: getKeretaRoot() + "Type/ApplicationType/",
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

function createApplicationType(appType)
{
	var result = false;
	$.ajax({	
		type: "POST",
		dataType: "xml",
		url: getKeretaRoot() + "Type/ApplicationType/" + appType,
		async: false,
		success: function(xml) 
		{
			result = true;
		}
	});
	return result;
}

function deleteApplicationType(appType)
{
	var result = false;
	$.ajax({	
		type: "DELETE",
		url: getKeretaRoot() + "Type/ApplicationType/" + appType,
		async: false,
		success: function(xml) 
		{
			result = true;
		}
	});
	return result;
}

function getApplication(id)
{
	var app;
	$.ajax({	
		type: "GET",
		dataType: "xml",
		url: getKeretaRoot() + "Application/" + id,
		async: false,
		success: function(xml) 
		{
			app = getAppObject(xml.documentElement);
		}
	});
	return app;
}

function getApplications()
{
	var apps = [];
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		url: getKeretaRoot() + "Application/",
		async: false,
		success: function(xml) 
		{
			$(xml).find('application').each(function() {
					
				var app = getAppObject(this);
				apps.push(app);
			});
		}
	});
	return apps;
}

function getDistributions(appId)
{
	var dstrs = [];
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		url: getKeretaRoot() + "Application/" + appId + "/Distribution/",
		async: false,
		success: function(xml) 
		{
			$(xml).find('distribution').each(function() {
					
				var dstr = getDstrObject(this);
				dstrs.push(dstr);
			});
		}
	});
	return dstrs;
}

function getDistributionsByAppType(appType)
{
	var dstrs = [];
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		url: getKeretaRoot() + "Search?resource=Distribution&applicationType=" + appType,
		async: false,
		success: function(xml) 
		{
			$(xml).find('distribution').each(function() {
					
				var dstr = getDstrObject(this);
				dstrs.push(dstr);
			});
		}
	});
	return dstrs;
}

function getDistribution(id)
{
	var dstr;
	$.ajax({	
		type: "GET",
		crossDomain: true,
		dataType: "xml",
		url: getKeretaRoot() + "Distribution/" + id,
		async: false,
		success: function(xml) 
		{
			dstr = getDstrObject(xml.documentElement);
		}
	});
	return dstr;
}


function persistApplication(app)
{
	var result = false;
	if (app == null) return result;
		
	var doc = $.parseXML("<application/>");
	var name = doc.createElement("name");
	var alias = doc.createElement("alias");
	var appType = doc.createElement("applicationType");
	var desc = doc.createElement("description");
	var author = doc.createElement("author");
	
	name.appendChild(doc.createTextNode(app.name));
	alias.appendChild(doc.createTextNode(app.alias));
	appType.appendChild(doc.createTextNode(app.appType));
	desc.appendChild(doc.createTextNode(app.description));
	author.appendChild(doc.createTextNode(app.author));
	
	doc.documentElement.appendChild(name);
	doc.documentElement.appendChild(alias);
	doc.documentElement.appendChild(appType);
	doc.documentElement.appendChild(desc);
	doc.documentElement.appendChild(author);
	
	data = (new XMLSerializer()).serializeToString(doc);
	
	$.ajax({	
		url: getKeretaRoot() + "Application/" + app.id,
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

function persistDistribution(dstr)
{
	var result = false;
	if (dstr == null) return result;
		
	var doc = $.parseXML("<distribution/>");
	var repr = doc.createElement("representation");
	var alias = doc.createElement("alias");
	var ns = doc.createElement("namespace");
	var lang = doc.createElement("language");
	var langV = doc.createElement("langVersion");
	var author = doc.createElement("author");
	
	repr.appendChild(doc.createCDATASection(dstr.representation));
	alias.appendChild(doc.createTextNode(dstr.alias));
	lang.appendChild(doc.createTextNode(dstr.lang));
	langV.appendChild(doc.createTextNode(dstr.langVersion));
	author.appendChild(doc.createTextNode(dstr.author));
	ns.appendChild(doc.createTextNode(dstr.namespace));
	
	doc.documentElement.appendChild(repr);
	doc.documentElement.appendChild(alias);
	doc.documentElement.appendChild(ns);
	doc.documentElement.appendChild(lang);
	doc.documentElement.appendChild(langV);
	doc.documentElement.appendChild(author);
	
	data = (new XMLSerializer()).serializeToString(doc);
	
	$.ajax({
		url: getKeretaRoot() + "Distribution/" + dstr.id,
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

function createApplication()
{
	var app;
	$.ajax({
		url: getKeretaRoot() + "Application/",
		crossDomain: true,
		type: "POST",
		dataType: "xml",
		async: false,
		success: function(xml) 
		{
			app = getAppObject(xml.documentElement);
		}
	});
	return app;
}

function createDistribution(appId)
{
	var dstr;
	$.ajax({
		url: getKeretaRoot() + "Application/" + appId + "/Distribution",
		crossDomain: true,
		type: "POST",
		dataType: "xml",
		async: false,
		success: function(xml) 
		{
			dstr = getDstrObject(xml.documentElement);
		}
	});
	return dstr;
} 

function deleteDistribution(dstr)
{
	var result = false;
	if (dstr == null) return result;
	
	$.ajax({	
		type: "DELETE",
		url: getKeretaRoot() + "Distribution/" + dstr.id,
		crossDomain: true,
		async: false,
		success: function() 
		{
			result = true;
		}
	});
	return result;
}

function deleteApplication(app)
{
	var result = false;
	if (app == null) return result;
	
	$.ajax({	
		type: "DELETE",
		url: getKeretaRoot() + "Application/" + app.id,
		crossDomain: true,
		async: false,
		success: function() 
		{
			result = true;
		}
	});
	return result;
}