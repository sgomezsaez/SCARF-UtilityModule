function kereta_ufGUI(appId, dstrId)
{
	var cnt = [];
	var apps = getApplications();

	var SelForm = kereta_createElement("div", "", "col-md-4 col-lg-3 form-group");
	var filter = kereta_createElement("h3", "", "");
	filter.textContent = "Filter";
	SelForm.appendChild(filter);
	cnt.push(SelForm);
	/* Adding the application selector */
	var fSelL = document.createElement("label");
	fSelL.setAttribute("for", "keretaSelectApp");
	fSelL.textContent = "Select Application:";
	SelForm.appendChild(fSelL);
	var fSel = kereta_createElement("select", "keretaSelectApp", "form-control");
	SelForm.appendChild(fSel);

	for (var i=0; i<apps.length; i++)
	{
		var opt = document.createElement("option");
		opt.setAttribute("value", apps[i].id);
		if (apps[i].id == appId) opt.setAttribute("selected", "true");

		opt.textContent = apps[i].name;
		fSel.appendChild(opt);
	}

	$(SelForm).append($.parseHTML("<label for='keretaSelectDstr'>Select Viable Distribution:</label></label><select id='keretaSelectDstr' class='form-control'></select>"));
	$(SelForm).append($.parseHTML("<label for='keretaSelectUf'>Select Utility Function:</label></label><select id='keretaSelectUf' class='form-control'></select>"));


	$(fSel).change(function() {
		kereta_loadDistributions($("#keretaSelectApp").val());
	});

	var UfForm = kereta_createElement("div", "keretaUf", "col-md-8 col-lg-9");
	cnt.push(UfForm);

	kereta_anchor("Utility Functions", cnt);
	kereta_loadDistributions($("#keretaSelectApp").val());
	if (dstrId != "") $("#keretaSelectDstr").val(dstrId);
	kereta_loadUtilityFunctions($("#keretaSelectDstr").val());
}

function kereta_calcGUI(ufId)
{
	var cnt = [];
	uf = getUtilityFunction(ufId);

	var SelForm = kereta_createElement("div", "", "col-md-4 col-lg-3");
	var headline = kereta_createElement("h3", "", "");
	headline.textContent = "Utility Function";
	SelForm.appendChild(headline);
	cnt.push(SelForm);

	if (uf != null)
	{
		dstr = getDistribution(uf.dstrId);
		app = getApplication(dstr.appId);
		$(SelForm).append($.parseHTML("<p style='line-height: 2em'>App. Type: <i>" + app.name + "</i><br>" + "Viab. Dstr: <i>" + dstr.alias + "</i><p>"));

		var CalcForm = kereta_createElement("div", "keretaCalc", "col-md-8 col-lg-9");
		cnt.push(CalcForm);


	}
	kereta_anchor("Calculation", cnt);
	if (uf != null) kereta_loadCalculation(uf.id);
}

function kereta_appGUI(appId)
{
	var cnt = [];
	var apps = getApplications();

	var SelForm = kereta_createElement("div", "", "col-md-4 col-lg-3 form-group");
	var filter = kereta_createElement("h3", "", "");
	filter.textContent = "Filter";
	SelForm.appendChild(filter);
	cnt.push(SelForm);
	/* Adding the application selector */
	var fSelL = document.createElement("label");
	fSelL.setAttribute("for", "keretaSelectApp");
	fSelL.textContent = "Select Application:";
	SelForm.appendChild(fSelL);
	var fSel = kereta_createElement("select", "keretaSelectApp", "form-control");
	SelForm.appendChild(fSel);

	var opt = document.createElement("option");
	opt.setAttribute("value", "");
	opt.textContent = "<new>";
	fSel.appendChild(opt);
	for (var i=0; i<apps.length; i++)
	{
		var opt = document.createElement("option");
		opt.setAttribute("value", apps[i].id);
		if (appId == apps[i].id) opt.setAttribute("selected", "true");
		opt.textContent = apps[i].name;
		fSel.appendChild(opt);
	}

	$(fSel).change(function() {
		kereta_loadApplication($("#keretaSelectApp").val());
	});

	var AppForm = kereta_createElement("div", "keretaApp", "col-md-8 col-lg-9");
	cnt.push(AppForm);

	kereta_anchor("Applications", cnt);
	kereta_loadApplication(appId);
}

function kereta_distributionGUI()
{
	var cnt = [];
	var appTypes = getApplicationTypes();

	var SelForm = kereta_createElement("div", "", "col-md-4 col-lg-3 form-group");
	var filter = kereta_createElement("h3", "", "");
	filter.textContent = "Filter";
	SelForm.appendChild(filter);
	cnt.push(SelForm);
	/* Adding the application type selector */
	var fSelL = document.createElement("label");
	fSelL.setAttribute("for", "keretaSelectAppType");
	fSelL.textContent = "Select Application Type:";
	SelForm.appendChild(fSelL);
	var fSel = kereta_createElement("select", "keretaSelectAppType", "form-control");
	SelForm.appendChild(fSel);

	for (var i=0; i<appTypes.length; i++)
	{
		var opt = document.createElement("option");
		opt.setAttribute("value", appTypes[i]);
		opt.textContent = appTypes[i];
		fSel.appendChild(opt);
	}

	$(fSel).change(function() {
		kereta_loadDistributionSelection($("#keretaSelectAppType").val());
	});

	var DstrForm = kereta_createElement("div", "keretaDstrSelection", "col-md-8 col-lg-9");
	cnt.push(DstrForm);

	kereta_anchor("Distributions", cnt);
	kereta_loadDistributionSelection($("#keretaSelectAppType").val());
}

