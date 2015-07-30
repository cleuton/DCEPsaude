Date.prototype.yyyymmdd = function() {
   var yyyy = this.getFullYear().toString();
   var mm = (this.getMonth()+1).toString(); // getMonth() is zero-based
   var dd  = this.getDate().toString();
   return yyyy + "-" + (mm[1]?mm:"0"+mm[0]) + "-" + (dd[1]?dd:"0"+dd[0]); // padding
  };
var qParRegiao="";
var regioes = [ "Região inválida",
               	"Rio de Janeiro - Zona Sul",
               	"Rio de Janeiro - Zona Norte", 
               	"Rio de Janeiro - Zona Oeste",
               	"Rio de Janeiro - Leopondina",
               	"Duque de Caxias",
               	"Niterói",
               	"Nova Iguaçú",
               	"São Gonçalo",
               	"Queimados"
              ];
var sintomas = ["Sintoma inválido",
                "Dor de cabeça",
                "Enjôo",
                "Vômito",
                "Diarréia",
                "Febre",
                "Dor muscular",
                "Rigidez na nuca",
                "Dor abdominal"
                ];
var cores = [
             "#000000",
             "#33CC00",
             "#33CCFF",
             "#660000",
             "#9900FF",
             "#999900",
             "#CC9900",
             "#66CCFF",
             "#FF9933"
             
             ];
var agora = new Date();
var myRadar = null;
var CONSERPRO_SINTOMAS = {
    /*Makes the AJAX call (synchronous) to load a Student Data*/
    loadSintomasData: function () {
        var sintomasProcessados = [];
        $.ajax({
            async: false,
            //url: "StudentJsonDataServlet",
            url: "http://localhost:3000/api/snapshot?dtInicio=" + getDtInicio() + "&dtFim=" + getDtFim() + qParRegiao,
            dataType: "json",
            method: "get",
            success: function (resultado) {

                console.log(JSON.stringify(resultado));

                $.each(resultado.data.mensagem, function (index, sintoma) {
                	sintomasProcessados.push(
                			{
                		        value: sintoma.value,
                		        color: cores[sintoma._id],
                		        highlight: "#FFFF66",
                		        label: sintomas[sintoma._id]
                		    }
                			);
                });
            }
        });
        return sintomasProcessados;
    },
    /*Crate the custom Object with the data*/
    createChartData: function () {

        return {
        	    //Boolean - Whether we should show a stroke on each segment
        	    segmentShowStroke : true,

        	    //String - The colour of each segment stroke
        	    segmentStrokeColor : "#fff",

        	    //Number - The width of each segment stroke
        	    segmentStrokeWidth : 2,

        	    //Number - The percentage of the chart that we cut out of the middle
        	    percentageInnerCutout : 50, // This is 0 for Pie charts

        	    //Number - Amount of animation steps
        	    animationSteps : 100,

        	    //String - Animation easing effect
        	    animationEasing : "easeOutBounce",

        	    //Boolean - Whether we animate the rotation of the Doughnut
        	    animateRotate : true,

        	    //Boolean - Whether we animate scaling the Doughnut from the centre
        	    animateScale : false,

        	    //String - A legend template
        	    legendTemplate : "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<segments.length; i++){%><li><span style=\"background-color:<%=segments[i].fillColor%>\"></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>"

        	};

    },
    /*Renders the Chart on a canvas and returns the reference to chart*/
    renderSintomasChart: function (data,options) {

        var context2D = document.getElementById("canvas").getContext("2d");
        var pizza = new Chart(context2D).Pie(data,options);

        return pizza;
    },
    /*Initalization Sintoms Render Chart*/
    initRadarChart: function () {

        var sintomasData = CONSERPRO_SINTOMAS.loadSintomasData();

        chartData = CONSERPRO_SINTOMAS.createChartData();

        myRadar = CONSERPRO_SINTOMAS.renderSintomasChart(sintomasData,chartData);

    }
};


function getDtInicio() {
	var ini30 = new Date();
	ini30.setDate(agora.getDate() - 30);
	return ini30.yyyymmdd();
}

function getDtFim() {
	return agora.yyyymmdd();
}

$(document).ready(function () {
    CONSERPRO_SINTOMAS.initRadarChart();
    $("#regiaoSelect").change(function() {
    		var reg = $("#regiaoSelect").val();
    		if (reg == 0) {
    			qParRegiao = "";
    		}
    		else {
        		qParRegiao = "&regiao=" + $("#regiaoSelect").val();
    		}
    		var ctx = $("#canvas").get(0).getContext("2d");
    		ctx.clearRect(0, 0, ctx.width, ctx.height);
    		myRadar.destroy();
    		CONSERPRO_SINTOMAS.initRadarChart();
    	}
    );
});