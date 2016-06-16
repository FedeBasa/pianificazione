$(document).ready(function() {
	$(".custom button").click(function(){
		var id=$(this).attr('id');
		var path="";
		var form="#form1";
		switch(id){
			case "upload_p":
			path="/v2/excel/upload/project";
			break;
			case "save_p":
			path="/v2/excel/save/project";
			break;
			case "replace_p":
			path="/v2/excel/replace/project";
			break;
			case "upload_e":
			path="/v2/excel/upload/employee";
			break;
			case "save_e":
			path="/v2/excel/save/employee";
			break;
			case "replace_e":
			path="/v2/excel/replace/employee";
			break;
			default:
			path="";
		}
		if(id.endsWith("p")){
			form="#form2";
		}
		$(form).attr("action",path).submit();
	});
});