function kereta_fctGUI()
{

	var fctTypes = getFunctionTypes();
	fctTypes.unshift("");

	var cnt = [];

	var SelForm = kereta_createElement("div", "", "col-md-4 col-lg-3 form-group");
	var filter = kereta_createElement("h3", "", "");
	filter.textContent = "Filter";
	SelForm.appendChild(filter);
	cnt.push(SelForm);
	var tSelL = document.createElement("label");
	tSelL.setAttribute("for", "keretaSelectFctType");
	tSelL.textContent = "Function Type Filter:";
	SelForm.appendChild(tSelL);
	var tSel = kereta_createElement("select", "keretaSelectFctType", "form-control");
	SelForm.appendChild(tSel);
	for (var i=0; i<fctTypes.length;i++)
	{
		var opt = document.createElement("option");
		opt.setAttribute("value", fctTypes[i]);
		opt.textContent = fctTypes[i];
		tSel.appendChild(opt);
	}
	$(tSel).change(function() {
		kereta_loadFunctions($("#keretaSelectFctType").val());
	});
	/* Adding the Function Selector */
	var fSelL = document.createElement("label");
	fSelL.setAttribute("for", "keretaSelectFct");
	fSelL.textContent = "Select Function:";
	SelForm.appendChild(fSelL);
	var fSel = kereta_createElement("select", "keretaSelectFct", "form-control");
	SelForm.appendChild(fSel);

	$(fSel).change(function() {
		kereta_loadFunction($("#keretaSelectFct").val());
	});

	var FctForm = kereta_createElement("div", "keretaFct", "col-md-8 col-lg-9");
	cnt.push(FctForm);

	kereta_anchor("Functions", cnt);
	kereta_loadFunctions("");
}

function kereta_loadDistributionSelection(appType)
{
	var cnt = $("#keretaDstrSelection");
	cnt.empty();
	cnt.append($.parseHTML("<h3>Distributions</h3><h4>Application Type: " + appType + "</h4>"));
	var tab = $.parseHTML("<table class='dstrTable'><tr><th>alias</th><th></th><th></th><th></th><th>application</th></tr></table>");
	cnt.append(tab);

	var dstrs = getDistributionsByAppType(appType);

	for (var i=0; i<dstrs.length; i++)
	{
		var dstr = dstrs[i];
		var app = getApplication(dstr.appId);

		var tr = "<tr id='dstr-" + dstr.id + "'>" +
			"<td>" + dstr.alias + "</td>" +
			"<td>" + dstr.lang + " " + dstr.langVersion + "</td>";
		if (dstr.lang == "Tosca") tr += "<td><span title='watch XML' onClick='kereta_watchDistribution(\"" + dstr.id + "\", \"XML\");' class='glyphicon glyphicon-file'></span></td>" +
			"<td><span title='watch Topology' onClick='kereta_watchDistribution(\"" + dstr.id + "\", \"Topology\");' class='glyphicon glyphicon-eye-open'></span></td>";
		else tr += "<td></td><td></td>";
		tr += "<td><span onClick='kereta_appGUI(\"" + app.id + "\")'>" + app.name + "</span></td>" +
			"</tr>";

		$(tab).append($.parseHTML(tr));
	}
}

function kereta_assignDialog()
{
	var rId = $(".informationContainer .name").text().trim();
	var nsp = $(".informationContainer .namespace").text().trim();

	var contentNode = kereta_createElement("div");
	$(contentNode).append($.parseHTML("<h3>Assign Viable Distribution to Application</h3>"));

	contentNode.setAttribute("style", "margin-top: 1em;");
	var formNode = $.parseHTML("<form id='assignForm' role=form></form>");
	var fg1Node =$.parseHTML("<div class='form-group'></div>");
	$(formNode).append(fg1Node);
	var nameN = $.parseHTML("<label for='resourceId'>Name:</label><input readonly name='resourceId' id='resourceId' value='" + rId + "' class='form-control' />");
	var nspN = $.parseHTML("<label for='namespace'>Namespace:</label><input readonly name='namespace' id='namespace' value='" + nsp + "' class='form-control' />");
	$(fg1Node).append(nameN);
	$(fg1Node).append(nspN);
	var fg2Node =$.parseHTML("<div class='form-group'></div>");
	$(formNode).append(fg2Node);
	var selN = $.parseHTML("<select id='keretaAppId' class='form-control' value=''></select>");
	var selT = $.parseHTML("<select id='keretaAppType' class='form-control' value=''></select>");
	$(fg2Node).append($.parseHTML("<label for='keretaAppId'>Application:</label>"));
	$(fg2Node).append(selN);
	$(fg2Node).append($.parseHTML("<label for='keretaAppType'>Application Type (only required when creating a new application):</label>"));
	$(fg2Node).append(selT);

	$(selN).append($.parseHTML("<option value=''>&lt;new&gt;</option>"));
	var apps = getApplications();
	for (var i=0; i<apps.length; i++)
	{
		$(selN).append($.parseHTML("<option value='" + apps[i].id + "'>" + apps[i].name + "</option>"));

	}
	var appTypes = getApplicationTypes();
	appTypes.unshift("<new>");
	for (var i=0; i<appTypes.length; i++)
	{
		var opt = document.createElement("option");
		opt.setAttribute("value", appTypes[i]);
		opt.textContent = appTypes[i];
		$(selT).append(opt);
	}


	$(formNode).append("<div class='form-group'><button type='button' onClick='appendDistribution();' class='btn btn-success'>assign</button></div>");

	$(contentNode).append(formNode);
	kereta_createSmallWrapper(contentNode);
}

