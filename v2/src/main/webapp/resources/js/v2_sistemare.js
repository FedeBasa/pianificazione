	//js non di Riccardo
	$("#aggiungi").click(function(){
		var path="/v2/record/insert";
		$("#v2Form").attr("action",path);
		$("#v2Form").submit();
	});
	$("#delete").click(function(){
		if (confirm('Stai per eliminare un record. Sei sicuro di voler continuare?')) {				
			var path = "/v2/record/delete";
			$("#v2Form").attr("action",path);
			$("#v2Form").submit();
		}
	});
	$("#aggiorna").click(function(){
		var path = "/v2/record/update";
		$("#v2Form").attr("action",path);
		$("#v2Form").submit();
	});
	
	var options = {
		url : function(phrase) {
			return "/v2/autocomplete/risorse";
		},
		getValue : "nameSurname",
		list : {
			onSelectItemEvent: function() {
				var value = $("#employeeDesc").getSelectedItemData().badgeNumber;
				$("#badgeNumber").val(value).trigger("change");
			},
			maxNumberOfElements : 10,
			match : {
				enabled : true
			},
		}
	}
	var options2 = {
		url : function(phrase) {
			return "/v2/autocomplete/progetto?bu=792";
		},
		getValue : function(result) {
			return result.description;
		},
		
		list : {
			
			onSelectItemEvent: function() {
				var first = getUrlVars()["month"];
				var value= [$("#projectDesc").getSelectedItemData().customer,
				           $("#projectDesc").getSelectedItemData().currency,
				           $("#projectDesc").getSelectedItemData().type,
				           $("#projectDesc").getSelectedItemData().idProject,
				            first];
				$("#customer").val(value[0]).trigger("change");
				$("#currency").val(value[1]).trigger("change");
				$("#activityType").val(value[2]).trigger("change");
				$("#idProject").val(value[3]).trigger("change");
			},
			
			maxNumberOfElements : 10,
			match : {
				enabled : true
			},
		}
	}
	$('#employeeDesc').focus(function() {
		$('#badgeNumber').val('');
	});
	$("#employeeDesc").easyAutocomplete(options);
	$("#projectDesc").easyAutocomplete(options2);
	
	$('#v2').editableTableWidget();
	
	$('#v2').on('change', function(evt,newValue){
		
		var data = newValue;
		var colname = $(evt.target).attr('colname');
		var id = $(evt.target).parent().attr('rowId');
		
		var url = "/v2/update?month=201606&bu=792&id="+ id +"&colname="+colname+"&value="+data;
		
		$.ajax({
			type: 'POST',
			url: url,
			success: function(result) {
				$('#responsecode').val(result.code);
			},
			async:false
		});
		
		if ($('#responsecode').val() < 0) {
			return false;
		}
	});	
	
	// funzioni esterne al document ready
	
	function detail(id) {
		$.get("/v2/record/detail/" + id,
				function(data) {
					console.log(data);
					for ( var key in data) {
						var value = data[key];
						$('#' + key).val(value);
					}
					$('#employeeDesc').val(data.employeeDesc).change();
					$('#projectDesc').val(data.projectDesc).change();
				});
	}
	function validate(form) {
		if(confirm("Vuoi approvare questo mese?")) {
			return true;
		}
		else {
			return false;
		}
	}

	function getUrlVars() {
	    var vars = {};
	    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
	        vars[key] = value;
	    });
	    return vars;
	}

	function allineaProduzione() {
		
		if (confirm('Stai per allineare il prodotto con il lavorato su tutte le risorse. Sei sicuro di voler continuare?')) {
			
			window.location = "/v2/allineaproduzione?month=201606&bu=792";
		}
		
	}	