function appendDistribution()
{
	var form = $("#assignForm");
	var name = $(form).find("#resourceId").val();
	var nsp = $(form).find("#namespace").val();
	var appId = $(form).find("#keretaAppId").val();
	var appType = $(form).find("#keretaAppType").val();

	if (appId == "")
	{

		var appName = prompt("Please enter the new Application's Name:", "appName");
		if (appName == "" || appName == null)
		{
			alert("Please insert a name!");
			return;
		}
		else
		{
			if (appType == "<new>")
			{
				var appType = prompt("Please enter the new Application Type:", "appType");
				if (appType == "" || appType == null)
				{
					alert("Please insert a type!");
					return;
				}
				else createApplicationType(appType);
			}

			var app = createApplication();
			app.name = appName;
			app.appType = appType;
			console.log(app.appType);
			persistApplication(app);
			appId = app.id;
		}
	}

	var dstr = createDistribution(appId);
	dstr.lang = "Tosca";
	dstr.langVersion = "1.0";
	dstr.alias = name;
	dstr.namespace = nsp;

	var xml;
	$.ajax({
		type: "GET",
		dataType: "xml",
		crossDomain: true,
		url: "?definitions",
		async: false,
		success: function(rsps)
		{
			dstr.representation = (new XMLSerializer()).serializeToString(rsps);
		}
	});
	persistDistribution(dstr);
	kereta_GUI(getKeretaRoot());
	kereta_appGUI("");
	$("#keretaSelectApp").val(appId);
	kereta_loadApplication(appId);
}



function kereta_loadCalculation(ufId)
{
	uf = getUtilityFunction(ufId);
	if (uf != null)
	{
		$("#keretaCalc").empty();

		var cnt = kereta_createElement("div", "keretaCalc", "col-lg-12");
		var hl = kereta_createElement("h3", "", "");
		cnt.appendChild(hl);
		hl.textContent = uf.alias;

		for (var i=0; i<uf.subFct.length; i++)
		{
			console.log
			var subFct = uf.subFct[i];
			var name = "";
			var typ = "";
			var parms = [];

			if (subFct.fctId.indexOf("nefolog$") >= 0)
			{
				var conf = subFct.fctId.split('$')[1];
				name = "Nefolog, conf.: " + conf;
				parms = getNefologParameter(subFct);
				typ = "cost";
			}
			else
			{
				var fct = getFunction(subFct.fctId);
				parms = fct.parameter;
				name = fct.alias;
				typ = fct.fctType;
			}
			var block = kereta_createElement("p", "", "");
			block.setAttribute("style", "margin-top: 1.5em;");
			var hl = kereta_createElement("h4", "", "");
			hl.textContent = name + " (" + typ + ")";
			block.appendChild(hl);

			var tab = $.parseHTML("<table class='calc' ufId=" + uf.id + " nbr='" + subFct.number + "'></table>");
			for (var j=0; j<parms.length; j++)
			{
				var parm = parms[j];
				var desc = "";
				var def = "";
				if (parm.description) desc = parm.description;
				if (parm.def) def = parm.def;

				var tr = $.parseHTML("<tr parameter='" + parm.name + "'><td>" + parm.name + "</td><td><input type='text' class='form-control' name='value' value='" + def + "' style='width:120px;' /></td><td>" + desc + " (" + parm.dataType + ")</td></tr>");
				$(tab).append(tr);
			}

			$(block).append(tab);
			$(cnt).append(block);
		}
		$(cnt).append($.parseHTML("<button role='button' style='margin-bottom:1em;' class='btn btn-success' onClick='kereta_evaluate();'>evaluate</button>"));

		$("#keretaCalc").append(cnt);
	}
}

function kereta_evaluate()
{
	var calc = $("#keretaCalc");
	var ufId = "";

	$(calc).find(".result").each (function() {
		$(this).remove();
	});

	$(calc).find("table.calc").each (function() {

		ufId = $(this).attr("ufId");
		var nbr = $(this).attr("nbr");
		var subFct = getSubFunction(ufId, nbr);
		var query = "key=myKey";

		$(this).find("tr").each (function() {
			var parm = $(this).attr("parameter");
			var val = $(this).find("input").val();
			if (val != "") query = query + "&" + parm + "=" + val;

		});

		assignSubFunction(subFct, query);
		var result = (calcSubFunction(subFct, "myKey"));
		$(this).append($.parseHTML("<b class='result'>result: " + result + "</b>"));
	});

	uf = getUtilityFunction(ufId);
	if (uf != null)
	{
		var result = calcUtilityFunction(uf, "myKey");
		var clr = "black";
		if (result < 0) clr = "red";
		$(calc).append("<h3 class='result' style='color:" + clr + ";'>Utility: " + result + "</h3>");
	}

}

function kereta_loadDistributions(appId)
{
	$("#keretaSelectDstr").empty();
	var dstrs = getDistributions(appId);

	var app = null;
	if (appId != "") app = getApplication(appId);
	if (app != null)
	{
		var dstrs = getDistributions(appId);
		for (var i=0; i<dstrs.length; i++)
		{
			var dstr = dstrs[i];
			$("#keretaSelectDstr").append($.parseHTML("<option value='" + dstr.id + "'>" + dstr.alias + "</option>"));
		}
		$("#keretaSelectDstr").change(function() {
			kereta_loadUtilityFunctions($("#keretaSelectDstr").val());
		});
	}
	//kereta_loadUtilityFunctions($("#keretaSelectDstr").val());
}

function kereta_loadUtilityFunctions(dstrId)
{
	$("#keretaSelectUf").empty();
	var opt = document.createElement("option");
	opt.textContent = "<new>";
	opt.setAttribute("value", "");
	$("#keretaSelectUf").append(opt);
	var ufs = getUtilityFunctions(dstrId);
	for (var i=0; i<ufs.length; i++)
	{
		var uf = ufs[i];
		$("#keretaSelectUf").append($.parseHTML("<option value='" + uf.id + "'>" + uf.alias + "</option>"));
	}
	$("#keretaSelectUf").change(function() {
		kereta_loadUtilityFunction($("#keretaSelectUf").val());
	});
	kereta_loadUtilityFunction($("#keretaSelectUf").val());
}

function kereta_loadUtilityFunction(ufId)
{
	$("#keretaUf").empty();
	var uf = null;
	if (ufId != "") uf = getUtilityFunction(ufId);

	var form = $.parseHTML("<div class='col-lg-12'>" +
			"<h3>Utility Function</h3>" +
			"<input type='hidden' value='' id='ufId' />" +
			"<div class='form-group'>" +
			"<label for='fctAlias'>Alias:</label><input id='ufAlias' class='form-control' />" +
			"</div>" +
			"<div class='form-group'>" +
			"<button class='btn btn-info' role='button' id='ufPersist' onClick='kereta_processUtilityFunction();'></button>" +
			"<button style='margin-left:.5em;' class='btn btn-warning' role='button' id='ufDelete' onClick='kereta_deleteUtilityFunction();'>delete</button>" +
			"</div>" +
			"</div>");
	if (uf != null)
	{
		$(form).find("#ufPersist").text("update");
		$(form).find("#ufAlias").val(uf.alias);
		$(form).find("#ufId").val(uf.id);
	}
	else
	{
		$(form).find("#ufPersist").text("create");
		$(form).find("#ufDelete").remove();
	}

	$("#keretaUf").append(form);



	/* Add parameter handling */
	var formS = $.parseHTML("<div class='col-lg-12'><h3>Utility Function Subfunctions</h3><div>");

	var tab = kereta_createElement("table", "", "ufSubfunctions");
	$(formS).append(tab);
	var th = $.parseHTML("<tr><th>function</th><th>nefolog conf.ID</th><th></th><th></th><tr>");
	$(tab).append(th);

	var fcts = getFunctions();
	var max = 1;

	if (uf != null && uf.subFct != null)
	{
		for (var i=0; i<uf.subFct.length;i++)
		{
			var subFct = uf.subFct[i];
			if (parseInt(subFct.number) >= max) max = parseInt(subFct.number) + 1;
			var selNefolog = "";
			var nefId = "";
			if (subFct.fctId.indexOf("nefolog$") >= 0)
			{
				selNefolog = "selected='true'";
				nefId = subFct.fctId.split('$')[1];
			}
			var td = "<tr id='subFct-" + subFct.number + "' nbr='" + subFct.number + "'>" +
					 "<td><select class='form-control' name='fctId'><option val=''></option><option value='Nefolog' "+ selNefolog + ">Nefolog</option></select></td>" +
					 "<td><input class='form-control' type='text' name='nefologConfig' value='" + nefId + "' style='width:110px;' /></td>" +
					 "<td><span class='glyphicon glyphicon-plus' onClick='kereta_processSubfunction(\"subFct-" + subFct.number + "\");'></span></td>" +
					 "<td><span class='glyphicon glyphicon-remove' onClick='kereta_deleteSubfunction(\"" + subFct.ufId + "\", " + subFct.number + ");'></span></td>" +
					 "</tr>";
			var tdO = $.parseHTML(td);
			$(tab).append(tdO);

			for (var j=0; j<fcts.length; j++)
			{
				if (fcts[j].fctType == "cost" || fcts[j].fctType == "revenue")
				{
					var opt = document.createElement("option");
					opt.setAttribute("value", fcts[j].id);
					if (fcts[j].id == subFct.fctId) opt.setAttribute("selected", "true");
					opt.textContent = fcts[j].alias + " (" + fcts[j].fctType + ")";
					$(tdO).find("select[name='fctId']").append(opt);
				}
			}
		}
	}
	var trNew = $.parseHTML("" +
			"<tr id='subfunction-create' nbr='" + max + "'>" +
			"<td><select class='form-control' name='fctId' value=''><option selected='true' value=''></option><option value='Nefolog'>Nefolog</option></select></td>" +
			"<td><input class='form-control' type='text' name='nefologConfig' value='' style='width:110px;' /></td>" +
			"<td><span class='glyphicon glyphicon-plus' onClick='kereta_processSubfunction(\"subfunction-create\");'></span></td><td></td>" +
			"</tr>");

	for (var j=0; j<fcts.length; j++)
	{
		if (fcts[j].fctType == "cost" || fcts[j].fctType == "revenue")
		{
			var opt = document.createElement("option");
			opt.setAttribute("value", fcts[j].id);
			opt.textContent = fcts[j].alias + " (" + fcts[j].fctType + ")";
			$(trNew).find("select[name='fctId']").append(opt);
		}
	}
	$(tab).append(trNew);
	if (ufId != "") {
		$("#keretaUf").append(formS);
		var formC = $.parseHTML("<br><div class='col-lg-12'><button class='btn btn-success' onClick='kereta_calcGUI(\"" + ufId + "\");'>calculate</button><div>");
		$("#keretaUf").append(formC);
	}

}

function kereta_processSubfunction(trId)
{
	var tr = $("tr#" + trId);
	var nbr = $(tr).attr("nbr");
	var ufId = $("#keretaSelectUf").val();
	var fctId = $(tr).find("select[name='fctId']").val();
	var nefConf = $(tr).find("input[name='nefologConfig']").val();
	subFct = null;
	if (trId == "subfunction-create")
	{
		subFct = createSubFunction(ufId, nbr);
	}
	else
	{
		subFct = getSubFunction(ufId, nbr);
	}
	if (subFct != null)
	{
		if (fctId == "Nefolog") fctId = "nefolog$" + nefConf;
		subFct.fctId = fctId;
		if (persistSubFunction(subFct)) kereta_loadUtilityFunction(ufId);
		else alert("something went wrong!");
	}
}

function kereta_deleteSubfunction(ufId, sfNbr)
{
	console.log(ufId + " " + sfNbr);
	var subFct = getSubFunction(ufId, sfNbr);
	var dr = confirm("Delete subfunction?");
	if (dr == true)
	{
		if (subFct != null)
		{
			if (deleteSubFunction(subFct))
			{
				kereta_loadUtilityFunction(ufId);
			}
			else alert("Something went wrong!");
		}
	}

}

function kereta_processUtilityFunction()
{
	var uf = null;
	var dstrId = $("#keretaSelectDstr").val();
	var ufId = $("#ufId").val();
	var alias = $("#ufAlias").val();

	console.log(ufId);

	if (ufId == "") {
		uf = createUtilityFunction(dstrId);
	}
	else
	{
		uf = getUtilityFunction(ufId);
	}

	if (uf != null)
	{
		uf.alias = alias;
		if (persistUtilityFunction(uf))
		{
			kereta_loadUtilityFunctions(dstrId)
			$("#keretaSelectUf").val(uf.id);
			kereta_loadUtilityFunction(uf.id)
		}
	}
}

function kereta_deleteUtilityFunction()
{
	var uf = null;
	var dstrId = $("#keretaSelectDstr").val();
	var ufId = $("#ufId").val();
	if (ufId != "") uf = getUtilityFunction(ufId);

	if (deleteUtilityFunction(uf))
	{
		kereta_loadUtilityFunctions(dstrId)
		$("#keretaSelectUf").val("");
		kereta_loadUtilityFunction("")
	}
	else alert("Something went wrong!");
}

function kereta_loadFunctions(fctType)
{
	$("#keretaSelectFct").empty();
	var fcts = selectFunctions(fctType);
	var opt = document.createElement("option");
	opt.setAttribute("value", "");
	opt.textContent = "<new>";
	$("#keretaSelectFct").append(opt);
	for (var i=0; i<fcts.length; i++)
	{
		var opt = document.createElement("option");
		opt.setAttribute("value", fcts[i].id);
		opt.textContent = fcts[i].alias + " (" + fcts[i].fctType + ")";
		$("#keretaSelectFct").append(opt);
	}
	kereta_loadFunction("");
}

function kereta_loadFunction(fctId)
{
	$("#keretaFct").empty();
	var fct = null;
	if (fctId != "") fct = getFunction(fctId);

	var form = $.parseHTML("<div class='col-lg-12'>" +
			"<h3>Function</h3>" +
			"<input type='hidden' value='' id='fctId' />" +
			"<div class='form-group'>" +
			"<label for='fctType'>Function Type:</label><select id='fctType' class='form-control'></select>" +
			"<label for='fctAlias'>Alias:</label><input id='fctAlias' class='form-control' />" +
			"</div>" +
			"<div class='form-group'>" +
			"<label for='fctFormula'>Formula:</label><textarea id='fctFormula' class='form-control'></textarea>" +
			"<label for='fctDesc'>Description:</label><textarea id='fctDesc' class='form-control'></textarea>" +
			"</div>" +
			"<div class='form-group'>" +
			"<button class='btn btn-info' role='button' id='fctPersist' onClick='kereta_processFunction();'></button>" +
			"<button style='margin-left:.5em;' class='btn btn-warning' role='button' id='fctDelete' onClick='kereta_deleteFunction();'>delete</button>" +
			"</div>" +
			"</div>");

	var fctTypes = getFunctionTypes();
	fctTypes.unshift("");
	for (var i=0; i<fctTypes.length; i++)
	{
		var opt = document.createElement("option");
		opt.setAttribute("value", fctTypes[i]);
		opt.textContent = fctTypes[i];
		$(form).find("#fctType").append(opt);
	}

	if (fct == null)
	{
		$(form).find("#fctPersist").text("create");
		$(form).find("#fctDelete").remove();
	}
	else
	{
		$(form).find("#fctPersist").text("update");
		$(form).find("#fctType").val(fct.fctType);
		$(form).find("#fctId").val(fct.id);
		$(form).find("#fctAlias").val(fct.alias);
		$(form).find("#fctFormula").text(fct.formula);
		$(form).find("#fctDesc").text(fct.description);

	}
	$("#keretaFct").append(form);

	/* Add parameter handling */
	var formP = $.parseHTML("<div class='col-lg-12'><h3>Parameter</h3><div>");

	var tab = kereta_createElement("table", "", "fctParameter");
	$(formP).append(tab);
	var th = $.parseHTML("<tr><th>name</th><th>description</th><th>dataType</th><th></th><th></th><tr>");
	$(tab).append(th);

	var dTypes = getDataTypes();
	dTypes.unshift("");

	if (fct != null && fct.parameter != null)
	{
		for (var i=0; i<fct.parameter.length;i++)
		{
			var parm = fct.parameter[i];
			var td = "<tr id='parm-" + parm.name + "'>" +
					 "<td>" + parm.name + "</td>" +
					 "<td><input class='form-control' type='text' name='desc' value='" + parm.description + "' /></td>" +
					 "</tr>";
			var tdO = $.parseHTML(td);

			var sel = $.parseHTML("<select class='form-control'></select>");
			for (var j=0; j<dTypes.length; j++)
			{
				var opt = document.createElement("option");
				opt.setAttribute("value", dTypes[j]);
				if (parm.dataType == dTypes[j]) opt.setAttribute("selected", "true");
				opt.textContent = dTypes[j];
				$(sel).append(opt);
			}
			var tdS = $.parseHTML("<td></td>");
			var tdUp = $.parseHTML("<td><span class='glyphicon glyphicon-plus' onClick='kereta_processParameter(\"" + parm.fctId + "\", \"" + parm.name + "\");'></span></td>");
			var tdDel = $.parseHTML("<td><span class='glyphicon glyphicon-remove' onClick='kereta_deleteParameter(\"" + parm.fctId + "\", \"" + parm.name + "\");'></span></td>");

			$(tdS).append(sel);
			$(tdO).append(tdS);
			$(tdO).append(tdUp);
			$(tdO).append(tdDel);
			$(tab).append(tdO);
		}
	}
	var trNew = $.parseHTML("<tr id='parm-create'>" +
			"<td><input class='form-control' type='text' name='name' value='' style='width:80px; text-align:center;' /></td>" +
			"<td></td>" +
			"<td></td>" +
			"<td><span class='glyphicon glyphicon-plus' onClick='kereta_processParameter(\"" + fctId + "\", \"create\");'></span></td><td></td>" +
			"</tr>");
	$(tab).append(trNew);

	if (fctId != "") $("#keretaFct").append(formP);
}

function kereta_loadApplication(appId)
{
	$("#keretaApp").empty();
	var app = null;
	if (appId != "") app = getApplication(appId);

	var form = $.parseHTML("<div class='col-lg-12'>" +
			"<h3>Application</h3>" +
			"<input type='hidden' value='' id='appId' />" +
			"<div class='form-group'>" +
			"<label for='appName'>Name:</label><input type='text' id='appName' class='form-control' />" +
			"<label for='appType'>Application Type: <span class='glyphicon glyphicon-remove' onClick='kereta_deleteApplicationType()'></span></label><select id='appType' class='form-control'></select>" +
			"<!--<label for='appAlias'>Alias:</label><input id='appAlias' class='form-control' />-->" +
			"</div>" +
			"<div class='form-group'>" +
			"<label for='appDesc'>Description:</label><textarea id='appDesc' class='form-control'></textarea>" +
			"</div>" +
			"<div class='form-group'>" +
			"<button class='btn btn-info' role='button' id='appPersist' onClick='kereta_processApplication();'></button>" +
			"<button style='margin-left:.5em;' class='btn btn-warning' role='button' id='appDelete' onClick='kereta_deleteApplication();'>delete</button>" +
			"</div>" +
			"</div>");

	var appTypes = getApplicationTypes();
	appTypes.unshift("<new>");
	for (var i=0; i<appTypes.length; i++)
	{
		var opt = document.createElement("option");
		opt.setAttribute("value", appTypes[i]);
		opt.textContent = appTypes[i];
		$(form).find("#appType").append(opt);
	}

	if (app == null)
	{
		$(form).find("#appPersist").text("create");
		$(form).find("#appDelete").remove();
	}
	else
	{
		$(form).find("#appPersist").text("update");
		$(form).find("#appType").val(app.appType);
		$(form).find("#appId").val(app.id);
		$(form).find("#appAlias").val(app.alias);
		$(form).find("#appName").val(app.name);
		$(form).find("#appDesc").text(app.description);

	}
	$("#keretaApp").append(form);

	/* Add viable distributions */
	if (appId != "")
	{
		var dstrs = getDistributions(appId);

		var formD = $.parseHTML("<div class='col-lg-12'>" +
				"<h3>Viable Distributions</h3>" +
				"<table class='dstrTable'>" +
				"<tr> <th>alias</th> <th></th> <th></th> <th></th></tr>" +
				"</table>" +
				"</div>");

		for (var i=0; i<dstrs.length; i++)
		{
			var dstr = dstrs[i];
			var tr = "<tr id='dstr-" + dstr.id + "'>" +
					"<td><input class='form-control' type='text' name='alias' value='" + dstr.alias + "' /></td>" +
					"<td><span class='glyphicon glyphicon-ok' onClick='kereta_processDistribution(\"" + dstr.id + "\")'></span></td>" +
					"<td><span class='glyphicon glyphicon-remove' onClick='kereta_deleteDistribution(\"" + dstr.id + "\")'></span></td>" +
					"<td>" + dstr.lang + " " + dstr.langVersion + "</td>";
			if (dstr.lang == "Tosca") tr += "<td><span title='watch XML' onClick='kereta_watchDistribution(\"" + dstr.id + "\", \"XML\");' class='glyphicon glyphicon-file'></span></td>" +
					"<td><span title='watch Topology' onClick='kereta_watchDistribution(\"" + dstr.id + "\", \"Topology\");' class='glyphicon glyphicon-eye-open'></span></td>";
			else tr += "<td></td><td></td>";
			tr += "<td><span onClick='kereta_ufGUI(\"" + dstr.appId + "\", \"" + dstr.id + "\")'>u.fct.</span></td>" +
					"</tr>";

			var trObj = $.parseHTML(tr);
			$(formD).find(".dstrTable").append(trObj);
			if (dstr.lang == "Tosca")
			{
				var tmp = $(trObj).find("input");
				tmp.attr("readonly", "");
			}
		}

		var trN = "<tr id='dstr-New'>" +
		"<td><input class='form-control' type='text' name='alias' value='' /><input type='hidden' name='appId' value='" + appId + "' /></td>" +
		"<td><span class='glyphicon glyphicon-plus' onClick='kereta_processDistribution(\"\")'></span></td><td></td>" +
		"<td></td>" +
		"</tr>";
		$(formD).find(".dstrTable").append($.parseHTML(trN));

		$("#keretaApp").append(formD);
	}

}

function kereta_watchDistribution(dstrId, object)
{
	dstr = getDistribution(dstrId);
	if (dstr != null)
	{
		if (object == "XML")
		{
			var str = dstr.representation;

		 	var win = window.open("", "_blank");
		 	win.focus();
		 	$(win.document).find("body").append($.parseHTML("<h3>" + dstr.alias + "</h3><h4>" + dstr.namespace + "</h4><textarea readonly style='width: 100%; height: 100%; font-family: monospace;'>" + str + "</textarea>"));
		}
		else if (object == "Topology")
		{
			var url = "http://" + window.location.hostname + ":" + window.location.port + "/winery/servicetemplates/" + kereta_replaceURI(dstr.namespace) + "/" + dstr.alias + "/topologytemplate?view";
			window.open(url, "_blank");
		}
	}
}

function kereta_processDistribution(dstrId)
{
	var dstr = null;
	var tr = null
	;
	if (dstrId != "")
	{
		tr = $("#dstr-" + dstrId);
		dstr = getDistribution(dstrId);
	}
	else
	{
		tr = $("#dstr-New");
		var appId = $(tr).find("input[name='appId']").val();
		dstr = createDistribution(appId);
	}

	if (dstr != null)
	{
		var alias = $(tr).find("input[name='alias']").val();
		dstr.alias = alias;
		if (persistDistribution(dstr)) kereta_loadApplication(dstr.appId);
		else  alert("Something went wrong!");
	}
}

function kereta_deleteDistribution(dstrId)
{
	var dstr = getDistribution(dstrId);
	var dr = confirm("Delete viable distribution?");
	if (dr == true)
	{
		if (dstr != null)
		{
			if (deleteDistribution(dstr))
			{
				kereta_loadApplication(dstr.appId);
			}
			else alert("Something went wrong!");
		}
	}


}

function kereta_processParameter(fctId, name)
{
	var tr = $("#parm-" + name);
	var desc = $(tr).find("input[name='desc']").val();
	var type = $(tr).find("select").val();

	var parm = null;
	if (name == "create") {
		var nm = $(tr).find("input[name='name']").val();
		parm = createParameter(fctId, nm);
	}
	else parm = getParameter(fctId, name);
	parm.description = desc;
	parm.dataType = type;
	if (persistParameter(parm))
	{
		kereta_loadFunction(parm.fctId);
	}
	else alert("Something went wrong!");
}

function kereta_deleteParameter(fctId, name)
{
	var dr = confirm("Delete parameter " + name + "?");
	if (dr == true) {
		var parm = getParameter(fctId, name);
		if (deleteParameter(parm))
		{
			kereta_loadFunction(parm.fctId);
		}
		else alert("Something went wrong!");
	}
}

function kereta_processApplication()
{
	var id = $("#appId").val();
	var appType = $("#appType").val();
	//var alias = $("#appAlias").val();
	var name = $("#appName").val();
	var desc = $("#appDesc").val();

	var app = null;
	if (id == "") app = createApplication();
	else app = getApplication(id);

	if (app != null)
	{
		if (appType == "<new>")
		{
			appType = prompt("Please enter a new Application Type", "Application Type");
			if (appType == "" || appType == null)
			{
				alert("Please insert a new Application Type");
			}
			else createApplicationType(appType);
		}
		app.appType = appType;
		//app.alias = alias;
		app.name = name;
		app.description = desc;

		if (persistApplication(app))
		{
			kereta_appGUI("");
			kereta_loadApplication(app.id);
			$("#keretaSelectApp").val(app.id);
		}
		else alert("Something went wrong!");
	}
}

function kereta_deleteFunction()
{
	var id = $("#fctId").val();
	fct = getFunction(id);

	var dr = confirm("Delete function?");
	if (dr == true)
	{
		if (fct != null)
		{
			if (deleteFunction(fct))
			{
				kereta_loadFunctions($("#keretaSelectFctType").val());
			}
			else alert("Something went wrong!");
		}
	}
}

function kereta_deleteApplication()
{
	var id = $("#appId").val();
	app = getApplication(id);
	var dr = confirm("Delete application type " + app.name + "?");
	if (dr == true)
	{
		if (app != null)
		{
			if (deleteApplication(app))
			{

				kereta_appGUI("");
			}
			else alert("Something went wrong!");
		}
	}
}

function kereta_deleteApplicationType()
{
	var appType = $("#appType").val();
	var appId = $("#appId").val();
	if (appType == "<new>") return;
	var dr = confirm("Delete application type " + appType + "?");
	if (dr == true)
	{

		if (deleteApplicationType(appType))
		{
			kereta_loadApplication(appId);
		}
		else alert("Something went wrong!");
	}
}

function kereta_processFunction()
{
	var id = $("#fctId").val();
	var fctType = $("#fctType").val();
	var alias = $("#fctAlias").val();
	var formula = $("#fctFormula").val();
	var desc = $("#fctDesc").val();

	var fct = null;
	if (id == "") fct = createFunction();
	else fct = getFunction(id);

	if (fct != null)
	{
		fct.fctType = fctType;
		fct.alias = alias;
		fct.formula = formula;
		fct.description = desc;

		if (persistFunction(fct))
		{
			kereta_loadFunctions($("#keretaSelectFctType").val());
			$("#keretaSelectFct").val(fct.id);
			kereta_loadFunction(fct.id);
		}
		else alert("Something went wrong!");
	}
}

function kereta_manageApplications()
{
	var cntNode = kereta_createElement("div", "", "col-lg-12");
	var sel = document.createElement("select");
	sel.setAttribute("id", "keretaAppSelector");
	var cntApp = document.createElement("div");
	cntApp.setAttribute("id", "keretaAppContainer");
	cntNode.appendChild(sel);
	cntNode.appendChild(cntApp);
	kereta_createWrapper(cntNode);


	/*$("select[name='exAppTypes']").empty();
	var appTypes = getApplicationTypes();
	for (var i=0; i<appTypes.length;i++)
	{
		var opt = document.createElement("option");
		opt.setAttribute("value", appTypes[i]);
		opt.textContent = appTypes[i];
		$("select[name='exAppTypes']").append(opt);
	}

	$("#AppsContainer").empty();

	var sel = document.createElement("select");
	sel.setAttribute("id", "AppSelector");
	$("#AppsContainer").append(sel);
	*/
	var apps = getApplications();
	var optNull = document.createElement("option");
	optNull.setAttribute("value", "");
	optNull.textContent = "<new>";
	optNull.setAttribute("selected", true);
	sel.appendChild(optNull);

	for (var i=0; i<apps.length; i++)
	{
		var app = apps[i];
		var opt = document.createElement("option");
		opt.setAttribute("value", app.id);
		opt.textContent = app.name;
		sel.appendChild(opt);
	}

	$("#keretaAppSelector").on("change", function() {
		var id = $("#keretaAppSelector").val();
		initApp(id);
	});

	initApp("");
}

function initApp(id) {

	var appTypes = getApplicationTypes();
	appTypes.unshift("");

	$("#keretaAppContainer").empty();

	var app = null;
	if (id != null && id != "") app = getApplication(id);

	var fApp = document.createElement("form");
	fApp.setAttribute("class", "appForm");
	$("#keretaAppContainer").append(fApp);
	var clear = document.createElement("div");
	clear.setAttribute("class", "clear");
	$("#keretaAppContainer").append(clear);

	var hId = document.createElement("input");
	hId.setAttribute("type", "hidden");
	hId.setAttribute("name", "id");
	if (app != null) hId.setAttribute("value", app.id);
	fApp.appendChild(hId);

	var aName = document.createElement("input");
	aName.setAttribute("type", "text");
	if (app != null) aName.setAttribute("value", app.name);
	aName.setAttribute("name", "name");
	fApp.appendChild(kereta_formItem("app type", aName));

	var aAlias = document.createElement("input");
	aAlias.setAttribute("type", "text");
	if (app != null) aAlias.setAttribute("value", app.alias);
	aAlias.setAttribute("name", "alias");
	fApp.appendChild(kereta_formItem("alias", aAlias));

	var aType = document.createElement("select");
	aType.setAttribute("name", "appType");
	var found = false;
	for (var i=0; i<appTypes.length;i++)
	{
		var opt = appTypes[i];
		var oType = document.createElement("option");
		oType.setAttribute("value", opt);
		oType.textContent = opt;
		aType.appendChild(oType);
		if (app != null) {
			if (opt==app.appType) {
				oType.setAttribute("selected", "true");
				found = true;
			}
		}
	}
	if (!found && app != null) {
		var oType = document.createElement("option");
		aType.appendChild(oType);
		oType.setAttribute("value", app.appType);
		oType.textContent = app.appType;
		oType.setAttribute("selected", "true");
	}
	aType.setAttribute("type", "text");
	if (app != null) aType.setAttribute("value", app.appType);
	fApp.appendChild(kereta_formItem("app category", aType));

	var aAuthor = document.createElement("input");
	aAuthor.setAttribute("type", "text");
	if (app != null) aAuthor.setAttribute("value", app.author);
	aAuthor.setAttribute("name", "author");
	fApp.appendChild(kereta_formItem("author", aAuthor));

	var aDesc = document.createElement("textarea");
	if (app != null) aDesc.textContent = app.description;
	aDesc.setAttribute("name", "description");
	fApp.appendChild(kereta_formItem("description", aDesc));
	/*
	var bSend = document.createElement("button");
	bSend.setAttribute("type", "button");
	bSend.setAttribute("id", "AppUpdate");
	if (app != null) bSend.textContent = "update";
	else bSend.textContent = "create";
	p4.appendChild(bSend);

	if (app != null)
	{
		var bKill = document.createElement("button");
		bKill.setAttribute("type", "button");
		bKill.setAttribute("id", "AppDelete");
		bKill.textContent = "delete";
		p4.appendChild(bKill);
		$("#AppDelete").on("click", function() {

			if(confirm("Are you sure to delete?"))
			{
				if (deleteApplication(app)) init();
				else  alert("Something went wrong!");
			}
		});
	}

	if (app != null) $("#AppUpdate").on("click", function() { processApp(false); });
	else $("#AppUpdate").on("click", function() { processApp(true); });
	*/
}

